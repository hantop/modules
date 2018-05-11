package com.fenlibao.p2p.service.bid.migrate.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S62.entities.*;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.bid.migrate.AbstractOrderExecutor;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by LouisWang on 2015/8/10.
 */
@Service
public class TenderOrderExecutor extends AbstractOrderExecutor {
    private static final Logger logger= LogManager.getLogger(TenderOrderExecutor.class);

    @Override
    protected void doConfirm(Connection connection, int orderId, Map<String, String> params, boolean checkAmountFlag, boolean isSetLetter) throws Throwable {
        Map<String,Object> fxhbMap = null;
        try{
            //String bidType = params.get("bidType");
            // 订单详情
            T6504 t6504 = selectT6504(connection, orderId);
            if (t6504 == null) {
                throw new LogicalException("30610"); //订单明细记录不存在
            }
            // 标的详情,锁定标
            T6230 t6230 = selectT6230(connection, t6504.F03);
            if (t6230 == null) {
                throw new LogicalException("30602");     //标记录不存在
            }
            boolean ajkt = BooleanParser.parse(InterfaceConst.BID_SFZJKT);
            if (!ajkt && t6504.F02 == t6230.F02) {
                throw new LogicalException("30611");     //不可投本账号发的标
            }
            if (t6230.F20 != T6230_F20.TBZ) {
                throw new LogicalException("30604");    //不是投标中状态,不能投标
            }
            if (t6504.F04.compareTo(t6230.F07) > 0) {
                if (params != null) {
                    logger.error(String.format(
                            "%s %s投标异常：投标金额大于可出借额，投标订单%s，冻结订单%s，冻结流水%s",
                            DateTimeParser.format(new java.util.Date()),
                            t6230.F01, orderId, params.get("freezeOrdId"),
                            params.get("freezeTrxId")));

                }
                throw new LogicalException("30605"); //投标金额大于可投金额
            }

            if (checkAmountFlag) { //不进行校验
                // 校验最低起投金额
                //    BigDecimal min = BigDecimalParser.parse(InterfaceConst.MIN_BIDING_AMOUNT);
                // 校验最低起投金额
                BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);

                if (t6504.F04.compareTo(min) < 0) {
                    throw new LogicalException("30606");    //投标金额不能低于最低起投金额
                }
            }

            //剩余可投金额不能小于0 create by laubrence 2016-3-30 11:20:42
            t6230.F07 = t6230.F07.subtract(t6504.F04);
            if (t6230.F07.compareTo(BigDecimal.ZERO) <0) {
                throw new LogicalException("30607");    //剩余可投金额不能低于最低起投金额
            }
            String fxhbIds = params.get("fxhbIds");
            Map<String,UserRedPacketInfo> redPacketInfoMap = new HashMap<String,UserRedPacketInfo>();
            //add 20151019 wangyunjing 判断是否包含返现红包
            if(!StringUtils.isBlank(fxhbIds)){
                //前台传过来的返现红包id
                String[] hbIdArr = fxhbIds.split("\\|");
                for (int i = 0; i < hbIdArr.length ; i++) {
                    UserRedPacketInfo redPacketInfo = selectHbInfo(connection,t6504.F02,hbIdArr[i]);
                    /**
                     * 校验返现红包的使用规则
                     * 1.是否有对应红包；2.返现红包是否使用过；3.返现红包是否已经过期；
                     * 4.出借金额<返现红包最少出借金额；5.出借金额能获取的返现红包数量。
                     */
                    if(redPacketInfo != null && !"".equals(redPacketInfo)){

                        String useStatus = redPacketInfo.getStatus();
                        BigDecimal useMoney = redPacketInfo.getHbBalance();
                        BigDecimal leastMoney = redPacketInfo.getConditionBalance();
                        Timestamp end_date = redPacketInfo.getTimestamp();  //截止日期
                        Timestamp now_date = getCurrentTimestamp(connection);
                        if(Integer.valueOf(useStatus) == 2){
                            throw new LogicalException("30701&" + useMoney);    //返现红包已经使用
                        }

                        if(end_date.compareTo(now_date) < 0){
                            throw new LogicalException("30702&" + useMoney);  //返现红包已经过期
                        }

                        if(redPacketInfoMap.size() > 0){
                            if (t6504.F04.compareTo(leastMoney) < 0){
                                throw new LogicalException("30703&" + useMoney);
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

                                if (t6504.F04.compareTo(fxhbTotail) < 0){
                                    throw new LogicalException("30704&");  //返现红包使用不符合规则
                                }else {
                                    redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()),redPacketInfo);
                                }
                            }
                        }else {
                            if (t6504.F04.compareTo(leastMoney) < 0){
                                throw new LogicalException("30703&" + useMoney); //投标金额不能使用多少钱的返现红包
                            }else {
                                redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()),redPacketInfo);
                            }
                        }

                    }else{
                        throw new LogicalException("30700&");  //返现红包不存在
                    }
                }
            }

            int pid = getPTID(connection); // 平台用户id
            if (pid <= 0) {
                throw new LogicalException("30612");    //平台账号不存在
            }

            // 平台往来账户信息
            T6101 ptwlzh = selectT6101(connection, pid, SysFundAccountType.WLZH);
            if (ptwlzh == null) {
                throw new LogicalException("30608");    //平台往来账户不存在
            }
            if (params != null) {
                String trxId = params.get("trxId");
                int freezeOrdId = IntegerParser.parse(params.get("freezeOrdId"));
                String freezeTrxId = params.get("freezeTrxId");
                // 更新流水号
                updateT6501(connection, trxId, t6504.F01);
                if (freezeOrdId > 0) {
                    updateT6501(connection, freezeTrxId, freezeOrdId);
                }
            }
            // 扣减可投金额
            try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S62.T6230 SET F07 = ? WHERE F01 = ?")) {
                pstmt.setBigDecimal(1, t6230.F07);
                pstmt.setInt(2, t6504.F03);
                pstmt.execute();
            }
            // 锁定出借人资金账户
            T6101 czzh = selectT6101(connection, t6504.F02, SysFundAccountType.WLZH);
            if (czzh == null) {
                throw new LogicalException("30613");    //出借人往来账户不存在
            }
            // 锁定入账账户
            T6101 rzzh = null;
            String msg = null;
            int feeCode = 0;
            if (t6230.F15 == T6230_F15.S) {
                rzzh = selectT6101(connection, t6230.F02, SysFundAccountType.WLZH);
                msg = "借款人账户不存在";
                feeCode = InterfaceConst.JK;
            } else {
                rzzh = selectT6101(connection, t6504.F02, SysFundAccountType.SDZH);
                msg = "出借人锁定账户不存在";
                //feeCode = InterfaceConst.TZ;
                feeCode = Integer.valueOf(t6230.F04 +""+InterfaceConst.TZ);
            }
            if (rzzh == null) {
                throw new LogicalException("30614");    //出借人锁定账户不存在
            }
            {
                // }
                // 扣减出账账户金额
                czzh.F06 = czzh.F06.subtract(t6504.F04);
                if (czzh.F06.compareTo(BigDecimal.ZERO) < 0) {
                    throw new LogicalException("30609");    //账户金额不足
                }

                updateT6101(connection, czzh.F06, czzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = czzh.F01;
                t6102.F03 = Integer.valueOf(t6230.F04 +""+InterfaceConst.TZ);
                t6102.F04 = rzzh.F01;
                t6102.F07 = t6504.F04;
                t6102.F08 = czzh.F06;
                t6102.F09 = String.format("散标出借:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            {
                // 增加入账账户金额
                rzzh.F06 = rzzh.F06.add(t6504.F04);
                updateT6101(connection, rzzh.F06, rzzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = rzzh.F01;
                t6102.F03 = feeCode;
                t6102.F04 = czzh.F01;
                t6102.F06 = t6504.F04;
                t6102.F08 = rzzh.F06;
                t6102.F09 = String.format("散标出借:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            // 插入投标记录
            T6250 t6250 = new T6250();
            t6250.F02 = t6230.F01;
            t6250.F03 = t6504.F02;
            t6250.F04 = t6504.F04;
            t6250.F01 = t6504.F05;
            // 判断计息金额与标总金额是否一致
            if (t6230.F05.compareTo(t6230.F26) == 0) {
                t6250.F05 = t6504.F04;
            } else {
                t6250.F05 = t6230.F26.multiply(t6504.F04).divide(t6230.F05, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            t6250.F07 = T6250_F07.F;
            int rid = insertT6250(connection, t6250);
            try (PreparedStatement ps = connection
                    .prepareStatement("UPDATE S65.T6504 SET F05 = ? WHERE F01 = ?")) {
                ps.setInt(1, rid);
                ps.setInt(2, t6504.F01);
                ps.executeUpdate();
            }
//            if (t6230.F15 == T6230_F15.S) {
//                // 自动放款
//                Date currentDate = getCurrentDate(connection);
//                T6251 t6251 = new T6251();
//                {
//                    // 插入债权
//                    t6251.F02 = zqCode(rid);
//                    t6251.F03 = t6230.F01;
//                    t6251.F04 = t6504.F02;
//                    t6251.F05 = t6250.F04;
//                    t6251.F06 = t6250.F04;
//                    t6251.F07 = t6250.F04;
//                    t6251.F08 = T6251_F08.F;
//                    t6251.F09 = currentDate;
//                    t6251.F10 = new Date(currentDate.getTime() + t6230.F19
//                            * DateHelper.DAY_IN_MILLISECONDS);
//                    t6251.F11 = rid;
//                    t6251.F01 = insertT6251(connection, t6251);
//                }
//                // 生成还款计划
//                //   hkjh(connection, t6230, t6251);
//                // 收成交服务费
//                T6238 t6238 = selectT6238(connection, t6230.F01);
//                if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0) {
//                    BigDecimal fwf = t6238.F02.multiply(t6504.F04);
//                    // updateT6101(connection, wlzh.F06, wlzh.F01);
//                    {
//                        // 平台资金增加
//                        ptwlzh.F06 = ptwlzh.F06.add(fwf);
//                        updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
//                        T6102 t6102 = new T6102();
//                        t6102.F02 = ptwlzh.F01;
//                        t6102.F03 = InterfaceConst.CJFWF;
//                        t6102.F04 = rzzh.F01;
//                        t6102.F06 = fwf;
//                        t6102.F08 = ptwlzh.F06;
//                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
//                        insertT6102(connection, t6102);
//                    }
//                    {
//                        // 借款人账户减少
//                        rzzh.F06 = rzzh.F06.subtract(fwf);
//                        updateT6101(connection, rzzh.F06, rzzh.F01);
//                        T6102 t6102 = new T6102();
//                        t6102.F02 = rzzh.F01;
//                        t6102.F03 = InterfaceConst.CJFWF;
//                        t6102.F04 = ptwlzh.F01;
//                        t6102.F07 = fwf;
//                        t6102.F08 = rzzh.F06;
//                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
//                        insertT6102(connection, t6102);
//                    }
//                }
//                try (PreparedStatement pstmt = connection
//                        .prepareStatement("UPDATE S62.T6231 SET F12 = ? WHERE F01 = ?")) {
//                    pstmt.setTimestamp(1, getCurrentTimestamp(connection));
//                    pstmt.setInt(2, t6230.F01);
//                    pstmt.execute();
//                }
//                if (t6230.F07.compareTo(BigDecimal.ZERO) <= 0) {// 满标
//                    try (PreparedStatement pstmt = connection
//                            .prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?")) {
//                        pstmt.setTimestamp(1, getCurrentTimestamp(connection));
//                        pstmt.setInt(2, t6230.F01);
//                        pstmt.execute();
//                    }
//                    try (PreparedStatement pstmt = connection
//                            .prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?")) {
//                        pstmt.setString(1, T6230_F20.HKZ.name());
//                        pstmt.setInt(2, t6230.F01);
//                        pstmt.execute();
//                    }
//                }
//            }else{she
            /* 剩余可投金额小于100时自动封标  update by laubrence 2016-3-25 15:39:02*/
            if (t6230.F07.compareTo(new BigDecimal(100)) < 0) {// 满标
                try (PreparedStatement pstmt = connection
                        .prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?")) {
                    pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                    pstmt.setInt(2, t6230.F01);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt = connection
                        .prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?")) {
                    pstmt.setString(1, T6230_F20.DFK.name());
                    pstmt.setInt(2, t6230.F01);
                    pstmt.execute();
                }
            }

            if(isSetLetter){
                String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", t6230.F03).replace("#{investSum}", t6504.F04.toString());
                // 发站内信
                T6110 t6110 = selectT6110(connection, t6504.F02);
                String znxSuffixContent = Sender.get("znx.suffix.content");
                sendLetter(connection, t6504.F02, "系统消息", content+znxSuffixContent);
                //发短信
                sendMsg(connection, t6110.F04, content);
            }

            //是否获得返现红包
            /*if(!redPacketInfoMap.isEmpty() && redPacketInfoMap.size() > 0){
                getRedPacketByHbid(connection, t6504, redPacketInfoMap);
            }*/
        }catch (Exception e){
            logger.error(e, e);
            throw e;
        }
    }

    /**
     * @auhtor wangyunjing
     * @date 20151021
     * @todo 出借账号得到返现红包
     * @paramT6504 投标记录表
     * @param redPacketInfoMap 获取的红包
     */
    private void getRedPacketByHbid(Connection connection,T6504 t6504, Map<String, UserRedPacketInfo> redPacketInfoMap) throws Throwable{
        //获取交易的类型
        String[] ObjectNameArr = new String[]{"5116","返现红包"};//getObjectName(connection,subjectId);
        // 发站内信
        T6110 t6110 = selectT6110(connection, t6504.F02);
        Iterator entries = redPacketInfoMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            UserRedPacketInfo redPacket = (UserRedPacketInfo) entry.getValue();
            //1.红包是否可以使用
            if(Integer.valueOf(redPacket.getStatus()) == 1){
                T6101 t6101 = selectT6101(connection, t6504.F02, SysFundAccountType.WLZH);
                if (t6101 == null) {
                    throw new LogicalException("用户资金账户不存在");
                }

                // 更新用户账号信息
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F01 = ?")){
                    pstmt.setBigDecimal(1, redPacket.getHbBalance());
                    pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                    pstmt.setInt(3, t6101.F01);
                    pstmt.executeUpdate();
                }

                int ptzhId = 0;
                BigDecimal amount = BigDecimal.ZERO;
                // 发工资的账号id
                String payAccount = Config.get("redpacket.grantAccount");   //"QYZH00000000027" 开发：QYZH00000000070

                //内部转账
                String tSql = "SELECT T6101.F01,T6101.F06 FROM S61.T6101 WHERE T6101.F05='"+payAccount+"' AND F03='WLZH' FOR UPDATE";

                try (PreparedStatement pstmt = connection.prepareStatement(tSql)){

                    try (ResultSet rs = pstmt.executeQuery()){
                        if (rs.next()) {
                            ptzhId = rs.getInt(1);
                            amount = rs.getBigDecimal(2);
                        }
                    }
                }

                // 插入个人资金交易记录，内部转账
                try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 =?, F06 = ?, F08 = ?, F09 = ?")){
                    pstmt.setInt(1, t6101.F01);
                    pstmt.setInt(2, Integer.valueOf(ObjectNameArr[0]));
                    pstmt.setInt(3, ptzhId);
                    pstmt.setTimestamp(4, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(5, redPacket.getHbBalance());
                    pstmt.setBigDecimal(6, t6101.F06.add(redPacket.getHbBalance()));
                    pstmt.setString(7, String.format(ObjectNameArr[1]));
                    pstmt.execute();
                }
                // 插入平台资金交易记录，内部转账
                try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 =?, F07 = ?, F08 = ?, F09 = ?")){
                    pstmt.setInt(1, ptzhId);
                    pstmt.setInt(2, Integer.valueOf(ObjectNameArr[0]));
                    pstmt.setInt(3, t6101.F01);
                    pstmt.setTimestamp(4, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(5, redPacket.getHbBalance());
                    pstmt.setBigDecimal(6, amount.subtract(redPacket.getHbBalance()));
                    pstmt.setString(7, String.format(ObjectNameArr[1]));
                    pstmt.execute();
                }

                // 更新资金账户WLZH金额
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6101 SET F06=? WHERE F01 = ? AND F03 = 'WLZH'")){
                    pstmt.setBigDecimal(1, amount.subtract(redPacket.getHbBalance()));
                    pstmt.setInt(2, ptzhId);
                    pstmt.executeUpdate();
                }
                // 更新用户关联红包状态
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE flb.t_user_redpackets SET bid_id = ?,status = ? WHERE id = ?")){
                    pstmt.setInt(1, t6504.F03);
                    pstmt.setInt(2, 2);
                    pstmt.setInt(3, redPacket.getId());
                    pstmt.executeUpdate();
                }
                String content = Sender.get("sms.fxhb.content").replace("#{amount}", redPacket.getHbBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //发送站内信
                sendLetter(connection, t6504.F02, ObjectNameArr[1] + "发放成功", content);
                //发短信
                sendMsg(connection, t6110.F04, content);
            }
        }
    }

    protected void doConfirm2(Connection connection, int orderId, Map<String, String> params) throws Throwable {
        try{
            // 订单详情
            T6504 t6504 = selectT6504(connection, orderId);
            if (t6504 == null) {
                throw new LogicalException("订单明细记录不存在");
            }
            // 标的详情,锁定标
            T6230 t6230 = selectT6230(connection, t6504.F03);
            if (t6230 == null) {
                throw new LogicalException("标记录不存在");
            }
            boolean ajkt = BooleanParser.parse(InterfaceConst.BID_SFZJKT);
            if (!ajkt && t6504.F02 == t6230.F02) {
                throw new LogicalException("不可出借本账号发的标");
            }
            if (t6230.F20 != T6230_F20.TBZ) {
                throw new LogicalException("不是出借中状态,不能出借");
            }
            if (t6504.F04.compareTo(t6230.F07) > 0) {
                if (params != null) {
                    logger.error(String.format(
                            "%s %s投标异常：投标金额大于可出借额，投标订单%s，冻结订单%s，冻结流水%s",
                            DateTimeParser.format(new java.util.Date()),
                            t6230.F01, orderId, params.get("freezeOrdId"),
                            params.get("freezeTrxId")));

                }
                throw new LogicalException("出借金额大于可出借额");
            }
            // 校验最低起投金额
            BigDecimal min = BigDecimalParser.parse(InterfaceConst.MIN_BIDING_AMOUNT);
            if (t6504.F04.compareTo(min) < 0) {
                throw new LogicalException("出借金额不能低于最低出借额");
            }
            t6230.F07 = t6230.F07.subtract(t6504.F04);
            if (t6230.F07.compareTo(BigDecimal.ZERO) > 0
                    && t6230.F07.compareTo(min) < 0) {
                throw new LogicalException("剩余可出借额不能低于最低出借额");
            }
            int pid = getPTID(connection); // 平台用户id
            if (pid <= 0) {
                throw new LogicalException("平台账号不存在");
            }

            // 红包校验
            // 查询红包订单
            // T6523 t6523 = selectT6523(connection, orderId);
            // T6351 t6351 = null;
            // if (t6523 != null) {
            // t6351 = getT6351ById(connection, t6523.F02);
            // if (null == t6351 || t6351.F07 != T6351_F07.WSY) {
            // throw new LogicalException("红包不存在或已使用");
            // }
            // if (t6351.F04.compareTo(t6504.F04) == 1) {
            // throw new LogicalException("投标金 额必须大于使用红包金额");
            // }
            // // 跟新红包状态
            // updateRedPackageStatusToYSY(connection, t6351.F01);
            // }

            // 平台往来账户信息
            T6101 ptwlzh = selectT6101(connection, pid, SysFundAccountType.WLZH);
            if (ptwlzh == null) {
                throw new LogicalException("平台往来账户不存在");
            }
            if (params != null) {
                String trxId = params.get("trxId");
                int freezeOrdId = IntegerParser.parse(params.get("freezeOrdId"));
                String freezeTrxId = params.get("freezeTrxId");
                // 更新流水号
                updateT6501(connection, trxId, t6504.F01);
                if (freezeOrdId > 0) {
                    updateT6501(connection, freezeTrxId, freezeOrdId);
                }
            }
            // 扣减可投金额
            try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S62.T6230 SET F07 = ? WHERE F01 = ?")) {
                pstmt.setBigDecimal(1, t6230.F07);
                pstmt.setInt(2, t6504.F03);
                pstmt.execute();
            }
            // 锁定出借人资金账户
            T6101 czzh = selectT6101(connection, t6504.F02, SysFundAccountType.WLZH);
            if (czzh == null) {
                throw new LogicalException("出借人往来账户不存在");
            }
            // 锁定入账账户
            T6101 rzzh = null;
            String msg = null;
            int feeCode = 0;
            if (t6230.F15 == T6230_F15.S) {
                rzzh = selectT6101(connection, t6230.F02, SysFundAccountType.WLZH);
                msg = "借款人账户不存在";
                feeCode = InterfaceConst.JK;
            } else {
                rzzh = selectT6101(connection, t6504.F02, SysFundAccountType.SDZH);
                msg = "出借人锁定账户不存在";
                feeCode = InterfaceConst.TZ;
            }
            if (rzzh == null) {
                throw new LogicalException(msg);
            }
            {
                // 使用红包
                // if (null != t6351)
                // {
                // czzh.F06 = czzh.F06.add(t6351.F04);
                // updateT6101(connection, czzh.F06, czzh.F01);
                // //使用了红包的资金流水
                // T6102 t6102s = new T6102();
                // t6102s.F02 = czzh.F01;
                // t6102s.F03 = FeeCode.HBJE;
                // t6102s.F04 = pid;
                // t6102s.F06 = t6351.F04;
                // t6102s.F08 = czzh.F06;
                // t6102s.F09 = String.format("使用红包:%s，标题：%s", t6351.F01,
                // t6230.F03);
                // insertT6102(connection, t6102s);
                // }
                // 扣减出账账户金额
                czzh.F06 = czzh.F06.subtract(t6504.F04);
                if (czzh.F06.compareTo(BigDecimal.ZERO) < 0) {
                    throw new LogicalException("账户金额不足");
                }

                updateT6101(connection, czzh.F06, czzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = czzh.F01;
                t6102.F03 = InterfaceConst.TZ;
                t6102.F04 = rzzh.F01;
                t6102.F07 = t6504.F04;
                t6102.F08 = czzh.F06;
                t6102.F09 = String.format("散标出借:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            {
                // 增加入账账户金额
                rzzh.F06 = rzzh.F06.add(t6504.F04);
                updateT6101(connection, rzzh.F06, rzzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = rzzh.F01;
                t6102.F03 = feeCode;
                t6102.F04 = czzh.F01;
                t6102.F06 = t6504.F04;
                t6102.F08 = rzzh.F06;
                t6102.F09 = String.format("散标出借:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            // 插入投标记录
            T6250 t6250 = new T6250();
            t6250.F02 = t6230.F01;
            t6250.F03 = t6504.F02;
            t6250.F04 = t6504.F04;
            t6250.F01 = t6504.F05;
            // 判断计息金额与标总金额是否一致
            if (t6230.F05.compareTo(t6230.F26) == 0) {
                t6250.F05 = t6504.F04;
            } else {
                t6250.F05 = t6230.F26.multiply(t6504.F04).divide(t6230.F05, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            t6250.F07 = T6250_F07.F;
            // 红包
            // if (null != t6351) {
            // t6250.F13 = t6351.F01;
            // t6250.F14 = t6351.F04;
            // }
            int rid = insertT6250(connection, t6250);
            try (PreparedStatement ps = connection
                    .prepareStatement("UPDATE S65.T6504 SET F05 = ? WHERE F01 = ?")) {
                ps.setInt(1, rid);
                ps.setInt(2, t6504.F01);
                ps.executeUpdate();
            }
            if (t6230.F15 == T6230_F15.S) {
                // 自动放款
                Date currentDate = getCurrentDate(connection);
                T6251 t6251 = new T6251();
                {
                    // 插入债权
                    t6251.F02 = zqCode(rid);
                    t6251.F03 = t6230.F01;
                    t6251.F04 = t6504.F02;
                    t6251.F05 = t6250.F04;
                    t6251.F06 = t6250.F04;
                    t6251.F07 = t6250.F04;
                    t6251.F08 = T6251_F08.F;
                    t6251.F09 = currentDate;
                    t6251.F10 = new Date(currentDate.getTime() + t6230.F19
                            * DateHelper.DAY_IN_MILLISECONDS);
                    t6251.F11 = rid;
                    t6251.F01 = insertT6251(connection, t6251);
                }
                // 生成还款计划
                //   hkjh(connection, t6230, t6251);
                // 收成交服务费
                T6238 t6238 = selectT6238(connection, t6230.F01);
                if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal fwf = t6238.F02.multiply(t6504.F04);
                    // updateT6101(connection, wlzh.F06, wlzh.F01);
                    {
                        // 平台资金增加
                        ptwlzh.F06 = ptwlzh.F06.add(fwf);
                        updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = ptwlzh.F01;
                        t6102.F03 = InterfaceConst.CJFWF;
                        t6102.F04 = rzzh.F01;
                        t6102.F06 = fwf;
                        t6102.F08 = ptwlzh.F06;
                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
                        insertT6102(connection, t6102);
                    }
                    {
                        // 借款人账户减少
                        rzzh.F06 = rzzh.F06.subtract(fwf);
                        updateT6101(connection, rzzh.F06, rzzh.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = rzzh.F01;
                        t6102.F03 = InterfaceConst.CJFWF;
                        t6102.F04 = ptwlzh.F01;
                        t6102.F07 = fwf;
                        t6102.F08 = rzzh.F06;
                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
                        insertT6102(connection, t6102);
                    }
                }
                // 红包使用平台资金减少
                // if (null != t6351) {
                // ptwlzh.F06 = ptwlzh.F06.subtract(t6351.F04);
                // updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                // T6102 t6102s = new T6102();
                // t6102s.F02 = ptwlzh.F01;
                // t6102s.F03 = FeeCode.HBJE;
                // t6102s.F04 = czzh.F01;
                // t6102s.F07 = t6351.F04;
                // t6102s.F08 = ptwlzh.F06;
                // t6102s.F09 = String.format("扣除红包金额:%s，标题：%s", t6351.F01,
                // t6230.F03);
                // insertT6102(connection, t6102s);
                // }
                try (PreparedStatement pstmt = connection
                        .prepareStatement("UPDATE S62.T6231 SET F12 = ? WHERE F01 = ?")) {
                    pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                    pstmt.setInt(2, t6230.F01);
                    pstmt.execute();
                }
                if (t6230.F07.compareTo(BigDecimal.ZERO) <= 0) {// 满标
                    try (PreparedStatement pstmt = connection
                            .prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?")) {
                        pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt = connection
                            .prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?")) {
                        pstmt.setString(1, T6230_F20.HKZ.name());
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                }
            }else{
                if (t6230.F07.compareTo(BigDecimal.ZERO) <= 0) {// 满标
                    try (PreparedStatement pstmt = connection
                            .prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?")) {
                        pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt = connection
                            .prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?")) {
                        pstmt.setString(1, T6230_F20.DFK.name());
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                }
            }
            // 发站内信
            T6110 t6110 = selectT6110(connection, t6504.F02);
//                Envionment envionment = configureProvider.createEnvionment();
//                envionment.set("title", t6230.F03);
//                envionment.set("money", t6504.F04.toString());
//                String content = configureProvider.format(LetterVariable.TZR_TBCG, envionment);
            String content = "尊敬的用户： 您好，您投的借款“"+t6230.F03+"”出借成功，金额为"+t6504.F04.toString()+"元。 感谢您对我们的关注和支持！";
            sendLetter(connection, t6504.F02, "出借成功", content);

                /*String isUseYtx = configureProvider
                        .getProperty(SmsVaribles.SMS_IS_USE_YTX);
                if ("1".equals(isUseYtx)) {
                    SMSUtils smsUtils = new SMSUtils(configureProvider);
                    int type = smsUtils.getTempleId(LetterVariable.TZR_TBCG
                            .getDescription());
                    sendMsg(connection, t6110.F04, t6230.F03, type);
                } else {
                    sendMsg(connection, t6110.F04, content);
                }*/
        }catch (Exception e){
            logger.error(e, e);
            throw e;
        }
    }

    @Override
    protected void doSubmit(Connection connection, int orderId, Map<String, String> params) throws Throwable {
        if (params == null) {
            return;
        }
        String expOrdId = params.get("expOrdId");
        if (!StringHelper.isEmpty(expOrdId))
        {
            try
            {
                // 修改订单状态
                updateSubmit(connection, T6501_F03.DQR, IntegerParser.parse(expOrdId));
            }
            catch (Exception e)
            {
                // 异常处理
                handleError(connection, orderId);
                // 修改订单状态
                updateSubmit(connection, T6501_F03.SB, IntegerParser.parse(expOrdId));
                // 记录日志
                logger.error(e, e);
            }
        }
    }

    private void updateSubmit(Connection connection, T6501_F03 t6501_F03, int F02)
            throws Throwable
    {
        try (PreparedStatement pstmt =
                     connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6501_F03.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }

    private void hkjh(Connection connection, T6230 t6230, T6251 t6251)
            throws Throwable {
        final Date currentDate = getCurrentDate(connection); // 数据库当前日期
        // 起息日
        final Date interestDate = new Date(currentDate.getTime() + t6230.F19 * 86400000);
        Date endDate = new Date(DateHelper.rollMonth(t6230.F22.getTime(), t6230.F09));

        // 首期还款日期
        Date backMoneyDate = null;
        // 总期数
        int totalTerm = 0;
        if (t6230.F17 == T6230_F17.ZRY) {// 自然月
            // 首期还款日期
            backMoneyDate = new Date(t6230.F22.getTime());
            switch (t6230.F10) {
                case DEBX: {
                    // insertT6252s(connection, calZRY_DEBX(connection, t6230,
                    // t6251, interestDate, endDate));
                    //    insertT6252s(connection, calculate_DEBX(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case MYFX: {
                    // insertT6252s(connection, calZRY_MYFX(connection, t6230,
                    // t6251, interestDate, endDate));
                    /*insertT6252s(
                            connection,
                            calculate_MYFX(connection, t6230, t6251, interestDate,
                                    endDate, backMoneyDate, totalTerm));*/
                    break;
                }
                case YCFQ: {
                    // insertT6252s(connection, calYCFQ(connection, t6230, t6251,
                    // interestDate, endDate));
                    insertT6252s(
                            connection,
                            calculate_YCFQ(connection, t6230, t6251, interestDate,
                                    endDate, backMoneyDate, totalTerm));
                    break;
                }
                case DEBJ: {
                    // insertT6252s(connection, calZRY_DEBJ(connection, t6230,
                    // t6251, interestDate, endDate));
                    /*insertT6252s(
                            connection,
                            calculate_DEBJ(connection, t6230, t6251, interestDate,
                                    endDate, backMoneyDate, totalTerm));*/
                    break;
                }
                default:
                    throw new LogicalException("不支持的还款方式");
            }
        }else if (t6230.F17 == T6230_F17.GDR) {// 固定付息日

        } else {
            throw new LogicalException("不支持的付息方式");
        }
    }

    /**
     * 本息到期一次付清
     *
     * @param connection
     * @param t6230
     * @param t6251
     * @param interestDate
     *            起息日
     * @param endDate
     *            结束日
     * @param backMoneyDate
     *            首期还款日
     * @param totalTerm
     * @return
     * @throws Throwable
     */
    private ArrayList<T6252> calculate_YCFQ(Connection connection, T6230 t6230,
                                            T6251 t6251, Date interestDate, Date endDate, Date backMoneyDate,
                                            int totalTerm) throws Throwable {
        // 一年中的天数
        BigDecimal yearDays = new BigDecimal(IntegerParser.parse(InterfaceConst.REPAY_DAYS_OF_YEAR, 360));

        BigDecimal lx = BigDecimal.ZERO;
        BigDecimal bj = BigDecimal.ZERO;
        ArrayList<T6252> t6252s = new ArrayList<>();

        T6231 t6231 = selectT6231(connection, t6230.F01);

        Calendar date = Calendar.getInstance();
        date.setTime(backMoneyDate);
        date.add(Calendar.DAY_OF_MONTH, t6231.F22);
        endDate = new Date(date.getTimeInMillis());

        int days = 0;
        days = (int) Math.floor((endDate.getTime() - interestDate.getTime())
                / DateHelper.DAY_IN_MILLISECONDS);

        lx = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
        lx = lx.multiply(t6230.F06).multiply(new BigDecimal(days))
                .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);

        bj = t6251.F07;

		/*
		 * if (t6231.F21 == T6231_F21.S) {//按天算 Calendar date =
		 * Calendar.getInstance(); date.setTime(backMoneyDate);
		 * date.add(Calendar.DAY_OF_MONTH, t6231.F22); endDate = new
		 * Date(date.getTimeInMillis());
		 *
		 * int days = 0; days = (int)Math.floor((endDate.getTime() -
		 * interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
		 *
		 * lx = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP); lx =
		 * lx.multiply(t6230.F06) .multiply(new BigDecimal(days))
		 * .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
		 *
		 * bj = t6251.F07;
		 *
		 * } else { List<T6252> list = calculate_MYFX(connection, t6230, t6251,
		 * interestDate, endDate, backMoneyDate, totalTerm);
		 *
		 * if (list != null && list.size() > 0) { for (T6252 t6252 : list) { if
		 * (FeeCode.TZ_LX == t6252.F05) { lx = lx.add(t6252.F07); } else if
		 * (FeeCode.TZ_BJ == t6252.F05) { bj = bj.add(t6252.F07); } } } }
		 */

        {
            // 利息
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = InterfaceConst.TZ_LX;
            t6252.F06 = 1;
            t6252.F07 = lx;
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
            System.out.println("date:" + endDate + "===lx:"
                    + t6252.F07.doubleValue());
        }
        {
            // 本金
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = InterfaceConst.TZ_BJ;
            t6252.F06 = 1;
            t6252.F07 = bj;
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
            System.out.println("date:" + endDate + "===bj:"
                    + t6252.F07.doubleValue());
        }
        return t6252s;
    }

    private void insertT6252s(Connection connection, List<T6252> entities)
            throws SQLException {
        try (PreparedStatement pstmt = connection
                .prepareStatement("INSERT INTO S62.T6252 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?")) {
            for (T6252 entity : entities) {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setInt(3, entity.F04);
                pstmt.setInt(4, entity.F05);
                pstmt.setInt(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F07);
                pstmt.setDate(7, entity.F08);
                pstmt.setString(8, entity.F09.name());
                pstmt.setTimestamp(9, entity.F10);
                pstmt.setInt(10, entity.F11);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // 标扩展信息
    protected T6231 selectT6231(Connection connection, int F01)
            throws SQLException {
        T6231 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F21, F22 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6231();
                    record.F21 = T6231_F21.parse(resultSet.getString(1));
                    record.F22 = resultSet.getInt(2);
                }
            }
        }
        return record;
    }

    protected T6504 selectT6504(Connection connection, int F01)
            throws SQLException {
        T6504 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6504 WHERE T6504.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
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

    private T6230 selectT6230(Connection connection, int F01)
            throws SQLException {
        T6230 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F31 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE")) {
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
                    //record.F31 = resultSet.getTimestamp(27);
                }
            }
        }
        return record;
    }

    protected int getPTID(Connection connection) throws Throwable {
        try (PreparedStatement ps = connection
                .prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new LogicalException("平台账号不存在");
                }
            }
        }
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

    private void updateT6501(Connection connection, String F10, int F01)
            throws Throwable {
        try (PreparedStatement ps = connection
                .prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ? WHERE F01 = ?")) {
            ps.setString(1, T6501_F03.CG.name());
            ps.setString(2, F10);
            ps.setInt(3, F01);
            ps.executeUpdate();
        }
    }

    private void updateT6101(Connection connection, BigDecimal F01, int F02)
            throws Throwable {
        try (PreparedStatement pstmt = connection
                .prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?")) {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }

    private int insertT6102(Connection connection, T6102 entity)
            throws Throwable {
        try (PreparedStatement pstmt = connection
                .prepareStatement(
                        "INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setTimestamp(4, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setBigDecimal(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, T6102_F10.WDZ.name());
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    private int insertT6250(Connection connection, T6250 entity)
            throws SQLException {
        try (PreparedStatement pstmt = connection
                .prepareStatement(
                        "INSERT INTO S62.T6250 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = CURRENT_TIMESTAMP(), F07 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setString(5, entity.F07.name());
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    private String zqCode(int recordId) {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        return ("Z" + decimalFormat.format(recordId));
    }

    private int insertT6251(Connection connection, T6251 entity)
            throws SQLException {
        try (PreparedStatement pstmt = connection
                .prepareStatement(
                        "INSERT INTO S62.T6251 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setString(7, entity.F08.name());
            pstmt.setDate(8, entity.F09);
            pstmt.setDate(9, entity.F10);
            pstmt.setInt(10, entity.F11);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    protected T6238 selectT6238(Connection connection, int F01)
            throws SQLException {
        T6238 record = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6238();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                }
            }
        }
        return record;
    }

    /**
     * @Title: insertJoinRecord
     * @Description: 薪金宝插入用户投标记录
     * @param connection
     * @param
     * @return
     * @throws Throwable
     * @return: int
     */
    private int insertJoinRecord(Connection connection, T6141 userInfo, int bidType, int period, BigDecimal investSum, int bidId)
            throws Throwable {
        try (PreparedStatement pstmt = connection
                .prepareStatement(
                        "INSERT INTO FLB.T_JOIN_RECORD SET F_USER_ID = ?, F_USER_NAME = ?, F_TYPE = ?, F_DAY = ?, F_AMOUNT = ?, F_JOIN_TIME = ?, F_BID_ID = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userInfo.F01);
            pstmt.setString(2, userInfo.F02);
            pstmt.setInt(3, bidType);
            pstmt.setInt(4, period);
            pstmt.setBigDecimal(5, investSum);
            pstmt.setTimestamp(6, getCurrentTimestamp(connection));
            pstmt.setInt(7, bidId);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    /**
     * @Title: selectT6141
     * @Description: 获取实名认证的用户userId和姓名
     * @param userId
     * @return
     * @throws Throwable
     * @return: T6141
     */
    public T6141 selectT6141(Connection connection, int userId) throws Throwable {
        T6141 record = null;
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT F01,F02 FROM S61.T6141 WHERE F01= ?  ")) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                record = new T6141();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getString(2);
            }
        }
        return record;
    }
    public Map<String,Object> getUserXjbInvestMonth(Connection connection, int userId, int investDay,String bidTypeCode) throws Throwable{
        Map<String,Object> returnMap = new HashMap<String,Object>();
        int i=0;
        String bidTitle=null;
        try (PreparedStatement pstmt = connection.prepareStatement(
                "select A.bidTitle,count(A.bidTitle) "+
                        "from (select r.id, r.f_user_id userId, r.f_amount monthAmount, r.f_day investDay, "+
                        "r.f_join_time firstJoinTime, DATE_ADD(t.F31, INTERVAL 11 MONTH) planStopTime,t.F22 publishTime,t.F03 bidTitle "+
                        "from flb.t_join_record r left join S62.t6230 t on r.f_bid_id=t.F01 where r.f_user_id =? and r.f_day=? and t.F20 in ('DFK','HKZ','YJQ','YDF') order by r.f_join_time desc limit 1 ) A "+
                        "inner join S62.t6250 z on z.F03 = A.userId inner join S62.t6230 t on z.F02 = t.F01 left join S62.t6211 s on s.F01 = t.F04 "+
                        "where DAYOFMONTH(t.F31)=A.investDay and t.F20 in ('TBZ','DFK','HKZ','YJQ','YDF') and s.F05=? AND z.F06 >= A.publishTime "+
                        "AND z.F06 <= A.planStopTime ")) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, investDay);
            pstmt.setString(3, bidTypeCode);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()) {
                bidTitle=resultSet.getString(1);
                i=resultSet.getInt(2);
            }
        }
        returnMap.put("investMonth", i);
        returnMap.put("bidTitle", bidTitle);
        return returnMap;
    }

    /**
     * 根据红包id 获取红包相关信息
     * @param connection
     * @param hbId
     */
    private UserRedPacketInfo selectHbInfo(Connection connection, int accountId, String hbId) throws SQLException{
        UserRedPacketInfo urpInfo = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT red.id AS hbId,relation.id AS id,red.red_type AS type,red.red_money AS hbBalance,red.invest_money AS conditionBalance,relation.status AS STATUS,relation.valid_time AS TIMESTAMP "
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
                    urpInfo.setId(resultSet.getInt(2));
                    urpInfo.setType(resultSet.getString(3));
                    urpInfo.setHbBalance(resultSet.getBigDecimal(4));
                    urpInfo.setConditionBalance(resultSet.getBigDecimal(5));
                    urpInfo.setStatus(resultSet.getString(6));
                    urpInfo.setTimestamp(resultSet.getTimestamp(7));
                }
            }
        }
        return urpInfo;
    }

    public InverstBidTradeInfo getBidOrderDetail(Connection connection, int orderId) throws Throwable {
        InverstBidTradeInfo inverstBidTradeInfo = null;
        try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01 AS orderId, F02 AS userId, F03 AS bidId, F04 AS money, F05 AS bidRecordId FROM S65.T6504 WHERE T6504.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    inverstBidTradeInfo = new InverstBidTradeInfo();
                    inverstBidTradeInfo.setOrderId(resultSet.getInt("orderId"));
                    inverstBidTradeInfo.setBidId(resultSet.getInt("bidId"));
                    inverstBidTradeInfo.setBidRecordId(resultSet.getInt("bidRecordId"));
                    inverstBidTradeInfo.setMoney(resultSet.getBigDecimal("money"));
                    inverstBidTradeInfo.setUserId(resultSet.getInt("userId"));
                }
            }
        }
        return inverstBidTradeInfo;
    }

    public int getT6211_F01(Connection connection, String code) throws SQLException {
        int f01 = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("select F01 from s62.`t6211` where F05 = ?")) {
            pstmt.setString(1, code);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    f01 = resultSet.getInt("F01");
                }
            }
        }
        return f01;
    }

    @Override
    public void lockAmountForPlan(Connection connection, InvestPlan investPlan, int userId, BigDecimal amount, VersionTypeEnum versionTypeEnum) throws Throwable {
        try {
            SysFundAccountType wlzh = VersionTypeEnum.CG.equals(versionTypeEnum) ? SysFundAccountType.XW_INVESTOR_WLZH : SysFundAccountType.WLZH;
            SysFundAccountType sdzh = VersionTypeEnum.CG.equals(versionTypeEnum) ? SysFundAccountType.XW_INVESTOR_SDZH : SysFundAccountType.SDZH;

            // 锁定出借人资金账户
            T6101 czzh = selectT6101(connection, userId, wlzh);
            if (czzh == null) {
                logger.error("出借人往来账户不存在,userId=[{}]", userId);
                throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);    //出借人往来账户不存在
            }
            // 锁定入账账户
            T6101 rzzh = selectT6101(connection, userId, sdzh);
            int f01 = getT6211_F01(connection, "JH");
            int feeCode = Integer.valueOf(f01 + "" + InterfaceConst.TZ);
            if (rzzh == null) {
                logger.error("出借人锁定账户不存在,userId=[{}]", userId);
                throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);    //出借人锁定账户不存在
            }
            {
                // 扣减出账账户金额
                czzh.F06 = czzh.F06.subtract(amount);
                if (czzh.F06.compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT);    //账户金额不足
                }

                updateT6101(connection, czzh.F06, czzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = czzh.F01;
                t6102.F03 = feeCode;
                t6102.F04 = rzzh.F01;
                t6102.F07 = amount;
                t6102.F08 = czzh.F06;
                t6102.F09 = String.format("组合出借:%s，标题：%s", investPlan.getNumber(), investPlan.getTitle());
                insertT6102(connection, t6102);
            }
            {
                // 增加入账账户金额
                rzzh.F06 = rzzh.F06.add(amount);
                updateT6101(connection, rzzh.F06, rzzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = rzzh.F01;
                t6102.F03 = feeCode;
                t6102.F04 = czzh.F01;
                t6102.F06 = amount;
                t6102.F08 = rzzh.F06;
                t6102.F09 = String.format("组合出借:%s，标题：%s", investPlan.getNumber(), investPlan.getTitle());
                insertT6102(connection, t6102);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }
    }
}
