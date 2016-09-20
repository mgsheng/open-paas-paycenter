package cn.com.open.openpaas.payservice.app.channel.paymax.sign;

import java.io.InputStream;

public interface Request<T> {
    String getMethod();

    String getRequestUriPath();

    String getRequestQueryString();

    InputStream getRequestBody();

    String getHeaderValue(String name);
}
