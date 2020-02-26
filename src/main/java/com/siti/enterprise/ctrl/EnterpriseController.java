package com.siti.enterprise.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.common.ReturnResult;
import com.siti.common.UploadFile.GeneralUploadBiz;
import com.siti.enterprise.biz.EnterpriseBiz;

import com.siti.enterprise.po.EnterpriseInfo;
import com.siti.system.biz.UserBiz;
import com.siti.system.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("enterprise")
public class EnterpriseController {

    @Resource
    EnterpriseBiz enterpriseBiz;

    @Resource
    UserBiz userBiz;

    @Resource
    GeneralUploadBiz generalUploadBiz;

    private static Logger logger = LoggerFactory.getLogger(EnterpriseController.class);

    /**
     * @Param enterpriseName
     */
    @GetMapping("getEnterprise")
    public ReturnResult getEnterprise(Integer page, Integer pageSize, String enterpriseName) {
        try {
            PageInfo<EnterpriseInfo> material = enterpriseBiz.getEnterprise(page, pageSize, enterpriseName);
            return new ReturnResult(1, "查询成功", material);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1, "异常错误");
        }
    }

    /**
     * 企业注册*/
    @PostMapping("insert")
    public ReturnResult insert(@RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            EnterpriseInfo enterprise = enterpriseBiz.insert(enterpriseInfo);
            User user = new User();
            if (enterprise != null) { // 新增user信息
                user.setUserName(enterpriseInfo.getEntName());
                user.setRealName(enterpriseInfo.getContactName());
                user.setRoleCode("manage");
                user.setEmailAddr(enterpriseInfo.getEmailAddress());
                user.setPhoneNum(enterpriseInfo.getTel());
                user.setUserType("manager");
                user.setStatus(1);
                try {
                    userBiz.saveUser("ADD", user);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                return new ReturnResult(1, "添加成功",enterprise);
            } else if (enterprise == null) { // 修改user信息
                try {
                    userBiz.saveUser("UPDATE", user);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                return new ReturnResult(1, "添加成功");
            } else {
                return new ReturnResult(0, "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1, "异常错误");
        }
    }

    @GetMapping("delete")
    public ReturnResult delete(int id) {
        try {
            int flag = enterpriseBiz.delete(id);
            if (flag == 1) {
                return new ReturnResult(1, "删除成功");
            } else {
                return new ReturnResult(0, "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1, "异常错误");
        }
    }

    @PostMapping("update")
    public ReturnResult update(@RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            int flag = enterpriseBiz.update(enterpriseInfo);
            if (flag == 1) {
                return new ReturnResult(1, "修改成功");
            } else {
                return new ReturnResult(0, "修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1, "异常错误");
        }
    }

    // 上传文件
    @PostMapping("uploadFiles")
    public ReturnResult uploadFiles(@RequestParam("files") MultipartFile[] files, String uploadType, HttpSession session) {
        String filePaths = "";
        String folderName = "";
        if (uploadType == "entPic" || "entPic".equals(uploadType)) { // 企业上传logo
            folderName = "logo";
            if (files.length > 1) {
                return new ReturnResult(-1, "只能上传1张图片!");
            }
        } else if (uploadType == "qualiCertificate" || "qualiCertificate".equals(uploadType)) {
            folderName = "quali";
        }
        try {
            for (int i = 0; i < files.length; i++) {
                filePaths += generalUploadBiz.uploadFiles(files[i], folderName).get("fileName");
                if (i > 0 && i != files.length - 1) {
                    filePaths += ";";
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("uploadType", uploadType);
            map.put("paths", filePaths);
            return new ReturnResult(1, "上传成功", map);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ReturnResult(-1, "上传失败");
        }

    }
}
