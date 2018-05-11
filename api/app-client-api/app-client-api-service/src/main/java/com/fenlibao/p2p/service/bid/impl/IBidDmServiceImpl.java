package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.common.CGModeEnum;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.bid.IBidDmService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/17.
 * 优化修改 20151019
 */
@Service
public class IBidDmServiceImpl extends BaseServiceDmImpl implements IBidDmService {

    private static final Logger logger = LoggerFactory.getLogger(IBidDmServiceImpl.class);
    
    @Resource
    private SpecialUserService specialUserService;

    private Map<String, String> doBidKernel(Connection connection, final int bidId, final BigDecimal amount, final int accountId, String experFlg,String fxhbIds,boolean planFlag, OperationTypeEnum operationTypeEnum) throws Throwable {
        // 查询是否有逾期未还
        int count = selectYqInfo(connection, accountId);
        if (count > 0) {
            throw new BusinessException(ResponseCode.BIT_LOAN_OVERDUE); //借款逾期未还，不能出借！
        }

        //自动配标/复投，小数也可以
        if(!OperationTypeEnum.PLAN.equals(operationTypeEnum) && !specialUserService.isSpecial(String.valueOf(accountId), SpecialUserType.ALL) ){
            //出借金额必须为整数 kris
            BigDecimal _a = amount.stripTrailingZeros();
            if (_a.toPlainString().contains(".")) {
                throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //出借金额必须为整数
            }
        }

        if (!specialUserService.isSpecial(String.valueOf(accountId), SpecialUserType.ALL) && !planFlag) { //特殊用户不进行校验
            // 校验最低起投金额
            BigDecimal min = new  BigDecimal(InterfaceConst.BID_MIX_AMOUNT);  //PS:这里应该是动态获取资源
            if (amount.compareTo(min) < 0) {
                throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); //出借金额不能低于最低起投金额
            }
        }

        //判断是否为100 的整数倍
            /*去除投资金额为100的整数倍 update by laubrence 2016-4-25 11:39:47*/
            /*BigDecimal remainder = amount.remainder(InterfaceConst.INTEGER_ROUNDING_LIMIT);
            if (remainder.compareTo(new BigDecimal(0)) > 0){
            	throw new LogicalException(ResponseCode.USER_IS_HAVE_BID.getCode()); //出借金额必须为100的整数倍
            }*/

        //updated by zcai 20160712 不需要限制用户同一分钟只能投一次,更改为redis防重复提交
        //限制每个用户同一次出借频率为1分钟
//            Timestamp lastOrderTime = getUserBidLastOrder(connection, accountId, bidId);
//            if(lastOrderTime!=null){
//            	Timestamp currDate =  getCurrentTimestamp(connection);
//            	if(currDate.getTime() - lastOrderTime.getTime() < 60*1000){
//            		throw new BusinessException(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT); //出借过于频繁
//            	}
//            }

        Map<String, String> rtnMap = new HashMap<String, String>();

        // 锁定标
        T6230 t6230 = selectT6230(connection, bidId);
        if (t6230 == null) {
            throw new BusinessException(ResponseCode.BID_NOT_EXIST);     //指定的标记录不存在
        }
        boolean zjb = BooleanParser.parse(InterfaceConst.BID_SFZJKT); //PS:这里应该是动态获取资源
        if (zjb && accountId == t6230.F02) {
            throw new BusinessException(ResponseCode.BID_BORROWER_CANNOT_INVESTMENT);     //您是该标的借款人，不能出借
        }
        if (t6230.F20 != T6230_F20.TBZ) {
            logger.error("[{}]标不是出借中状态[{}],不能出借,accountId=[{}]", bidId, t6230.F20.name(), accountId);
            throw new BusinessException(ResponseCode.BID_FULLED);     //指定的标不是出借中状态,不能出借  //160617按产品要求提示 该项目已满标，请投资其他项目
        }
        if (amount.compareTo(t6230.F07) > 0) {
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);     //出借金额大于可投金额
        }
        //过滤新手标（通过计划投资的情况，不进行校验）
        if (!specialUserService.isSpecial(String.valueOf(accountId), SpecialUserType.ALL) && !planFlag) { //特殊用户不进行校验
            if ("S".equals(t6230.xsb.name())) {
                // 添加判断 非新用户不能投资新手标
                int flag = isUserHaveInvest(connection, accountId);
                if (flag > 0) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_NEW_USER);
                }
                BigDecimal max = new BigDecimal(Config.get("bid.novice.invest.limit"));
                //新手标不能大于最大限投金额3000 (laubrence 2015-12-15 15:29:58)
                //新手标不能大于最大限投金额10000 (laubrence 2016-3-16 15:54:16)
                if (amount.compareTo(max) > 0) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_OVER_MAX_AMOUNT); //新手标出借金额不能大于最大限制金额
                }
            }
        }
        //add 20151019 wangyunjing 判断是否包含返现红包
        if(!StringUtils.isBlank(fxhbIds)){
            // 非新手标才可以使用返现红包  (laubrence 2015-12-15 15:50:03)
            if("S".equals(t6230.xsb.name())){
                throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_FXHB);
            }

            //前台传过来的返现红包id
            String[] hbIdArr = fxhbIds.split("\\|");
            Map<String,UserRedPacketInfo> redPacketInfoMap = new HashMap<String,UserRedPacketInfo>();
            for (int i = 0; i < hbIdArr.length ; i++) {
                UserRedPacketInfo redPacketInfo = selectHbInfo(connection,accountId,hbIdArr[i]);
                /**
                 * 校验返现红包的使用规则
                 * 1.是否有对应红包；2.返现红包是否使用过；3.返现红包是否已经过期；
                 * 4.投资金额<返现红包最少投资金额；5.投资金额能获取的返现红包数量。
                 * 6.投资期限；7.标类型
                 */
                if(redPacketInfo != null){

                    String useStatus = redPacketInfo.getStatus();
                    BigDecimal useMoney = redPacketInfo.getHbBalance();
                    BigDecimal leastMoney = redPacketInfo.getConditionBalance();
                    Timestamp end_date = redPacketInfo.getTimestamp();  //截止日期
                    Timestamp now_date = getCurrentTimestamp(connection);
                    if(Integer.valueOf(useStatus) == 2){
                        throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_IS_USED);    //返现红包已经使用
                    }

                    if(end_date.compareTo(now_date) < 0){
                        throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_IS_OVERDUE);  //返现红包已经过期
                    }

                    if(redPacketInfoMap.size() > 0){
                        if (amount.compareTo(leastMoney) < 0){
                            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);
                        }else {
                            BigDecimal fxhbTotail = new BigDecimal(0);
                            Iterator entries = redPacketInfoMap.entrySet().iterator();
                            fxhbTotail = fxhbTotail.add(leastMoney);
                            while (entries.hasNext()) {
                                Map.Entry entry = (Map.Entry) entries.next();
                                UserRedPacketInfo packetInfoTmp = (UserRedPacketInfo) entry.getValue();
                                BigDecimal dkLeastMoney = packetInfoTmp.getConditionBalance();
                                fxhbTotail = fxhbTotail.add(dkLeastMoney);
                            }

                            if (amount.compareTo(fxhbTotail) < 0){
                                throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);  //返现红包使用不符合规则
                            }else {
                                redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()),redPacketInfo);
                            }
                        }
                    }else {
                        if (amount.compareTo(leastMoney) < 0){
                            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED); //出借金额不能使用多少钱的返现红包
                        }else {
                            redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()),redPacketInfo);
                        }
                    }

                }else{
                    throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);  //返现红包不存在
                }
            }
        }

        amount.setScale(InterfaceConst.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);

        //剩余可投金额不能小于0 create by laubrence 2016-3-30 10:54:18
        t6230.F07 = t6230.F07.subtract(amount);
        if (t6230.F07.compareTo(new BigDecimal(0)) < 0 ) {
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //剩余可投金额不能低于最低起投金额
        }

        // 锁定投资人资金账户
        T6101 investor = null;
        if(OperationTypeEnum.PLAN.equals(operationTypeEnum)){
            investor = selectT6101(connection, accountId, t6230.F38 == CGModeEnum.CG.getIndex()?SysFundAccountType.XW_INVESTOR_SDZH: SysFundAccountType.SDZH);
            if (investor == null) {
                throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST); //用户锁定账户不存在
            }
        }else{
            investor = selectT6101(connection, accountId, t6230.F38 == CGModeEnum.CG.getIndex()?SysFundAccountType.XW_INVESTOR_WLZH: SysFundAccountType.WLZH);
            if (investor == null) {
                throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST); //用户往来账户不存在
            }
        }

        if (investor.F06.compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //账户余额不足
        }

        int ordId = 0;
        // 插入出借订单
        ordId = insertT6501(connection, FeeCode.BIDORDERTYPE, T6501_F03.DTJ, T6501_F07.YH, accountId);
        insertT6504(connection, ordId, accountId, bidId, amount);
        rtnMap.put("orderId", String.valueOf(ordId));
        rtnMap.put("fxhbIds",fxhbIds);
        // 插入体验金订单
        if (!StringHelper.isEmpty(experFlg))
        {
            int expOrdId = insertExperienceOrd(bidId, connection, ordId,accountId);
            rtnMap.put("expOrdId", String.valueOf(expOrdId));
        }
        return rtnMap;
    }

    @Override
    public Map<String, String> doBid(final int bidId, final BigDecimal amount, final int accountId, String experFlg, String fxhbIds) throws Throwable {
        if (bidId <= 0) {
            throw new BusinessException(ResponseCode.BID_NOT_EXIST);
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_GT0);
        }
       
        Connection connection = DbPoolConnection.getInstance().getConnection();
        try{
            connection.setAutoCommit(false);
            Map<String, String> rtnMap = doBidKernel(connection, bidId, amount, accountId, experFlg, fxhbIds, true, null);
            connection.commit();
            return rtnMap;
        }catch (Exception e){
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public Map<String, String> doBid(Connection connection, final int bidId, final BigDecimal amount, final int accountId, String experFlg, String fxhbIds, boolean planFlag, OperationTypeEnum operationTypeEnum) throws Throwable {
        if (bidId <= 0) {
            throw new BusinessException(ResponseCode.BID_NOT_EXIST);
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_GT0);
        }

        return doBidKernel(connection, bidId, amount, accountId, experFlg, fxhbIds, planFlag, operationTypeEnum);
    }

    /**
     * 根据红包id 获取红包相关信息
     * @param connection
     * @param hbId
     */
    private UserRedPacketInfo selectHbInfo(Connection connection, int accountId, String hbId) throws SQLException{
        UserRedPacketInfo urpInfo = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT red.id AS hbId,red.red_type AS type,red.red_money AS hbBalance,red.invest_money AS conditionBalance,relation.status AS STATUS,relation.valid_time AS TIMESTAMP "
                        + "FROM flb.t_red_packet AS red,flb.t_user_redpackets AS relation "
                        + "WHERE red.id = relation.redpacket_id AND relation.user_id = ? AND relation.status = ? "
                        + "AND relation.id = ? ")) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2,1);  //应该传递红包类型
            pstmt.setInt(3, Integer.valueOf(hbId));  //根据红包id 查询红包信息
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    urpInfo = new UserRedPacketInfo();
                    urpInfo.setHbId(resultSet.getInt(1));
                    urpInfo.setType(resultSet.getString(2));
                    urpInfo.setHbBalance(resultSet.getBigDecimal(3));
                    urpInfo.setConditionBalance(resultSet.getBigDecimal(4));
                    urpInfo.setStatus(resultSet.getString(5));
                    urpInfo.setTimestamp(resultSet.getTimestamp(6));
                }
            }
        }
        return urpInfo;
    }


    @Override
    public int bid(int accountId,int bidId, BigDecimal amount) throws Throwable {
        if (bidId <= 0) {
            throw new ParameterException("没有指定要投的标");
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new ParameterException("出借金额必须大于零");
        }

        try (Connection connection = DbPoolConnection.getInstance().getConnection()) {

            // 查询是否有逾期未还
            int count = selectYqInfo(connection, accountId);
            if (count > 0) {
                throw new LogicalException("借款逾期未还，不能出借！");
            }

            int ordId = 0;
            BigDecimal _a = amount.stripTrailingZeros();
            if (_a.toPlainString().contains(".")) {
                throw new LogicalException("出借金额必须为整数");
            }
            amount.setScale(InterfaceConst.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);

            connection.setAutoCommit(false);
            // 锁定标
            T6230 t6230 = selectT6230(connection, bidId);
            if (t6230 == null) {
                throw new LogicalException("指定的标记录不存在");
            }

            boolean zjb = BooleanParser.parse(InterfaceConst.BID_SFZJKT);
            if (!zjb && accountId == t6230.F02) {
                throw new LogicalException("您是该标的借款人，不能出借");
            }
            if (t6230.F20 != T6230_F20.TBZ) {
                throw new LogicalException("指定的标不是出借中状态,不能出借");
            }
            if (amount.compareTo(t6230.F07) > 0) {
                throw new LogicalException("出借金额大于可出借额");
            }

            // 校验最低起投金额
            BigDecimal min = BigDecimalParser.parse(InterfaceConst.MIN_BIDING_AMOUNT);
            if (amount.compareTo(min) < 0) {
                throw new LogicalException("出借金额不能低于最低出借额");
            }
            t6230.F07 = t6230.F07.subtract(amount);
            if (t6230.F07.compareTo(new BigDecimal(0)) > 0
                    && t6230.F07.compareTo(min) < 0) {
                throw new LogicalException("剩余可出借额不能低于最低出借额");
            }
            // 锁定投资人资金账户
            T6101 investor = selectT6101(connection, accountId, t6230.F38 == CGModeEnum.CG.getIndex()?SysFundAccountType.XW_INVESTOR_WLZH: SysFundAccountType.WLZH);
            if (investor == null) {
                throw new LogicalException("用户往来账户不存在");
            }
            if (investor.F06.compareTo(amount) < 0) {
                throw new LogicalException("账户余额不足");
            }

            // 插入出借订单
            ordId = insertT6501(connection, FeeCode.BIDORDERTYPE, T6501_F03.DTJ, T6501_F07.YH, accountId);
            insertT6504(connection, ordId, accountId, bidId, amount);
            connection.commit();
            connection.setAutoCommit(true);

            return ordId;
        }
    }

    @Override
    public T6230 selectT6230(int bidId) throws Throwable {
        return null;
    }

    @Override
    public T6504 selectT6504(int orderId) throws Throwable {
        T6504 record = null;
        try (Connection connection =  DbPoolConnection.getInstance().getConnection()){
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6504 WHERE T6504.F01 = ?"))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6504();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getInt(5);
                    }
                }
            }
            return record;
        }
    }


    private int selectYqInfo(Connection connection, int F03)
            throws SQLException {
        int count = 0;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT COUNT(1) FROM S62.T6252 WHERE F03 = ? AND F08 < CURDATE() AND F09 = ? ")) {
            pstmt.setInt(1, F03);
            pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }

    private T6230 selectT6230(Connection connection, int F01)
            throws SQLException {
        T6230 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F28,F38 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.xsb = T6230_F28.parse(resultSet.getString(27));
                    record.F38 = resultSet.getInt(28);
                }
            }
        }
        return record;
    }

    private T6101 selectT6101(Connection connection, int F02, SysFundAccountType F03)
            throws SQLException {
        T6101 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? FOR UPDATE")) {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6101();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6101_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }


    private int insertT6501(Connection connection, int F02, T6501_F03 F03, T6501_F07 F07, int F08) throws Throwable {
        try (PreparedStatement pstmt = connection
                .prepareStatement(
                        "INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F07 = ?, F08 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setString(4, F07.name());
            pstmt.setInt(5, F08);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    protected Timestamp getCurrentTimestamp(Connection connection) throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }

    private void insertT6504(Connection connection, int F01, int F02, int F03,
                             BigDecimal F04) throws SQLException {
        try (PreparedStatement pstmt = connection
                .prepareStatement("INSERT INTO S65.T6504 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?")) {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.execute();
        }
    }

    public int insertExperienceOrd(int bidId, Connection connection, int ordId, int accountId)
            throws Throwable
    {
        // 获取用户ID
        // 锁定用户体验金
        T6103 t6103 = selectT6103(connection, accountId);
        // 判断用户是否有体验金
        if (t6103 == null || t6103.F03.compareTo(BigDecimal.ZERO) == 0)
        {
            return 0;
        }
        int expOrdId =
                insertT6501(connection, FeeCode.BIDEXPERIENCE, T6501_F03.DTJ, T6501_F07.YH, accountId);

        insertT6518(connection, expOrdId, accountId, bidId, t6103.F03, ordId);

        return expOrdId;
    }

    private T6103 selectT6103(Connection connection, int accountId)
            throws Throwable
    {
        T6103 record = null;
        Date currDate =  getCurrentDate(connection);
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, SUM(F03), F04, F05, F06, F07, F08, F09, F10 FROM S61.T6103 WHERE T6103.F02 = ? AND T6103.F06 = ? AND T6103.F04 <= ? AND T6103.F05 >= ? FOR UPDATE")) {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, T6103_F06.WSY.name());
            pstmt.setDate(3, currDate);
            pstmt.setDate(4, currDate);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6103();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6103_F06.parse(resultSet.getString(6));
                    record.F07 = resultSet.getInt(7);
                    record.F08 = T6103_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getTimestamp(10);
                }
            }
        }
        return record;
    }

    private void insertT6518(Connection connection, int F01, int F02, int F03,
                             BigDecimal F04, int ordId) throws SQLException {
        try (PreparedStatement pstmt = connection
                .prepareStatement("INSERT INTO S65.T6518 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?")) {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.setInt(5, ordId);
            pstmt.execute();
        }
    }
    
	private T6251 selectT6251(Connection connection, int userId)
			throws SQLException {
		T6251 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01 FROM S62.T6251 WHERE T6251.F03 = ? ")) {
			pstmt.setInt(1, userId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6251();
					record.F01 = resultSet.getInt(1);
					return record;
				}
			}
		}
		return record;
	}
	
	private int isUserHaveInvest(Connection connection, int userId)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("select t.F01 from s62.t6250 t where t.F03 = ? union all select s.F01 from s62.t6251 s where s.F04 = ? limit 1")) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, userId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}
	
	private int isUserHaveInvestBid(Connection connection, int userId, int bidId)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("select s.F01 bidId from S62.T6250 t inner join S62.T6230 s on t.F02=s.F01 where t.F02= ? and t.F03= ? limit 1")) {
			pstmt.setInt(1, bidId);
			pstmt.setInt(2, userId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}
	
	private Timestamp getUserBidLastOrder(Connection connection, int userId, int bidId)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT t.F01,t.F04 FROM S65.t6501 t INNER JOIN s65.t6504 s on s.F01 = t.F01 WHERE s.F02 = ? and s.F03 = ? ORDER BY t.F04 desc LIMIT 1")) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bidId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getTimestamp(2);
				}
			}
		}
		return null;
	}
    
}
