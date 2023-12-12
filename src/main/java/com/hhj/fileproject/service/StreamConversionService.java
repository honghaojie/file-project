package com.hhj.fileproject.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author: hong.hj
 * @createDate: 2023/12/12 10:59
 * @description:
 */
public interface StreamConversionService {
    void outFileStream(String uploadTime, InputStream inputStream, HttpServletRequest request, HttpServletResponse response);
}
