package com.siti.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by hongtu on 2018/9/10.
 */
public final class ImageUtil {

    public static void main() throws Exception {
        //创建图片文件(此处为1024×768px的图片)和处理后的图片文件
        File fromPic = new File("picture/测试图片1024px-768px.jpg");
        File toPic = new File("picture/结果图片.jpg");
        File waterPic = new File("picture/水印图片.jpg");//作为水印的图片
        //按指定大小把图片进行缩和放（会遵循原图高宽比例）
        //此处把图片压成400×500的缩略图
        Thumbnails.of(fromPic).size(400, 500).toFile(toPic);//变为400*300,遵循原图比例缩或放到400*某个高度
        //按照比例进行缩小和放大
        Thumbnails.of(fromPic).scale(0.2f).toFile(toPic);//按比例缩小
        Thumbnails.of(fromPic).scale(2f);//按比例放大
        //不按比例，就按指定的大小进行缩放
        Thumbnails.of(fromPic).size(100, 100).keepAspectRatio(false).toFile(toPic);
        //或者Thumbnails.of(fromPic).forceSize(100,100).toFile(toPic);
        //旋转图片，rotate(角度),正数则为顺时针，负数则为逆时针
        Thumbnails.of(fromPic).size(200, 200).rotate(90).toFile(toPic);
        //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
        Thumbnails.of(fromPic).scale(1f).outputQuality(0.25f).toFile(toPic);
        //给图片加水印，watermark(位置，水印图，透明度)Positions.CENTER表示加在中间
        Thumbnails.of(fromPic).size(400, 400).watermark(Positions.CENTER, ImageIO.read(waterPic), 0.5f).outputQuality(0.8f).toFile(toPic);

        //用sourceRegion()实现图片裁剪
        //图片中心300*300的区域,Positions.CENTER表示中心，还有许多其他位置可选
        Thumbnails.of(fromPic).sourceRegion(Positions.CENTER, 300, 300).size(300, 300).toFile(toPic);

        //图片中上区域300*300的区域
        Thumbnails.of(fromPic).sourceRegion(Positions.TOP_CENTER, 300, 300).size(300, 300).toFile(toPic);

        Thumbnails.of(fromPic).sourceRegion(0, 0, 200, 200).size(300, 300).toFile(toPic);

        //用outputFormat(图像格式)转换图片格式，保持原尺寸不变
        Thumbnails.of(fromPic).scale(1f).outputFormat("png").toFile("picture/png格式的图片.png");

        //输出成文件流OutputStream
        OutputStream os = new FileOutputStream(toPic);
        Thumbnails.of(fromPic).size(120, 120).toOutputStream(os);

        //输出BufferedImage,asBufferedImage()返回BufferedImage
        BufferedImage bi = Thumbnails.of(fromPic).size(120, 120).asBufferedImage();
        ImageIO.write(bi, "jpg", toPic);

        //压缩至指定图片尺寸（例如：横400高300），保持图片不变形，多余部分裁剪掉(这个是引的网友的代码)
        BufferedImage image = ImageIO.read(fromPic);
        Thumbnails.Builder<BufferedImage> builder = null;

        int imageWidth = image.getWidth();
        int imageHeitht = image.getHeight();
        if ((float) 300 / 400 != (float) imageWidth / imageHeitht) {
            if (imageWidth > imageHeitht) {
                image = Thumbnails.of(fromPic).height(300).asBufferedImage();
            } else {
                image = Thumbnails.of(fromPic).width(400).asBufferedImage();
            }
            builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 400, 300).size(400, 300);
        } else {
            builder = Thumbnails.of(image).size(400, 300);
        }
        builder.outputFormat("jpg").toFile(toPic);
    }


}
