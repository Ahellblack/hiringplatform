package com.siti.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import com.siti.system.ctrl.LoginCtrl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ht on 2017/12/18.
 */
public class FileCopy {

    public static List<String> setFilePath(String path, String[] fileNames) {
        List<String> URLs = new ArrayList<>();
        if (fileNames != null) {
            for (String string : fileNames) {
                if (!"".equals(string.trim())) {
                    URLs.add(path + string + ".400x400.jpg");
                }
            }
        }
        return URLs;
    }
    /**
     * 文件的复制（修改文件名）
     *
     * @param uploadPath 目标文件地址
     * @param filePath   原始文件地址
     * @param fileName   原始文件名
     */
    public static String fileCopy(String uploadPath, String filePath, String fileName) {
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            Boolean isCreate = folder.mkdirs();
            if (!isCreate) {
                throw new RuntimeException("文件夹创建出错，请重试！");
            }
        }
        String overrideFileName = LoginCtrl.getLoginUserInfo().getId() + fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
        InputStream input = null;
        OutputStream output = null;
        int length;
        // 创建输入输出流对象
        try {
            input = new FileInputStream(filePath + fileName);
            output = new FileOutputStream(new File(uploadPath + overrideFileName));
            // 获取文件长度
            length = input.available();
            // 创建缓存区域
            byte[] buffer = new byte[length];
            // 将文件中的数据写入缓存数组
            input.read(buffer);
            // 将缓存数组中的数据输出到文件
            output.write(buffer);
            return overrideFileName;
        } catch (IOException e) {
            throw new RuntimeException("文件未找到！");
        } finally {
            if (input != null && output != null) {
                try {
                    input.close(); // 关闭流
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件的复制（修不改文件名）
     *
     * @param uploadPath 目标文件地址
     * @param filePath   原始文件地址
     * @param fileName   原始文件名
     */
    public static String fileCopyAPP(String uploadPath, String filePath, String fileName) {
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            Boolean isCreate = folder.mkdirs();
            if (!isCreate) {
                return "";
            }
        }
        InputStream input = null;
        OutputStream output = null;
        int length;
        // 创建输入输出流对象
        try {
            input = new FileInputStream(filePath + fileName);
            output = new FileOutputStream(new File(uploadPath + fileName));
            // 获取文件长度
            length = input.available();
            // 创建缓存区域
            byte[] buffer = new byte[length];
            // 将文件中的数据写入缓存数组
            input.read(buffer);
            // 将缓存数组中的数据输出到文件
            output.write(buffer);
            return fileName;
        } catch (IOException e) {
            return "";
        } finally {
            if (input != null && output != null) {
                try {
                    input.close(); // 关闭流
                    output.close();
                } catch (IOException e) {
                    return "";
                }
            }
        }
    }


    //压缩至指定图片尺寸（例如：横400高400），保持图片不变形，多余部分裁剪掉(这个是引的网友的代码)
    public static String imgMini(String path, String originPath, String fileName, File fromPic) {
        try {
            //File fromPic = new File(originPath + fileName);
            BufferedImage image = ImageIO.read(fromPic);
            Thumbnails.Builder<BufferedImage> builder;

            int imageWidth = image.getWidth();
            int imageHeitht = image.getHeight();
            if ((float) 400 / 400 != (float) imageWidth / imageHeitht) {
                if (imageWidth > imageHeitht) {
                    image = Thumbnails.of(fromPic).height(400).asBufferedImage();
                } else {
                    image = Thumbnails.of(fromPic).width(400).asBufferedImage();
                }
                builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 400, 400).size(400, 400);
            } else {
                builder = Thumbnails.of(image).size(400, 400);
            }
            builder.outputFormat("jpg").toFile(originPath + fileName + ".400x400.jpg");
            return path + fileName + ".400x400.jpg";
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(12 * 1000L);
                System.out.println("thread sleep 15s.");
            } catch (Exception ex) {
                System.out.println("thread sleep 15s error.");
            }
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    //压缩至指定图片尺寸，保持图片不变形，多余部分裁剪掉(这个是引的网友的代码)
    public static String imgSquare(String path, String originPath, String fileName, File fromPic) {
        try {
            //File fromPic = new File(originPath + fileName);
            BufferedImage image = ImageIO.read(fromPic);
            if (image == null) return null;
            Thumbnails.Builder<BufferedImage> builder;

            int imageWidth = image.getWidth();
            int imageHeitht = image.getHeight();

            if ((float) 100 / 100 != (float) imageWidth / imageHeitht) {
                if (imageWidth > imageHeitht) {
                    image = Thumbnails.of(fromPic).height(imageHeitht).asBufferedImage();
                    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, imageHeitht, imageHeitht).size(imageHeitht, imageHeitht);
                } else {
                    image = Thumbnails.of(fromPic).width(imageWidth).asBufferedImage();
                    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, imageWidth, imageWidth).size(imageWidth, imageWidth);
                }
            } else {
                builder = Thumbnails.of(image).size(imageWidth, imageHeitht);
            }
            builder.outputFormat("jpg").outputQuality(0.25f).toFile(originPath + fileName + ".square.jpg");
            return path + fileName + ".square.jpg";
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(12 * 1000L);
                System.out.println("thread sleep 15s.");
            } catch (Exception ex) {
                System.out.println("thread sleep 15s error.");
            }
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

}
