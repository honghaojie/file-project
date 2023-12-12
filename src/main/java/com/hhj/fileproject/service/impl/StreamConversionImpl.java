package com.hhj.fileproject.service.impl;

import com.hhj.fileproject.mvc.web.FileController;
import com.hhj.fileproject.service.StreamConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author: hong.hj
 * @createDate: 2023/12/12 11:00
 * @description:
 */
@Service
public class StreamConversionImpl implements StreamConversionService {
    private static final Logger logger = LoggerFactory.getLogger(StreamConversionImpl.class);
    @Override
    public void outFileStream(String uploadTime, InputStream inputStream, HttpServletRequest request, HttpServletResponse response) {
        //开始输出
        ReadableByteChannel inChannel = null;
        WritableByteChannel outChannel = null;
        OutputStream outputStream = null;
        try{
            //输出流
            outputStream = response.getOutputStream();
            //获取链接的文件流
            //获取输入通道
            long time1 = System.currentTimeMillis();
            inChannel = Channels.newChannel(new BufferedInputStream(inputStream));
            long time2 = System.currentTimeMillis();
            logger.info("耗时-："+(time2-time1));
            //获取输出通道
            outChannel = Channels.newChannel(new BufferedOutputStream(outputStream));
            //零拷贝
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            long time3 = System.currentTimeMillis();
            while (-1 != inChannel.read(buffer)){
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }
            long time4 = System.currentTimeMillis();
            logger.info("文件流时间统计 输出浏览器耗时-："+(time4-time3));
        }catch (Exception e){

        }finally {
            try{
                if(outChannel != null){
                    outChannel.close();
                }
            }catch (Exception e){
                logger.error("outChannel 关闭资源失败", e);
            }
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (Exception e) {
                logger.error("outputStream 关闭资源失败", e);
            }
            try {
                if(inChannel != null){
                    inChannel.close();
                }
            } catch (Exception e) {
                logger.error("inChannel 关闭资源失败", e);
            }
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("inputStream 关闭资源失败", e);
            }
        }
    }
}
