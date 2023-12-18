package com.hhj.fileproject.service.impl;

import com.hhj.fileproject.constant.FileConstant;
import com.hhj.fileproject.param.LocalParam;
import com.hhj.fileproject.service.LocalStorageService;
import com.hhj.fileproject.service.StreamConversionService;
import com.hhj.fileproject.util.HttpUtil;
import com.hhj.fileproject.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author: hong.hj
 * @createDate: 2023/12/11 17:10
 * @description:
 */
@Service
public class LocalStorageServiceImpl implements LocalStorageService {
    private static final Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);
    
    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private StreamConversionService streamConversionService;
    
    @Override
    public String saveFile(LocalParam localParam) {
            /*    URL resource = this.getClass().getClassLoader().getResource("../../");
        String filePath = resource.getPath() + "file/test.txt";*/
        File localFile = new File(FileConstant.FILE_PATH+"\\test.txt");
        Path oriPath = Paths.get(FileConstant.FILE_PATH);
        Path targetFilePath = Optional.of(oriPath)
                .map(this::createDirectory)
                .filter(Files::exists)
                .map(path -> path.resolve("tt"))
                .orElseThrow(() -> new RuntimeException("target file is not exist"));

        try {
            Files.deleteIfExists(targetFilePath);
        } catch (Exception e) {
            logger.error("delete fail",e);
        }

        FileSystemResource fileSystemResource = new FileSystemResource(localFile);
        try (InputStream inputStream = fileSystemResource.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             OutputStream outputStream = Files.newOutputStream(targetFilePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            int len;
            byte[] bytes = new byte[4096];
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
        } catch (Exception e) {
            logger.error("part write stream failed : ",e);
        };
        return null;
    }

    @Override
    public InputStream getFile() {
        Path filePath = Paths.get(FileConstant.FILE_PATH+"\\test.txt");
        try {
            return Files.newInputStream(filePath);
        } catch (NoSuchFileException ne) {
            logger.error("no such file",ne);
        } catch (Exception e) {
            logger.error("download file failed",e);
        }
       return null;
    }

    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, InputStream inputStream) {
     /*   String contentDisposition = response.getHeader("Content-Disposition");
        if (StringUtils.isNotBlank(contentDispositioZn)) {
            *//*contentDisposition = CommonUtil.base64Decode(contentDisposition);
            response.setHeader("Content-Disposition", contentDisposition);*//*
            response.setHeader("Content-Disposition", "attachment;filename=test.txt");
        }*/
        response.setHeader("Content-Disposition", "attachment;filename=test.txt");
        streamConversionService.outFileStream("", inputStream, request, response);
    }

    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest request1 = httpUtil.getRequest();
        String type = request.getParameter("type");
        ResponseUtil.redirectUrl(response,"https://www.baidu.com");

    }

    private Path createDirectory(Path path) {
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
