package com.hhj.fileproject.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author: hong.hj
 * @createDate: 2023/12/18 10:01
 * @description:
 */
public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);


    /**
     * 设置请求头
     *
     * @param response
     * @param fileSize
     * @param fileName
     * @param contentType
     * @param uploadTime
     * @param userAgent
     */
    public static void setResponseHeader(HttpServletResponse response, Long fileSize, String fileName, String contentType, String uploadTime, String userAgent) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Last-Modified", uploadTime);
        //30分钟
        response.setHeader("Cache-Control", "public,max-age=" + 30 * 60);
        response.setHeader("Content-Encoding", "identity");
        response.setHeader("Content-Length", String.valueOf(fileSize));
        response.setHeader("Content-Disposition", setGenerateDownloadContentDisposition(fileName, userAgent));
        if (fileName.toLowerCase().endsWith(".mp4")) {
            response.setHeader("Accept-Ranges", "bytes");
        }
        Consumer<HttpServletResponse> responseConsumer = setDownloadContentType(contentType, fileName, userAgent);
        responseConsumer.accept(response);
    }


    /**
     * 重定向到错误页面
     *
     * @param message 需要展示的错误信息
     */
    public static void redirectError(HttpServletResponse response, String message) {
        try {
            if (StringUtils.isBlank(message)) {
                return;
            }
            message = URLEncoder.encode(message, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
            //String url = redirectError("/hhj/errorpage?type=error&label=");+message;
            ResponseUtil.redirectUrl(response, "https://www.baidu.com");
        } catch (Exception e2) {
            logger.error("重定向失败", e2);
        }
    }

    /**
     * 文件下载重定向
     *
     * @param response 响应
     * @param url      重定向的下载地址
     * @param fileName 文件名
     */
    public static void redirectUrl(HttpServletResponse response, String url, String fileName) {
        if (url == null) {
            return;
        }
        try {
            if (StringUtils.isNotBlank(fileName)) {
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20").replaceAll("%2B", "\\+");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            }
            response.sendRedirect(url);
        } catch (Exception e) {
            logger.error("下载重定向失败", e);
            throw new RuntimeException("下载失败");
        }
    }

    public static void redirectUrl(HttpServletResponse response, String url) {
        redirectUrl(response, url, "");
    }


    /**
     * 文件流重定向到缓存
     *
     * @param response   响应
     * @param request    请求
     * @param uploadTime 上传时间
     * @return true 定向到缓存，false 不定向到缓存
     */
    public static boolean redirectCache(HttpServletResponse response, HttpServletRequest request, String uploadTime) {
        String lastModified = request.getHeader("If-Modified-Since");
        if ((lastModified != null) && lastModified.equals(uploadTime)) {
            logger.info("---------> 返回浏览器中的缓存 lastModified={}", lastModified);
            response.setStatus(HttpStatus.SC_NOT_MODIFIED);
            return true;
        }
        return false;
    }


    public static String setGenerateDownloadContentDisposition(String fileName, String userAgent) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "未命名文件.txt";
        }
        if (StringUtils.isBlank(userAgent)) {
            userAgent = "userAgent";
        }
        String contentDisposition;
        userAgent = userAgent.toLowerCase();
        try {
            if (userAgent.toLowerCase().contains("firefox")) {
                //火狐浏览器
                contentDisposition = "attachment;filename*=utf-8''" + URLEncoder.encode(new String(fileName.getBytes(), StandardCharsets.UTF_8), "UTF-8").replaceAll("\\+", "%20");
            } else if (userAgent.contains("edge")) {
                //微软浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                contentDisposition = "attachment;filename=\"" + fileName + "\"";
            } else if (userAgent.contains("msie") || userAgent.contains("like Gecko")) {
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                contentDisposition = "attachment;filename=\"" + fileName + "\"";
            } else if ((userAgent.contains("applewebkit")) && (userAgent.contains("chrome"))) {
                //360 兼容模式/极速模式 ，谷歌浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                contentDisposition = "attachment;filename=" + fileName;
            } else if ((userAgent.contains("safari")) && (!userAgent.contains("chrome"))) {
                //苹果 safari 浏览器
                contentDisposition = "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            } else if (userAgent.contains("mozilla") && !userAgent.contains("trident")) {
                //苹果企业微信
                if (userAgent.contains("wechat") && userAgent.contains("mac ")) {
                    contentDisposition = "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                } else {
                    //其他
                    fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                    contentDisposition = "attachment;filename=\"" + fileName + "\"";
                }
            } else if (userAgent.contains("trident") && userAgent.contains("rv:11")) {
                //ie 11 版本浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                contentDisposition = "attachment;filename=\"" + fileName + "\"";
            } else {
                fileName = commonEncode(fileName);
                contentDisposition = "attachment;filename=\"" + fileName + "\"";
            }

        } catch (Exception e) {
            logger.error("文件名转码失败", e);
            throw new RuntimeException("文件名转码失败");
        }
        return contentDisposition;
    }

    public static String commonEncode(String param) {
        try {
            param = Optional.ofNullable(param).orElse("");
            param = URLEncoder.encode(param, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~")
                    .replace("-", "%2D");
        } catch (UnsupportedEncodingException ex) {
            logger.info("\n>>>>>>>>>>>>>>>>>> common encode failed : ", ex);
        }
        return param;
    }


    public static Consumer<HttpServletResponse> setDownloadContentType(String originContentType, String fileName, String userAgent) {
        String contentType;
        if (StringUtils.isBlank(fileName)) {
            fileName = "未命名文件.txt";
        }
        if (StringUtils.isBlank(userAgent)) {
            userAgent = "userAgent";
        }

        userAgent = userAgent.toLowerCase();
        String fileExt = FilenameUtils.getExtension(fileName);
        fileExt = fileExt.toLowerCase();
        if ((userAgent.contains("iphone") || userAgent.contains("ipad"))
                && userAgent.contains("dingtalk")
                && (fileExt.equals("pdf")) || fileExt.equals("ofd")) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
        } else if (fileExt.endsWith("mp4")) {
            contentType = "video/mp4";
        } else if (StringUtils.isNotBlank(originContentType) && !originContentType.equals("null")) {
            contentType = originContentType;
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return response -> {
            Optional.ofNullable(response).ifPresent(tempResponse -> {
                isBlank(contentType).ifPresent(tempResponse::setContentType);
            });
        };

    }

    public static Optional<String> isBlank(String str) {
        return StringUtils.isEmpty(str)
                ? Optional.empty()
                : Optional.of(str);
    }
}
