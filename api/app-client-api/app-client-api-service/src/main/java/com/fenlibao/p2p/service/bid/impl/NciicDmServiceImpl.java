package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.bid.NciicDmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Administrator on 2015/8/26.
 */
@Service
public class NciicDmServiceImpl implements NciicDmService {

    private static final Logger logger= LogManager.getLogger(NciicDmServiceImpl.class);
    /**
     * 实名认证.
     *
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    @Override
    public boolean check(String id, String name)
            throws Exception
    {
        return check(id, name, false);
    }

    /**
     * 实名认证.
     *
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param status
     *            练练支付实名认证状态
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    @Override
    public boolean check(String id, String name, boolean status)
            throws Exception
    {

        logger.info("check() start ");
        /*if (!isValidId(id))
        {
            throw new LogicalException("无效的身份证号");
        }*/
        if (StringHelper.isEmpty(name))
        {
            throw new LogicalException("姓名不能为空");
        }
        name = name.trim();
        if (name.length() > 20)
        {
            throw new LogicalException("姓名最多为20个字符");
        }

        // 判断是否启用实名认证 后台设置，但是我们这里是必须实名认证
        boolean enable = BooleanParser.parse(InterfaceConst.NCIICVARIABLE);
        if (!enable)
        {
            return true;
        }

        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int born = Integer.parseInt(id.substring(6, 10));
        if ((year - born) < 16)
        {
            throw new LogicalException("必须年满16周岁");
        }*/

        Connection connection = DbPoolConnection.getInstance().getConnection();
        try
        {

            if (false)
            {
                try (PreparedStatement pstmt =
                             connection.prepareStatement("SELECT F02 FROM S71.T7122 WHERE F01 = ? AND F03 = 'TG' LIMIT 1"))
                {
                    pstmt.setString(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            return name.equalsIgnoreCase(resultSet.getString(1));
                        }
                    }
                }
            }
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT F03 FROM S71.T7122 WHERE F01 = ? AND F02 = ?"))
            {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return "TG".equalsIgnoreCase(resultSet.getString(1));
                    }
                }
            }
            connection.setAutoCommit(false);
            String compStatusResult = null;
            /* 这里做了连连的验证
            try
            {
                compStatusResult =
                        doCheck(configureProvider.format(LianLianVariable.URL),
                                configureProvider.format(LianLianVariable.KEY),
                                configureProvider.format(LianLianVariable.MERCHID),
                                id,
                                name);
                logger.info("check() compStatusResult: " + compStatusResult);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }*/
            boolean passed = false;
            if (status)
            {
                passed = status;

                try (PreparedStatement pstmt =
                             connection.prepareStatement("INSERT INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?"))
                {
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, passed ? "TG" : "SB");
                    pstmt.execute();
                }

                try (PreparedStatement pstmt =
                             connection.prepareStatement("INSERT INTO S71.T7124 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?"))
                {
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, passed ? "TG" : "SB");
                    pstmt.setInt(4, 1);
                    pstmt.execute();
                }
            }
            connection.commit();// 提交事务
            return passed;
        }
        catch (Exception e)
        {
            connection.rollback();
            throw e;
        }finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    @Override
    public boolean isIdcard(String idCard) throws Exception {
        boolean is = false;
        int uid = 0;
        try(Connection connection = DbPoolConnection.getInstance().getConnection()) {
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT F01 FROM S61.T6141 WHERE T6141.F07 = ?"))
            {
                pstmt.setString(1,StringHelper.encode(idCard));
                try (ResultSet resultSet = pstmt.executeQuery()){
                    if (resultSet.next())
                    {
                        uid = resultSet.getInt(1);
                    }
                }

            }

        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        if(uid > 0){
            return true;
        }else {
            return false;
        }

    }

    public static void main(String[] args) {
        try {
            System.out.println(com.dimeng.util.StringHelper.decode("gga2RW5kMF/fYRiWvjhn24kGESy2oDyEe7XOQYUBFY8="));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
