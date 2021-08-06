package com.renren.faceos.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @auther pengjiaxin@faceos.com
 * @description
 * @date 2019/8/15
 */
public class Base64Utils {


    //图片转化成base64字符串
    public static String getImageStr(String path) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String s = null;
        try {
            byte[] b = Files.readAllBytes(Paths.get(path));
            s = Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 功能描述：图像按宽400，高500等比例缩放
     *
     * @param file
     * @return java.lang.String
     * @author pengjiaxin@faceos.com
     * @date 2019/11/18
     */
    public static BufferedImage imageScaleZoom(MultipartFile file) {
        try {
            /*
             * size(width,height) 若图片横比400小，高比500小，不变
             * 若图片横比400小，高比500大，高缩小到400，图片比例不变 若图片横比400大，高比500小，横缩小到400，图片比例不变
             * 若图片横比400大，高比500大，图片按比例缩小，横为200或高为300
             */
            BufferedImage bufferedImage = Thumbnails
                    .of(file.getInputStream())
                    .size(600, 800)
                    .asBufferedImage();
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述：图像等比例缩放
     *
     * @param file
     * @return java.lang.String
     * @author pengjiaxin@faceos.com
     * @date 2019/11/18
     */
    public static BufferedImage imageScaleZoom(MultipartFile file, int width, int height) {
        try {
            /*
             * size(width,height) 若图片横比400小，高比500小，不变
             * 若图片横比400小，高比500大，高缩小到400，图片比例不变 若图片横比400大，高比500小，横缩小到400，图片比例不变
             * 若图片横比400大，高比500大，图片按比例缩小，横为200或高为300
             */
            BufferedImage bufferedImage = Thumbnails
                    .of(file.getInputStream())
                    .size(width, height)
                    .asBufferedImage();
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 功能描述：bufferedImage转base64
     *
     * @param bufferedImage
     * @return java.lang.String
     * @author pengjiaxin@faceos.com
     * @date 2019/11/18
     */
    public static String imageToBase64(BufferedImage bufferedImage) {
        Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoder.encodeToString(byteArrayOutputStream.toByteArray());
    }


    /**
     * 将BufferedImage转换为InputStream
     *
     * @param image
     * @return
     */
    public InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param base64   源图片地址
     * @param fileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片BASE64转码字符串
     */

    public static String compressPicForScale(String base64, long fileSize) {
        Base64.Encoder encoder = Base64.getEncoder();
        float accuracy = 1f;
        try {
            while (base64.length() > fileSize * 1024) {
                /**
                 * Thumbnails.of(InputStream... inputStreams) 从流读入源;
                 * .scale(double scale) 按比例缩放，0~1缩小，1原比例，>1放大;
                 * .scale(doublescaleWidth, double scaleHeight) 长宽各自设置比例，会拉伸;
                 * .outputQuality(double quality) 质量0.0<=quality<=1.0;
                 * .toOutputStream(OutputStream os) 无返回，写入outputStream里;
                 *
                 */
                MultipartFile multipartFile = Base64DecodedMultipartFile.base64ToMultipart(base64);
                if (multipartFile != null) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    Thumbnails.of(inputStream)
                            .scale(accuracy)
                            .outputQuality(accuracy).toOutputStream(outputStream);
                    base64 = encoder.encodeToString(outputStream.toByteArray());
                    accuracy -= 0.1f;
                } else {
                    System.out.println("图片压缩失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("图片压缩失败=======" + e);
            return null;
        }
        return base64;
    }

}
