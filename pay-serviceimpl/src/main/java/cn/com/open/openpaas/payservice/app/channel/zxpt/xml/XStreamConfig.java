package cn.com.open.openpaas.payservice.app.channel.zxpt.xml;

import java.util.HashMap;
import java.util.Map;

public class XStreamConfig {
	/** 别名. */
    private Map<String, Class<?>> aliasMap              = new HashMap<String, Class<?>>();

    /** 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType. */
    private Map<String, Class<?>> implicitCollectionMap = new HashMap<String, Class<?>>();

    /**
     * 获得 别名.
     *
     * @return the aliasMap
     */
    public Map<String, Class<?>> getAliasMap(){
        return aliasMap;
    }

    /**
     * 设置 别名.
     *
     * @param aliasMap
     *            the aliasMap to set
     */
    public void setAliasMap(Map<String, Class<?>> aliasMap){
        this.aliasMap = aliasMap;
    }

    /**
     * 获得 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType.
     *
     * @return the implicitCollectionMap
     */
    public Map<String, Class<?>> getImplicitCollectionMap(){
        return implicitCollectionMap;
    }

    /**
     * 设置 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType.
     *
     * @param implicitCollectionMap
     *            the implicitCollectionMap to set
     */
    public void setImplicitCollectionMap(Map<String, Class<?>> implicitCollectionMap){
        this.implicitCollectionMap = implicitCollectionMap;
    }
}
