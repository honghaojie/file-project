package com.hhj.fileproject.service;

import com.hhj.fileproject.param.LocalParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author: hong.hj
 * @createDate: 2023/12/11 17:10
 * @description:
 */
public interface LocalStorageService {

    String saveFile(LocalParam localParam);

    InputStream getFile();

    void downloadFile(HttpServletRequest request, HttpServletResponse response, InputStream inputStream);

    void redirect(HttpServletRequest request, HttpServletResponse response);
}
