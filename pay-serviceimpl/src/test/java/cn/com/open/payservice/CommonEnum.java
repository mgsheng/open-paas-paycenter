package cn.com.open.payservice;

/**
 * 项目名称 : project.
 * 创建日期 : 2017/8/29 17:47.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/8/29][17:47]创建文件 by JACKIE.
 */
public enum CommonEnum {
    APP_ID("aa98545f11cb49418f18a2ea9ed9873c"),
    APP_KEY("a014d6d5ca534a9eb90126c9a326d6a9"),
    APP_SECRET("945fa18c666a4e0097809f6727bc6997"),

    ;

    String code="";
    String display="";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    CommonEnum(String display) {
        this(display,"");
    }

    CommonEnum(String display, String code) {
        this.display = display;
        this.code = code;
    }
}
