package com.fenlibao.p2p.service.withdraw;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;

/**
 * 连连账号支付系统服务基础虚类
 * 
 * 提供支付常量，数据链接
 * @author  huguangze
 * @version  [版本号, 2014年11月26日]
 */
public abstract class AbstractAipgService extends BaseAbstractService {
    
    /**
     * 代付接口地址
     */
    private static String WITHDRAW_APPLY;
    
    /**
     * 代付私钥文
     */
    private static String RSA_P_KEY;
    
    /**
     * 代付公钥钥文
     */
    private static String RSA_PB_KEY;
    
    /**
     * MD5key
     */
    private static  String MD5_KEY;
    /**
     * 代付商户id
     */
    private static String OID_PARTNER;
    
    /**
     * 签名方式 WITHDRAWCHK_URL
     */
    private static String SIGN_TYPE;
    
    /**
     * 代付对账地址（提现）
     */
    private static String WITHDRAWCHK_URL;
    
     /**
     * 代付异步回调地址（提现）
     */
    private static String NOTIFY_URL;
    


    public static String getwithdrawApply()
    {
        WITHDRAW_APPLY = Config.get(Payment.WITHDRAW_APPLY); //configureProvider.getProperty(LianLianPayVariable.WITHDRAW_APPLY);
        return WITHDRAW_APPLY;
    }

    public static String getRsaPKey()
    {
        RSA_P_KEY = Payment.get(Payment.RSA_P_KEY); //configureProvider.getProperty(LianLianPayVariable.RSA_P_KEY);
        return RSA_P_KEY;
    }

    public static String getRsaPBKey()
    {
        RSA_PB_KEY = Payment.get(Payment.RSA_PB_KEY); //configureProvider.getProperty(LianLianPayVariable.RSA_PB_KEY);
        return RSA_PB_KEY;
    }

    public static String getMd5Key(){
        MD5_KEY = Payment.get(Payment.MD5_KEY); //configureProvider.getProperty(LianLianPayVariable.KEY);
        return MD5_KEY;
    }
    
    
    public static String getOidPartner()
    {
        OID_PARTNER = Payment.get(Payment.OID_PARTNER);//configureProvider.getProperty(LianLianPayVariable.OID_PARTNER);
        boolean isTestAccount = BooleanParser.parse(Payment.get(Payment.IS_ACCOUNT_TEST)); 
		if (isTestAccount) { //是否是测试账号
			OID_PARTNER = Payment.get(Payment.OID_PARTNER_TEST);
		}
        return OID_PARTNER;
    }
    
    public static String getSignType()
    {
        SIGN_TYPE = Payment.get(Payment.SIGN_TYPE_WITHDRAW);//configureProvider.getProperty(LianLianPayVariable.SIGN_TYPE);
        return SIGN_TYPE;
    }
    
    
     public static String getWithdrawChkUrl()
    {
         WITHDRAWCHK_URL = Config.get(Payment.ORDER_RESULT_QUERY_URL);//configureProvider.getProperty(LianLianPayVariable.WITHDRAWCHK_URL);
        return WITHDRAWCHK_URL;
    }
    
        public static String getNotifyURL() throws IOException
    {
            NOTIFY_URL = Config.get(Payment.WITHDRAW_NOTIFY_URL);//configureProvider.format(URLVariable.WITHDRAW_NOTIFY_URL);
            
        return NOTIFY_URL;
    }
     
    /**
     * 容联云通讯发送短信方法
     *
     * @param connection
     * @param mobile
     *            手机
     * @param content
     *            内容
     * @param tempId
     *            模板id
     * @throws Throwable
     */
    protected void sendMsg(Connection connection, String mobile,
        String content, int tempId) throws Throwable {
        try {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) {
                long msgId = 0;
                try (PreparedStatement ps = connection
                    .prepareStatement(
                        "INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, tempId);
                    ps.setString(2, content);
                    ps.setTimestamp(3,
                        new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0) {
                    try (PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }
    }

    
    /**
	 * <dt>
	 * <dl>
	 * 描述：写操作日志.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param type
	 *            操作类别
	 * @param log
	 *            日志内容
	 * @throws Throwable
	 */
    protected void writeLog(Connection connection, String type, String log, int userId, String ip)
        throws Throwable
    {
    	try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, userId);
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setString(3, type);
			pstmt.setString(4, log);
			pstmt.setString(5, ip);
			pstmt.execute();
		}
	}
    
    /**
	 * 根据用户ID，获取用户对象
	 * @param connection
	 * @param F01
	 * @return
	 * @throws SQLException
	 */
	protected T6110 selectT6110(Connection connection, int F01)
		throws SQLException {
		T6110 record = null;
		try {
			try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6110();
						record.F02 = resultSet.getString(1);
						record.F03 = resultSet.getString(2);
						record.F04 = resultSet.getString(3);
						record.F05 = resultSet.getString(4);
						record.F06 = T6110_F06.parse(resultSet.getString(5));
						record.F07 = T6110_F07.parse(resultSet.getString(6));
						record.F08 = T6110_F08.parse(resultSet.getString(7));
						record.F09 = resultSet.getTimestamp(8);
						record.F10 = T6110_F10.parse(resultSet.getString(9));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
		return record;
	}
}

