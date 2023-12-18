package com.hhj.fileproject.mvc.web;

import com.hhj.fileproject.param.LocalParam;
import com.hhj.fileproject.service.LocalStorageService;
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




    @PostMapping("/saveFile")
    public ResponseEntity<?> saveFile(@RequestBody LocalParam param) {
        localStorageService.saveFile(param);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(HttpServletRequest request,HttpServletResponse response) {
        try (InputStream inputStream =localStorageService.getFile()) {
            response.setContentType("application/octet-stream");
            localStorageService.downloadFile(request, response, inputStream);
        } catch (Exception e) {

        }
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/redirect")
    public ResponseEntity<?> redirect(HttpServletRequest request,HttpServletResponse response){
        localStorageService.redirect(request,response);
        return  ResponseEntity.ok("ok");
    }




}
