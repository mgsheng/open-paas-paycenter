package cn.com.open.pay.platform.manager.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 共通工具类
 * 
 * @see
 */
public class CommonUtils  extends FatherCommon{
    /**
     * Logger
     * 日志
     */
    public static Logger logger = Logger.getLogger(CommonUtils.class);
    /***
     * 取得属性文件中属性值
     * 
     * @param propertiesFile
     *            属性文件相对路径,以src为根目录
     * @param key
     *            属性
     * @return String 属性值
     */
    public static String getProValueByKey(String propertiesFile, String key) throws IOException {
        Properties prop = new Properties();
        String proValue = "";
        InputStream iStream;
        iStream = CommonUtils.class.getResourceAsStream(propertiesFile);
        prop.load(iStream);
        proValue = prop.getProperty(key);
        iStream.close();
        return proValue;
    }

    /***
     * 设置属性文件中属性值
     * 
     * @param propertiesFile
     *            属性文件相对路径,以src为根目录
     * @param key
     *            属性key
     * @param value
     *            属性值
     * @return String 属性值
     */
    public static Object setProValueByKey(String propertiesFile, String key, String value) throws IOException {
        Properties prop = new Properties();
        InputStream iStream;
        iStream = CommonUtils.class.getResourceAsStream(propertiesFile);
        prop.load(iStream);
        OutputStream os = new FileOutputStream(CommonUtils.getGML() + propertiesFile);
        Object obj = prop.setProperty(key, value);
        // 将Properties集合保存到流中
        prop.store(os, "");
        os.flush();
        os.close();
        iStream.close();
        return 1;
    }

    /***
     * 取得属性文件中属性值
     * 
     * @param pathFile
     *            属性文件
     * @param key
     *            属性
     * @return String 属性值
     */
    public static String getProValueByKey2(String pathFile, String key) throws IOException {
        Properties prop = new Properties();
        String proValue = "";

        InputStream iStream = new BufferedInputStream(new FileInputStream(pathFile));
        prop.load(iStream);
        proValue = prop.getProperty(key);
        iStream.close();
        return proValue;
    }

    /**
     * 返回根目录下的文件的InputStream
     */
    public static InputStream getInputStream(String filename) {
        try {
            String f_path = CommonUtils.getGML();
            return new BufferedInputStream(new FileInputStream(f_path + "/" + filename));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回根目录下的文件的InputStream
     */
    public static String getGML() {
        try {
            String path = URLDecoder.decode(CommonUtils.class.getProtectionDomain().getCodeSource().getLocation()
                    .getFile(), "UTF-8");
            String f_path = path;
            File f = new File(f_path);
            if (f.isFile()) {
                f_path = path.substring(path.indexOf(":") - 2, path.lastIndexOf("/"));
            }
            return f_path;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Object to String
     * 
     * @param obj
     * @return String
     */
    public static String obj2String(Object obj) throws Exception {
        String str = "";
        if (obj != null && obj.toString().length() > 0) {
            str = obj.toString();
        }
        return str;
    }

    /***
     * String to Integer
     * 
     * @param str
     * @return Integer
     */
    public static int string2Intger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return -1;
        }
    }

    /***
     * String to Integer
     * 
     * @param str
     * @return Integer
     */
    public static Long string2Long(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            return (long) -1;
        }
    }

    /****
     * 时间 转String
     * 
     * @return 字符串
     */
    public static String dateToString(Date date, String strFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
            String sysDatetime = sdf.format(date);
            return sysDatetime;
        } catch (Exception ex) {
            return "";
        }
    }

    /****
     * String 转 日期
     * 
     * @return 日期
     */
    public static Date stringToDate(String date, String strFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
            return sdf.parse(date);
        } catch (Exception ex) {
            return null;
        }
    }

    /****
     * String 毫秒转 日期字符串
     * 
     * @return 日期
     */
    public static String hsToDateString(String hs, String strFormat) {
        try {
            return CommonUtils.dateToString(new Date(Long.valueOf(hs)), strFormat);
        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * 日期加法 天
     * 
     * @param str
     * @return Integer
     */
    public static Date dateAddDay(Date date, Integer day) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);// 让日期加1
            return calendar.getTime();
        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * 日期加法 月
     * 
     * @param str
     * @return Integer
     */
    public static Date dateAddMonth(Date date, Integer month) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONDAY) + month);// 让月加1
            return calendar.getTime();
        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * Object to String
     * 
     * @param obj
     * @return String
     */
    public static boolean isNull(Object obj) {
        if (obj != null && obj.toString().length() > 0) {
            return false;
        }
        return true;
    }

    /***
     * 转码 UTF-8
     * 
     * @param str
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String string2UTF8(String str) throws UnsupportedEncodingException {
        if (!ValidateUtil.isNullString(str)) {
            return new String(str.getBytes(), "UTF-8");
        }
        return str;
    }

    /***
     * Timestamp To String 转换异常返回空字符串
     * 
     * @param ts
     * @return String
     */
    public static String Timestamp2String(Timestamp ts, String sdfStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
        String str = "";
        try {
            str = sdf.format(ts);
            return str;
        } catch (Exception ex) {
            return "";
        }
    }

    /***
     * String To Timestamp 转换异常返回null
     * 
     * @param ts
     * @return String
     */
    public static Timestamp String2Timestamp(String date, String sdfStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
        try {
            return new Timestamp(sdf.parse(date).getTime());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解决Oracle中in的参数列表长度超过1000的问题
     * 
     * @param length
     *            每个分段的长度，
     * @param paramsList
     *            待拆分的参数列表
     * @return 拆分后的分段列表
     */
    public static <T> List<List<T>> splitInParams(int length, List<T> paramsList) {
        if (length < 1 || paramsList == null || paramsList.size() == 0)
            return null;
        int size = paramsList.size();
        List<List<T>> list = new ArrayList<List<T>>();
        int d = (int) Math.ceil(size / (length + 0.0));
        for (int i = 0; i < d; i++) {
            int fromIndex = length * i;
            int toIndex = Math.min(fromIndex + length, size);
            list.add(paramsList.subList(fromIndex, toIndex));
        }
        return list;
    }

    /**
     * 解决Oracle中in的参数列表长度超过1000的问题 返回拼接的SQL语句 eg: 返回的SQL语句片段：XX IN (.......) OR XX IN (......)这样的形式
     * 
     * @param paramName
     *            需要进行in查询的查询参数的名称
     * @param length
     *            每个分段的长度，对于Oracle，一般设置为800-1000
     * @param paramsList
     *            待拆分的参数列表
     * @return 拼接的SQL语句片段
     */
    public static <T> String getSQLInParamsSplit(String paramName, int length, List<T> paramsList) {
        if (length < 1 || paramName == null || paramsList == null || paramsList.size() == 0)
            return null;
        List<List<T>> list = splitInParams(length, paramsList);
        StringBuilder sb = new StringBuilder();
        String temp = list.get(0).toString();
        // 由于List的toString方法返回的是[....]形式，需要去掉开头和结尾的中括号
        sb.append(paramName).append(" IN (" + temp.subSequence(1, temp.length() - 1) + ") ");
        int size = list.size();
        for (int i = 1; i < size; i++) {
            temp = list.get(i).toString();
            sb.append(" OR " + paramName + " IN (" + temp.subSequence(1, temp.length() - 1) + ") ");
        }
        return sb.toString();
    }

}
