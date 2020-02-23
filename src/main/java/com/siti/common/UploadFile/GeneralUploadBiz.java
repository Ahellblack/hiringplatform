package com.siti.common.UploadFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.siti.common.constant.ConstantPath;
import com.siti.common.exception.MyException;
import com.siti.config.YmlConfig;
import com.siti.utils.FileCopy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quyue1205 on 2018/8/31.
 * file
 */
@Service
public class GeneralUploadBiz {

    private static final Logger logger = LoggerFactory.getLogger(GeneralUploadBiz.class);

    @Resource
    private YmlConfig ymlConfig;

    /**
     * 单张图片上传
     *
     * @param imgs
     */
    public Map<String, Object> uploadImgs(MultipartFile imgs) {//单张图片上传
        Map<String, Object> map = new HashMap<>();
        String uploadPath;
        /*String path = ConstantPath.imagesPath;*/
        uploadPath = ConstantPath.imagesPath;

        File folder = new File(uploadPath);
        if (!folder.exists()) {
            Boolean isCreate = folder.mkdirs();
            if (!isCreate) {
                map.put("status", -1);
                map.put("data", "文件夹创建出错，请重试！");
                return map;
            }
        }
        if (imgs == null) {
            return null;
        }
        String newFileName = System.currentTimeMillis() + (int) (Math.random() * 900) + 100 + ".jpg";
        try {
            imgs.transferTo(new File(uploadPath + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File fromPic = new File(uploadPath + newFileName);
        /*FileCopy.imgMini(path, uploadPath, newFileName, fromPic);
        FileCopy.imgSquare(path, uploadPath, newFileName, fromPic);*/
        map.put("fileName", newFileName);
        /*map.put("filePath", path + newFileName + ".400x400.jpg");*/
        return map;
    }

    public String[][] uploadFiles(MultipartFile[] files, String uploadPath) {
        String[][] fileNames = new String[files.length][2];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = file.getOriginalFilename();
            String fileSize = file.getSize() / (1024.0 * 1024 * 1024) > 1 ? (file.getSize() / (1024.0 * 1024 * 1024) + "GB") : (file.getSize() / (1024.0 * 1024) > 1 ? (file.getSize() / (1024.0 * 1024) + "MB") : (file.getSize() / (1024.0) + "KB"));
            System.out.println("file size: " + fileSize);
            int pointIndex = fileName.lastIndexOf('.');
            long time = System.currentTimeMillis();
            int random = (int) (Math.random() * 900) + 100;
            String newFileName = time + random + "." + fileName.substring(pointIndex + 1);
            try {
                file.transferTo(new File(uploadPath + newFileName));
                fileNames[i][0] = fileName.substring(0, pointIndex) + time + "." + fileName.substring(pointIndex + 1);    //文件名后+时间戳作为版本号
                fileNames[i][1] = newFileName;
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return fileNames;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String fileName) {
        String uploadPath = ConstantPath.imagesPath;

        File file = new File(uploadPath + fileName);
        return file.delete();
    }

    /**
     * 文件下载
     */
    public Map<String, Object> downloadFile(String filePath, String fileName, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (fileName == null || "".equals(fileName.trim())) {
                throw new MyException("参数错误，请联系开发人员！");
            }
            URL url = this.getClass().getClassLoader().getResource("");
            if (url == null) {
                throw new MyException("获取路径异常，请重试！");
            }
            response.setCharacterEncoding("UTF-8");
            //第一步：设置响应类型
            response.setContentType("application/force-download");//应用程序强制下载
            //第二读取文件
            InputStream in = new FileInputStream(filePath + fileName);
            //设置响应头，对文件进行url编码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(in.available());
            //第三步：老套路，开始copy
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            map.put("message", e.getLocalizedMessage());
            map.put("status", -1);
        }
        return map;
    }

    /**
     * 文件上传
     *
     * @param imgs
     * @param folderName 文件标识
     */
    public Map<String, Object> uploadFiles(MultipartFile imgs, String folderName) {
        Map<String, Object> map = new HashMap<>();
        String uploadPath = ConstantPath.imagesPath;
        /*String path = ConstantPath.imagesPath;*/
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            Boolean isCreate = folder.mkdirs();
            if (!isCreate) {
                map.put("status", -1);
                map.put("data", "文件夹创建出错，请重试！");
                return map;
            }
        }
        if (imgs == null) {
            return null;
        }
        String newFileName = folderName + "_" + System.currentTimeMillis() + (int) (Math.random() * 900) + 100 + ".jpg";
        try {
            imgs.transferTo(new File(uploadPath + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File fromPic = new File(uploadPath + newFileName);
        /*FileCopy.imgMini(path, uploadPath, newFileName, fromPic);
        FileCopy.imgSquare(path, uploadPath, newFileName, fromPic);*/
        map.put("fileName", newFileName);
        /*map.put("filePath", path + newFileName + ".400x400.jpg");*/
        return map;
    }
}
