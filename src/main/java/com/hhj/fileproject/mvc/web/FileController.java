package com.hhj.fileproject.mvc.web;

import com.hhj.fileproject.param.LocalParam;
import com.hhj.fileproject.service.LocalStorageService;
import com.hhj.fileproject.service.StreamConversionService;
import com.hhj.fileproject.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author: hong.hj
 * @createDate: 2023/12/11 17:07
 * @description:
 */
@Controller
@RequestMapping({"/api/file"})
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private  LocalStorageService localStorageService;

    @Autowired
    private StreamConversionService streamConversionService;


    @PostMapping("/saveFile")
    public ResponseEntity<?> saveFile(@RequestBody LocalParam param) {
        localStorageService.saveFile(param);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(HttpServletResponse response, HttpServletRequest request) {
        try (InputStream inputStream =localStorageService.getFile()) {
            response.setContentType("application/octet-stream");
            downloadFile(request, response, inputStream);
        } catch (Exception e) {

        }
        return ResponseEntity.ok("ok");

    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response, InputStream inputStream) {
     /*   String contentDisposition = response.getHeader("Content-Disposition");
        if (StringUtils.isNotBlank(contentDisposition)) {
            *//*contentDisposition = CommonUtil.base64Decode(contentDisposition);
            response.setHeader("Content-Disposition", contentDisposition);*//*
            response.setHeader("Content-Disposition", "attachment;filename=test.txt");
        }*/
        response.setHeader("Content-Disposition", "attachment;filename=test.txt");
        streamConversionService.outFileStream("", inputStream, request, response);
    }


}
