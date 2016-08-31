package cn.com.open.pay.order.service.tools;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 数据库操作类
 * 
 * @see
 */
public class DBHelp extends FatherCommon {
    // file path*/
    private String url = null;
    private String username = null;
    private String password = null;

    // private Properties props = new Properties();
    /**
     * 构造方法
     */
    public DBHelp(String dbkey) {
        // 加载读取DB属性文件
        try {
            InputStream iStream = CommonUtils.getInputStream("jdbc.properties");
            props.load(iStream);
        } catch (IOException e1) {
            logger.error("#ERROR# :系统加载sysconfig.properties配置文件异常，请检查！"+dbkey, e1);
        }
        // }
        url = (props.getProperty("jdbc.url"+"."+dbkey));
        username = (props.getProperty("jdbc.username"+"."+dbkey));
        password = (props.getProperty("jdbc.password"+"."+dbkey));
        // 注册驱动类
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("#ERROR# :加载数据库驱动异常，请检查！"+dbkey, e);
        }
    }
    /**
     * 构造方法
     */
    public DBHelp(String dbkey,String driver) {
        // 加载读取DB属性文件
        try {
            InputStream iStream = CommonUtils.getInputStream("jdbc.properties");
            props.load(iStream);
        } catch (IOException e1) {
            logger.error("#ERROR# :系统加载sysconfig.properties配置文件异常，请检查！"+dbkey, e1);
        }
        // }
        url = (props.getProperty("jdbc.url"+"."+dbkey));
        username = (props.getProperty("jdbc.username"+"."+dbkey));
        password = (props.getProperty("jdbc.password"+"."+dbkey));
        // 注册驱动类
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("#ERROR# :加载数据库驱动异常，请检查！"+dbkey, e);
        }
    }
    /**
     * 构造方法
     */
    public DBHelp() {
        String dbkey = "one";
        // 加载读取DB属性文件
        try {
            InputStream iStream = CommonUtils.getInputStream("jdbc.properties");
            props.load(iStream);
        } catch (IOException e1) {
            logger.error("#ERROR# :系统加载sysconfig.properties配置文件异常，请检查！"+dbkey, e1);
        }
        // }
        url = (props.getProperty("jdbc.url"+"."+dbkey));
        username = (props.getProperty("jdbc.username"+"."+dbkey));
        password = (props.getProperty("jdbc.password"+"."+dbkey));
        // 注册驱动类
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("#ERROR# :加载数据库驱动异常，请检查！"+dbkey, e);
        }
    }

    /**
     * 创建一个数据库连接
     * 
     * @return 一个数据库连接
     */
    public Connection getConnection() {
        Connection conn = null;
        // 创建数据库连接
        try {
            // logger.debug("url=" + url + ";username=" + username +
            // ";password=" + password);
            conn = DriverManager.getConnection(url, username, password);
            // logger.debug("创建数据库连接!");
        } catch (SQLException e) {
            logger.error("#ERROR# :创建数据库连接发生异常，请检查！", e);
        }
        return conn;
    }

    /**
     * 在一个数据库连接上执行一个SQL语句查询
     * 
     * @param conn
     *            数据库连接
     * @param staticSql
     *            静态SQL语句字符串
     * @return 返回查询结果集ResultSet对象
     */
    public ResultSet executeQuery(Connection conn, String staticSql) {
        ResultSet rs = null;
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            // logger.debug("executeQuery : " + staticSql);
            // 执行SQL，并获取返回结果
            rs = stmt.executeQuery(staticSql);
        } catch (SQLException e) {
            logger.error("#ERROR# :executeQuery执行SQL语句出错，请检查！\n" + staticSql, e);
            return null;
        }
        return rs;
    }

    /**
     * 在一个数据库连接上执行一个SQL语句查询
     * 
     * @param conn
     *            数据库连接
     * @param staticSql
     *            静态SQL语句字符串
     * @return 返回查询结果集ResultSet对象
     */
    public ResultSet executeQuery(Connection conn, String staticSql, String[] strValues) {
        ResultSet rs = null;
        try {
            // 创建执行SQL的对象
            // logger.debug("DBHelp--executeQuery--PreparedStatement--SQL-- : "
            // + staticSql);
            PreparedStatement ps = conn.prepareStatement(staticSql);
            // 执行SQL，并获取返回结果
            if (strValues != null) {
                int sqlCount = 1;
                for (String strValue : strValues) {
                    // logger.debug("DBHelp--executeQuery--PreparedStatement--strValue-- : "
                    // + sqlCount + ":" + strValue);
                    ps.setString(sqlCount, strValue);
                    sqlCount++;
                }
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            logger.error("#ERROR# :executeQuery执行SQL语句出错，请检查！\n" + staticSql, e);
            return null;
        }
        return rs;
    }

    /**
     * 在一个数据库连接上执行一个静态SQL语句
     * 
     * @param conn
     *            数据库连接
     * @param staticSql
     *            静态SQL语句字符串
     * @return 返回结果，true,false
     */
    public boolean executeSQL(Connection conn, String staticSql) {
        boolean b = false;
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            // logger.debug("executeSQL : " + staticSql);
            // 执行SQL，并获取返回结果
            b = stmt.execute(staticSql);
        } catch (SQLException e) {
            logger.error("#ERROR# :executeSQL执行SQL语句出错，请检查！\n" + staticSql, e);
            return false;
        }
        return b;
    }

    /**
     * 在一个数据库连接上执行一个更新SQL语句（插入、更新、删除）
     * 
     * @param conn
     *            数据库连接
     * @param staticSql
     *            静态SQL语句字符串
     * @return 返回响应记录数
     */
    public int executeUpdate(Connection conn, String updateSql) {
        int iCount = 0;
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            // logger.debug("executeUpdate : " + updateSql);
            // 执行SQL，并获取返回结果
            iCount = stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            logger.error("#ERROR# :executeUpdate执行SQL语句出错，请检查！\n" + updateSql, e);
            return -99;
        }
        return iCount;
    }

    /**
     * 在一个数据库连接上执行一批静态SQL语句
     * 
     * @param conn
     *            数据库连接
     * @param sqlList
     *            静态SQL语句字符串集合
     */
    public int[] executeBatchSQL(Connection conn, List<String> sqlList) {
        int[] ri;
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            for (String sql : sqlList) {
                // logger.info("executeBatchSQL : " + sql);
                stmt.addBatch(sql);
            }
            // 执行SQL，并获取返回结果
            ri = stmt.executeBatch();
        } catch (SQLException e) {
            logger.error("#ERROR# :executeBatchSQL执行批量SQL语句出错，请检查！", e);
            ri = new int[] { -99 };
           
        }
        return ri;
    }

    /**
     * 取得strSQL对应记录数
     * 
     * @param strSQL
     * @param strValue
     * @return
     */
    public int getCountBySQL(Connection conn, String strSQL, String[] strValue) {
        int iCount = 0;
        try {
            // 拼接Count SQL
            String strSQlCount = "select count(1) as cou" + " from " + " (" + strSQL + ")";
            // logger.debug("DBHelp--getCountBySQL--#debug# :拼接Count SQL=" +
            // strSQlCount);

            // 执行检索Count SQL
            ResultSet rsC = executeQuery(conn, strSQlCount, strValue);
            if (rsC == null) {
                logger.error("DBHelp--getCountBySQL--#debug# :执行检索Count SQL 异常：");
                return -1;
            }
            while (rsC.next()) {
                iCount = Integer.valueOf(rsC.getString("cou")).intValue();
            }

            // logger.debug("DBHelp--getCountBySQL--关闭数据连接");
        } catch (SQLException e) {
            logger.error("#ERROR# :DBHelp---getCountBySQL执行SQL 取得Count", e);
            return -99;
        }
        return iCount;
    }

    /**
     * 关闭数据连接
     * 
     * @param conn
     */
    public void closeConnection(Connection conn) {
        try {
            if (conn == null) {
                return;
            }
            if (!conn.isClosed()) {
                // 关闭数据库连接
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("#ERROR# :关闭数据库连接发生异常，请检查！", e);
        }
    }

}
