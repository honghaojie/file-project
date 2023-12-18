package com.hhj.fileproject.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: hong.hj
 * @createDate: 2023/12/18 15:54
 * @description:
 */
@Component
public class AddWaterMarkUtil {

    // 水印字体
    private static final Font FONT = new Font("微软雅黑", Font.PLAIN, 24);

    // 透明度
    private static final AlphaComposite COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);

    // 水印之间的间隔
    private static final int X_MOVE = 150;

    // 水印之间的间隔
    private static final int Y_MOVE = 200;

    public static void addWatermark(File input, File out, String text, int fontSize) {
        // 读取原图片
        BufferedImage image = null;
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取图片的宽度和高度
        int width = image.getWidth();
        int height = image.getHeight();

        // 创建一个图片缓存对象
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图片的画笔
        Graphics2D g = newImage.createGraphics();
        // 将原图片绘制到缓存图片上
        g.drawImage(image, 0, 0, width, height, null);

        // 创建字体对象
        Font font = new Font("微软雅黑", Font.BOLD, fontSize);
        // 创建字体渲染上下文
        FontRenderContext frc = new FontRenderContext(null, true, true);
        // 计算字符串的宽度和高度
        Rectangle2D bounds = font.getStringBounds(text, frc);
        // 字符宽度
        int strWidth = (int) bounds.getWidth();
        // 字符高度
        int strHeight = (int) bounds.getHeight();

        // 设置水印的字体样式
        g.setFont(font);
        // 设置水印的颜色
        g.setColor(Color.red);
        // 设置水印的位置 根据需要再自行调整宽度、高度
        g.drawString(text, width - strWidth - 10, height - strHeight + 15);
        // 释放图形上下文使用的系统资源
        g.dispose();

        // 保存带水印的图片
        try {
            ImageIO.write(newImage, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
/*        使用ImageIO类加载需要添加水印的图片

        创建一个BufferedImage的自定义图像对象，并使用Graphics2D对象将原始图像绘制到该对象上

        创建字体对象，并设置字体、颜色、透明度等属性

        使用Graphics2D对象的drawString()方法在需要添加水印的位置绘制字符串
                使用ImageIO类将修改后的图像保存到指定目录*/

/*        File input = new File("D:\\测试水印\\测试水印.png");
        File out = new File("D:\\测试水印\\watermark.png");
        // 水印文本内容,中文转Unicode
        String text = "\u6dfb\u52a0\u6c34\u5370";
        addWatermark(input, out, text, 20);*/

       /* File input = new File("D:\\测试水印\\测试水印.png");
        File out = new File("D:\\测试水印\\watermark.png");
        File watermarkImage = new File("D:\\测试水印\\icon.png");
        addWatermark(input, out, watermarkImage);*/

        String inputFile = "D:\\测试水印\\测试水印.png";
        // 输出图片路径
        String outputFile = "D:\\测试水印\\watermark1.png";
        // 水印文本内容,中文转Unicode
        String watermarkText = "\u6dfb\u52a0\u6c34\u5370";
        markWithContent(inputFile, FONT, Color.darkGray, watermarkText, outputFile);
    }



    public static void addWatermark(File input, File out, File watermarkImage) {
        // 读取添加水印的图片
        BufferedImage image = null;
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取图片的宽度和高度
        int width = image.getWidth();
        int height = image.getHeight();

        // 创建一个图片缓存对象
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图片的画笔
        Graphics2D g = newImage.createGraphics();
        // 将原图片绘制到缓存图片上
        g.drawImage(image, 0, 0, width, height, null);

        // 读取水印图片
        BufferedImage watermark = null;
        try {
            watermark = ImageIO.read(watermarkImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取水印图片的宽度和高度
        int wmWidth = watermark.getWidth();
        int wmHeight = watermark.getHeight();

        // 设置水印图片的透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        // 绘制水印图片
        g.drawImage(watermark, width - wmWidth - 10, height - wmHeight - 10, wmWidth, wmHeight, null);
        // 释放图形上下文使用的系统资源
        g.dispose();

        // 保存带水印的图片
        try {
            ImageIO.write(newImage, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void markWithContent(String inputImgPath, Font font, Color markContentColor,
                                       String waterMarkContent,
                                       String outImgPath) throws IOException {

        // 读取原图片信息
        File srcFile = new File(inputImgPath);
        File outFile = new File(outImgPath);
        BufferedImage srcImg = ImageIO.read(srcFile);

        // 图片宽、高
        int imgWidth = srcImg.getWidth();
        int imgHeight = srcImg.getHeight();

        // 图片缓存
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        // 创建绘图工具
        Graphics2D graphics = bufImg.createGraphics();

        // 画入原始图像
        graphics.drawImage(srcImg, 0, 0, imgWidth, imgHeight, null);

        // 设置水印颜色
        graphics.setColor(markContentColor);

        // 设置水印透明度
        graphics.setComposite(COMPOSITE);

        // 设置倾斜角度
        graphics.rotate(Math.toRadians(-35), (double) bufImg.getWidth() / 2,
                (double) bufImg.getHeight() / 2);

        // 设置水印字体
        graphics.setFont(font);

        // 消除java.awt.Font字体的锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xCoordinate = -imgWidth / 2, yCoordinate;
        // 字体长度
        int markWidth = FONT.getSize() * getTextLength(waterMarkContent);
        // 字体高度
        int markHeight = FONT.getSize();

        // 循环添加水印
        while (xCoordinate < imgWidth * 1.5) {
            yCoordinate = -imgHeight / 2;
            while (yCoordinate < imgHeight * 1.5) {
                graphics.drawString(waterMarkContent, xCoordinate, yCoordinate);
                yCoordinate += markHeight + Y_MOVE;
            }
            xCoordinate += markWidth + X_MOVE;
        }
       /* while (xCoordinate < imgWidth ) {
            yCoordinate = -imgHeight / 2;
            while (yCoordinate < imgHeight) {
                graphics.drawString(waterMarkContent, xCoordinate, yCoordinate);
                yCoordinate += markHeight + Y_MOVE;
            }
            xCoordinate += markWidth + X_MOVE;
        }*/

        // 释放画图工具
        graphics.dispose();

        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            // 输出图片
            ImageIO.write(bufImg, "jpg", fos);
            fos.flush();
        }
    }


    /**
     * 计算水印文本长度
     * 中文长度即文本长度
     * 英文长度为文本长度二分之一
     */
    public static int getTextLength(String text) {
        //水印文字长度
        int length = text.length();

        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }




}
