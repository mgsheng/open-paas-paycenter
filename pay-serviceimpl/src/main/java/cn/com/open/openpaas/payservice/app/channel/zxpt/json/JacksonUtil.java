package cn.com.open.openpaas.payservice.app.channel.zxpt.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtil
{
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static XmlMapper xmlMapper = new XmlMapper();
  
  public static String obj2json(Object obj)
    throws Exception
  {
    return objectMapper.writeValueAsString(obj);
  }
  
  public static <T> T json2pojo(String jsonStr, Class<T> clazz)
    throws Exception
  {
    return objectMapper.readValue(jsonStr, clazz);
  }
  
  public static <T> Map<String, Object> json2map(String jsonStr)
    throws Exception
  {
    return (Map)objectMapper.readValue(jsonStr, Map.class);
  }
  
  public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
    throws Exception
  {
    Map<String, Map<String, Object>> map = (Map)objectMapper.readValue(jsonStr, new TypeReference() {});
    Map<String, T> result = new HashMap();
    for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
      result.put(entry.getKey(), map2pojo((Map)entry.getValue(), clazz));
    }
    return result;
  }
  
  public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
    throws Exception
  {
    List<Map<String, Object>> list = (List)objectMapper.readValue(jsonArrayStr, new TypeReference() {});
    List<T> result = new ArrayList();
    for (Map<String, Object> map : list) {
      result.add(map2pojo(map, clazz));
    }
    return result;
  }
  
  public static <T> T map2pojo(Map map, Class<T> clazz)
  {
    return objectMapper.convertValue(map, clazz);
  }
  
  public static String json2xml(String jsonStr)
    throws Exception
  {
    JsonNode root = objectMapper.readTree(jsonStr);
    String xml = xmlMapper.writeValueAsString(root);
    return xml;
  }
  
  public static String xml2json(String xml)
    throws Exception
  {
    StringWriter w = new StringWriter();
    JsonParser jp = xmlMapper.getFactory().createParser(xml);
    JsonGenerator jg = objectMapper.getFactory().createGenerator(w);
    while (jp.nextToken() != null) {
      jg.copyCurrentEvent(jp);
    }
    jp.close();
    jg.close();
    return w.toString();
  }
}
