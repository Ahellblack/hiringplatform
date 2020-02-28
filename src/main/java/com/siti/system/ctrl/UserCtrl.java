package com.siti.system.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.common.BaseCtrl;
import com.siti.common.ReturnResult;
import com.siti.system.biz.UserBiz;
import com.siti.system.po.User;
import com.siti.utils.BASE64Util;
import com.siti.utils.ReturnMessage;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2018/7/23.
 */
@RestController
@RequestMapping("user")
public class UserCtrl extends BaseCtrl<UserBiz, User> {

    /**
     * 查询用户信息
     *
     * @param page
     * @param pageSize
     * @param userName
     * @return
     */
    @GetMapping("getUser")
    public PageInfo<User> getUser(int page, int pageSize, String userName) {
        return biz.getUser(page, pageSize, userName);
    }
    

    /**
     * 查询用户信息
     *
     * @return
     */
    @GetMapping("user")
    public User getLoginUser() {
        return biz.getLoginUser();
    }

    /**
     * 保存用户/添加用户
     * 后台设置默认密码
     *
     * @param user
     * @return
     */
    /*@PostMapping("saveUser")
    public Map<String, Object> saveUser(@RequestBody User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            biz.saveUser("ADD", user);
            return ReturnMessage.addSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }*/

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PutMapping("updateUser")
    public Map<String, Object> updateUser(User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            biz.saveUser("UPDATE", user);
            return ReturnMessage.saveSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getMessage());
        }
        return map;
    }


    /**
     * 冻结用户
     *
     * @param id
     * @return
     */
    @PutMapping("freezeUser/{id}")
    public String updateFreezeUser(@PathVariable Integer id) {
        biz.freezeUser(id);
        return "success";
    }

    /**
     * 解冻用户
     *
     * @param id
     * @return
     */
    @PutMapping("unFreezeUser/{id}")
    public String updateUnFreezeUser(@PathVariable Integer id) {
        biz.unFreezeUser(id);
        return "success";
    }

    /**
     * 注销用户
     *
     * @param id
     * @return
     */
    @PutMapping("cancelUser/{id}")
    public String updateCancelUser(@PathVariable Integer id) {
        biz.cancelUser(id);
        return "success";
    }
    

    /**
     * 密码验证
     *
     * @param oldPassword
     * @return
     */
    @GetMapping("checkPwd/{oldPassword}")
    public boolean checkPwd(@PathVariable String oldPassword) {
        int userId = LoginCtrl.getLoginUserInfo().getId();
        String logPwd = BASE64Util.decode(oldPassword);
        if (logPwd == null) {
            return false;
        }
        return biz.checkPwd(userId, logPwd);
    }

    /**
     * 修改密码
     *
     * @return success：修改成功
     **/
    @PutMapping("pwd/{password}")
    public ReturnResult updateModifyPwd(@PathVariable String password) {
        Integer id = LoginCtrl.getLoginUserInfo().getId();
        if (id == null || password == null || "".equals(password.trim())) {
            return new ReturnResult(-1, "修改失败，参数缺失");
        }
        String logPwd = BASE64Util.decode(password);
        if (logPwd == null) {
            return new ReturnResult(-1, "修改失败，密码不合规");
        }
        biz.modifyPwd(id, logPwd, id);
        return new ReturnResult(1, "修改成功");
    }

    /**
     * 重置密码
     *
     * @return success：修改成功
     **/
    @PutMapping("resetPwd/{userName}")
    public String updateResetPwd(@PathVariable String userName) {
        if (userName == null || "".equals(userName.trim())) {
            return "fail";
        }
        biz.resetPwd(userName);
        return "success";
    }

    /**
     * 发送邮箱验证码，找回密码
     * 账号+邮箱地址验证用户合法性
     */
    @PostMapping("getEmailCheckCode")
    public Map<String, Object> getEmailCheckCode(String emailAddr, String userName) {
        try {
            biz.getEmailCheckCode(emailAddr, userName);
            return ReturnMessage.successMessage("验证码已发送至您的邮箱，请查收！");
        } catch (Exception e) {
            return ReturnMessage.failMessage(e.getLocalizedMessage());
        }
    }

    /**
     * 账号+邮箱+验证码验证
     */
    @GetMapping("checkInLaw")
    public Map<String, Object> checkInLaw(String emailAddr, String userName, String checkCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = ReturnMessage.successMessage("验证成功！");
            map.put("lawInfo", biz.checkInLaw(emailAddr, userName, checkCode));
            return map;
        } catch (Exception e) {
            return ReturnMessage.failMessage(e.getLocalizedMessage());
        }
    }

    /**
     * 密码找回
     */
    @PutMapping("updatePwdByEmailCode")
    public Map<String, Object> updatePwdByEmailCode(String emailAddr, String userName, String newPwd, String lawInfo) {
        try {
            biz.updatePwdByEmailCode(emailAddr, userName, newPwd, lawInfo);
            return ReturnMessage.successMessage("修改成功！");
        } catch (Exception e) {
            return ReturnMessage.failMessage(e.getLocalizedMessage());
        }
    }

    public String getModelName() {
        return "用户管理";
    }
}
