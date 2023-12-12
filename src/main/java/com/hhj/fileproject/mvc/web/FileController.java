package com.hhj.fileproject.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: hong.hj
 * @createDate: 2023/12/11 17:07
 * @description:
 */
@Controller
@RequestMapping({"/api/file"})
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @PostMapping("/create")
    public ResponseEntity<?> saveStorageInstance(@RequestBody LocalParam param) {
        logger.info("aa");
        return ResponseEntity.ok("ok");
    }
}
