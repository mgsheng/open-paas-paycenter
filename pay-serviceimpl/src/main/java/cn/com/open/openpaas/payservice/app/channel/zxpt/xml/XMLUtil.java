package cn.com.open.openpaas.payservice.app.channel.zxpt.xml;

import java.util.Map;

import javax.xml.validation.Validator;

import com.thoughtworks.xstream.XStream;

public class XMLUtil {
	/**
     * 将xml字符串转为相对应的对象
     * 
     * @param xml
     * @return
     */
    public static <T> T fromXML(String xml){
        return fromXML(xml, null);
    }

    public static <T> T fromXML(String xml,XStreamConfig xStreamConfig){
        XStream xstream = constructXStream(xStreamConfig);
        return (T) xstream.fromXML(xml);
    }

    public static XStream constructXStream(XStreamConfig xStreamConfig){
        XStream xstream = new XStream();

        if (xStreamConfig!=null){

            // *******************alias********************************************
            Map<String, Class<?>> aliasMap = xStreamConfig.getAliasMap();
            if (!aliasMap.isEmpty()){
                for (Map.Entry<String, Class<?>> entry : aliasMap.entrySet()){
                    String key = entry.getKey();
                    Class<?> value = entry.getValue();
                    // 类重命名
                    xstream.alias(key, value);
                }
            }
            // *******************implicitCollectionMap********************************************
            Map<String, Class<?>> implicitCollectionMap = xStreamConfig.getImplicitCollectionMap();
            if (!implicitCollectionMap.isEmpty()){
                for (Map.Entry<String, Class<?>> entry : implicitCollectionMap.entrySet()){
                    String key = entry.getKey();
                    Class<?> value = entry.getValue();
                    xstream.addImplicitCollection(value, key);
                }
            }
        }
		return xstream;
    }
}

