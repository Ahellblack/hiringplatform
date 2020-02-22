package com.siti.utils;

/**
 * Created by zhouyuhan on 2018-09-10.
 */

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageBinary {
    static BASE64Encoder encoder = new BASE64Encoder();
    static BASE64Decoder decoder = new BASE64Decoder();
//    public static void main(String[] args) {
//        // System.out.println(getImageBinary(""));
//        //base64StringToImage(getImageBinary(""));
//    }
    public static String getImageBinary(String Imgpath){
        File f = new File(Imgpath);
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void base64StringToImage(String savePath,String base64String){
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 =ImageIO.read(bais);
            File w2 = new File(savePath);//可以是jpg,png,gif格式
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
