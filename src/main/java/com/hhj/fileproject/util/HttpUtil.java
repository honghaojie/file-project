package com.hhj.fileproject.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: hong.hj
 * @createDate: 2023/12/18 9:43
 * @description:
 */
@Component
public class HttpUtil {

    /**
     * 在 URL 或 Body 中设置请求参数。
     * 如果是 GET 请求，可以在 URL 中添加查询参数，例如 http://example.com/api?param1=value1&param2=value2。
     * 如果是 POST 请求，可以在 Body 中设置参数，例如 form-data、x-www-form-urlencoded 或 raw JSON
     * @return
     */
    public HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public HttpServletResponse getResponse(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }
}
