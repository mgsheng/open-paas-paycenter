package cn.com.open.pay.platform.manager.tools;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 共同基类
 * @author Administrator
 *
 */
public class FatherCommon {

    public String systemSeparator = System.getProperty("file.separator");
    /**
     * Logger
     * 日志
     */
    public static Logger logger = Logger.getLogger(FatherCommon.class);
    /**
     *  classPath �?�?结尾 
     */
    //public String classPath = FatherCommon.class.getClass().getResource("/").getPath();
    /**
     * log4jPath file
     * path
     */
    public String log4jFileName = "log4j.properties";
    /**
     * 读属性文件用
     */
    public Properties props = new Properties();
    
    /**
     * 构�?方法
     */
    public FatherCommon(){
        try {
            String f_path = CommonUtils.getGML();
            PropertyConfigurator.configure(f_path+"/"+  log4jFileName);
        } catch (Exception e) {
            System.out.println("#ERROR# :系统加载log4j.properties配置文件异常，请检查！");
            e.printStackTrace();
        }
    }
}
