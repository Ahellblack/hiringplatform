package com.siti.common.UploadFile;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用文件上传Ctrl
 */
@RestController
@RequestMapping("uploadFiles")
public class GeneralUploadCtrl {

    @Resource
    private GeneralUploadBiz generalUploadBiz;

    /**
     * 上传 文件
     *
     * @param files
     * @param session
     * @return
     */
    @PostMapping
    public Map<String, Object> uploadImgs(MultipartFile[] files, String imgType, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        imgType = (imgType == null || "".equals(imgType)) ? "bikeImg" : imgType;
        for (int i = 0; i < files.length; i++) {
            list.add(generalUploadBiz.uploadImgs(files[i]));
        }
        map.put("status", 0);
        map.put("data", list);
        return map;
    }

    /**
     * 删除 清理文件
     *
     * @param fileAliasName
     */
    @DeleteMapping
    public void delete(String fileAliasName, String imgType) {
        imgType = (imgType == null || "".equals(imgType)) ? "bikeImg" : imgType;
        generalUploadBiz.deleteFile(fileAliasName);
    }


}
