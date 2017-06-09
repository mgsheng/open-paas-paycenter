package cn.com.open.pay.order.service.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @author Administrator
 * 模板类DBUtils
 */
public final class DBUtil {
    // 参数定义
    private static String url = ""; // 数据库地址
    private static String username = ""; // 数据库用户名
    private static String password = ""; // 数据库密码

    private DBUtil() {

    }
    // 加载驱动
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载出错!");
        }
    }

    // 获得连接
    public static  Connection getConnection() throws SQLException {
    	 url="jdbc:mysql://10.100.134.76:3306/unionpay?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=utf8";
    	 username="root";
    	 password="root";
//         url = PropertiesTool.getAppPropertieByKey("jdbc.url");
//         username = PropertiesTool.getAppPropertieByKey("jdbc.username");
//         password = PropertiesTool.getAppPropertieByKey("jdbc.password");
        return DriverManager.getConnection(url, username, password);
    }

    // 释放连接
    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close(); // 关闭结果集
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close(); // 关闭Statement
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close(); // 关闭连接
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}