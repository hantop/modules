package com.fenlibao.p2p.controller.global;

import com.fenlibao.p2p.common.util.database.DruidInstanceEnum;
import com.fenlibao.p2p.common.util.database.DruidManager;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.controller.noversion.BaseController;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.EnumService;
import com.fenlibao.p2p.util.api.redis.RedisFactory;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@RequestMapping("testapi")
public class TestController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TestController.class);

	@Resource
	private EnumService enumService;

    @Resource
    private SqlSession sqlSession;
    
	/**
	 * 测试接口
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("test")
	public HttpResponse testapi(HttpServletRequest request) {
		HttpResponse result = new HttpResponse();
		try {
			if ("1".equals("2")) {
				throw new RuntimeException();
			}
			Map<String, String> params = RequestUtil.getAllParameters(request);
			System.out.println(request.toString());
			System.out.println(params);
			Map<String, Object> map = new HashMap<>();
            map.put("params", params);
			result.setData(map);
		} catch (Exception e) {
			result.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[EnumController.testapi]", e);
		}
		return result;
	}

	/**
	 * 测试json参数
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "json", method = {RequestMethod.POST}, headers = {"Content-Type=application/json"})
    HttpResponse testJson(HttpServletRequest request) {
		HttpResponse response = new HttpResponse();
		Map<String, String> map = RequestUtil.getAllParameters(request);
        for (Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
		try {
			InputStream in = request.getInputStream();
			byte[] bytes = new byte[request.getContentLength()];
            int read = in.read(bytes);
            System.out.println(read);
            String str = new String(bytes, request.getCharacterEncoding());
			System.out.println(str);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[TestController.testJson]", e);
		}
		return response;
	}

	/**
	 * redis内存数据测试
	 *
	 * @return
	 */
	@RequestMapping("redis")
	public HttpResponse testRedis() {
		HttpResponse response = new HttpResponse();
		try (Jedis jedis = RedisFactory.getResource()) {
			String key = "aaa";
			String value = jedis.get("aaa");
			Map<String, Object> map = new HashMap<>();
			map.put(key, value);
			response.setData(map);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[TestController.testRedis]", e);
		}
		return response;
	}

    /**
     * 测试多数据源
     * @return
     */
	@RequestMapping("testMultiDataSource")
    HttpResponse testMultiDataSource() {
		return enumService.testMultiDataSource();
	}

    /**
     * 测试multipart request
     * @param request
     * @param req
     * @return
     */
    @RequestMapping(value = "request/multipart", method = RequestMethod.POST)
    HttpResponse testMultipartRequest(
            HttpServletRequest request,
            MultipartRequest req) {
        System.out.println("test multipart request");
        System.out.println(RequestUtil.getAllParameters(request));
        Map<String, MultipartFile> map = req.getFileMap();
        for (String o : map.keySet()) {
            MultipartFile f = map.get(o);
            System.out.println(f.getOriginalFilename());
            System.out.println(f.getName());
            System.out.println(f.getSize());
            System.out.println(f.getContentType());
        }
        return new HttpResponse();
    }

    /**
     * 测试spring multipart request
     * @param request
     * @param name
     * @param file
     * @return
     */
    @RequestMapping(value = "request/multipart/morethanone", method = RequestMethod.POST)
    HttpResponse testMultipartRequestMoreThanOne(
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "file", required = false) MultipartFile[] file) {
        System.out.println(name);
        System.out.println("test multipart request more than one");
        System.out.println(RequestUtil.getAllParameters(request));
        for (MultipartFile f : file) {
            System.out.println(f.getContentType());
            System.out.println(f.getOriginalFilename());
            System.out.println(f.getName());
            System.out.println(f.getSize());
        }
        return new HttpResponse();
    }

    /**
     * 测试事务
     * @return
     */
    @RequestMapping("test/transaction")
    HttpResponse testTransaction() {
        HttpResponse response = new HttpResponse();
        int result = enumService.testTransaction();
        System.out.println(result);
        return response;
    }

    /**
     * 测试druid获取连接
     * @return
     * @throws SQLException
     */
    @RequestMapping("test/druid/connection")
    HttpResponse testDruidConnection() throws SQLException {
        HttpResponse response = new HttpResponse();
        DruidManager manager = DruidInstanceEnum.INSTANCE.getInstance();
        Connection connection = DruidManager.getConnection();
        System.out.println(connection);
        PreparedStatement statement = connection.prepareStatement("select * from flb.t_enum");
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            System.out.println(set.getString("enum_value"));
        }
        manager.closeResource(connection, statement, set);

        test();

//        DruidManager manager = DruidInstanceEnum.INSTANCE.getInstance();
        Connection connection1 = DruidManager.getConnection();
        System.out.println("conn1: " + connection1);
        PreparedStatement statement1 = connection1.prepareStatement("select * from flb.t_enum");
        ResultSet set1 = statement1.executeQuery();
        while (set1.next()) {
            System.out.println(set1.getString("enum_value"));
        }
        manager.closeResource(connection1, statement1, set1);
        return response;
    }

    /**
     * 测试druid获取连接
     * @throws SQLException
     */
    private static void test() throws SQLException {
        DruidManager manager = DruidInstanceEnum.INSTANCE.getInstance();
        Connection connection1 = DruidManager.getConnection();
        System.out.println("mid: " + connection1);
        PreparedStatement statement1 = connection1.prepareStatement("select * from flb.t_enum");
        ResultSet set1 = statement1.executeQuery();
        while (set1.next()) {
            System.out.println(set1.getString("enum_value"));
        }
        manager.closeResource(connection1, statement1, set1);
    }

    /**
     * 测试spring rest api版本控制
     * @param request
     * @return
     */
    @RequestMapping(value = "rest/version/api")
    public HttpResponse testVersionRestfulApi(HttpServletRequest request) {
        System.out.println("testapi/rest/version/api : no version");
        HttpResponse response = new HttpResponse();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        return response;
    }

    /**
     * 测试spring rest api版本控制
     * @param request
     * @return
     */
    @RequestMapping(value = "rest/version/api", headers = APIVersion.V_1_0_0)
    public HttpResponse testVersionRestfulApiV101(HttpServletRequest request) {
        System.out.println("testapi/rest/version/api : 1.0.0");
        HttpResponse response = new HttpResponse();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        System.out.println("hello");
        return response;
    }

    /**
     * 测试spring rest api版本控制
     * @param request
     * @return
     */
    @RequestMapping(value = "rest/version/api", headers = "version=1.0.1")
    public HttpResponse testVersionRestfulApiV102(HttpServletRequest request) {
        System.out.println("testapi/rest/version/api : 1.0.1");
        HttpResponse response = new HttpResponse();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        return response;
    }

    /**
     * 测试通过sqlSession获取连接
     * @param request
     * @return
     */
    @RequestMapping("database/sqlsession/connection")
    public HttpResponse testSqlSessionConnection(HttpServletRequest request) {
        HttpResponse response = new HttpResponse();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        try (Connection connection = getConnection()) {
            System.out.println("00000000000000000000");
            System.out.println(connection);
            System.out.println("00000000000000000000");
            try (PreparedStatement ps = connection.prepareStatement("SELECT enum_table, enum_column, enum_key, enum_value FROM t_enum")) {
                try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        System.out.println(result.getString(1));
                        System.out.println(result.getString(2));
                        System.out.println(result.getString(3));
                        System.out.println(result.getString(4));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 测试mybatis的sqlsession获取connection
     * @return
     */
    @RequestMapping("sql/session")
    public HttpResponse testSqlSession() {
        HttpResponse response = new HttpResponse();
        Connection connection = sqlSession.getConnection();
        System.out.println(connection);
        return response;
    }

    /**
     * 获取枚举
     *
     * @param enumTable 枚举表名
     * @param enumColumn 枚举字段名
     * @return json
     */
    @RequestMapping(value = "enum", method = RequestMethod.GET)
    HttpResponse getEnum(
            @RequestParam(required = false, value = "enumTable") String enumTable,
            @RequestParam(required = false, value = "enumColumn") String enumColumn) {
        HttpResponse response = new HttpResponse();
        try {
            response.getData().put("enums", enumService.getEnum(enumTable, enumColumn));
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("[EnumController.getEnum]", e);
        }
        return response;
    }

    @RequestMapping("check/token")
    HttpResponse checkToken(
            @RequestParam(required = true, value = "token") String token) {
        HttpResponse response = new HttpResponse();
        String tokenKey = RedisConst.$LOGIN_STATE.concat(token);
        try (Jedis jedis = RedisFactory.getResource()) {
            List<String> list = jedis.hmget(tokenKey, "token");
            if (list != null && !list.isEmpty()) {
                String redisToken = list.get(0);
                if (StringUtils.isNotBlank(redisToken) && redisToken.equals(token)) {
                    response.setCodeMessage("hehe", "token有效");
                } else {
                    response.setCodeMessage("caca", "token无效");
                }
            }
        }
        return response;
    }

}
