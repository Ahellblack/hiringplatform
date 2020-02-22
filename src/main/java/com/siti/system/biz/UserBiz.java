package com.siti.system.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siti.common.BaseBiz;
import com.siti.common.exception.MyException;
import com.siti.security.LoginUserInfo;
import com.siti.system.ctrl.LoginCtrl;
import com.siti.system.mapper.RoleMapper;
import com.siti.system.mapper.UserMapper;
import com.siti.system.po.Role;
import com.siti.system.po.User;
import com.siti.system.vo.AreaVo;
import com.siti.utils.MailUtil;
import com.siti.utils.Md5Utils;
import com.siti.utils.PatternMatch;
import com.siti.utils.newaes.base64.BackAES;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zyw on 2018/7/23.
 */
@Service
@Transactional
public class UserBiz extends BaseBiz<UserMapper, User> {

    @Resource
    private RoleMapper roleMapper;

    public User getLoginUser() {
        User user = dao.selectByPrimaryKey(LoginCtrl.getLoginUserInfo().getId());
        return user;
    }

    // 用于缓存用户邮箱验证码信息
    public static Map<String, Object> userEmailCheckCode = new HashMap<String, Object>();

    public PageInfo<User> getUser(int page, int pageSize, String userName) {

        LoginUserInfo loginUserInfo = LoginCtrl.getLoginUserInfo();

        List<User> list;
        PageHelper.startPage(page, pageSize);
        list = dao.getUser(loginUserInfo.getRoleCode(), userName);

        return new PageInfo<>(list);
    }



    /*保存用户*/
    public void saveUser(String operation, User user) {
        if ("ADD".equals(operation)) {
            if (user == null
                    || user.getUserName() == null || "".equals(user.getUserName())
                    || user.getRealName() == null || "".equals(user.getRealName())
                     || user.getRoleCode() == null || "".equals(user.getRoleCode())) {
                throw new MyException("信息不全");
            }
            User userTemp = dao.findUserByUserName(user.getUserName());
            if (userTemp != null) {
                throw new MyException("用户名被占用");
            }
        } else {
            if (user == null || user.getId() == null
                    || user.getRealName() == null || "".equals(user.getRealName())) {
                throw new MyException("信息不全");
            }
        }
        user.setPhoneNum(user.getPhoneNum() == null ? "" : user.getPhoneNum());
        user.setEmailAddr(user.getEmailAddr() == null ? "" : user.getEmailAddr());
        user.setStatus(user.getStatus() == null ? 1 : user.getStatus());
        user.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        user.setUpdateTime(new Date());
        Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
        user.setRoleCode(role.getCode());
        if ("ADD".equals(operation)) {
            user.setPassword(Md5Utils.encryptString("123456"));
            user.setId(null);
            dao.insert(user);
        } else {
            dao.updateByPrimaryKeySelective(user);
        }
    }


    /* 冻结系统用户*/
    public void freezeUser(Integer userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(2);
        user.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        dao.updateByPrimaryKeySelective(user);
    }

    /*解冻结系统用户*/
    public void unFreezeUser(Integer userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(1);
        user.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        dao.updateByPrimaryKeySelective(user);
    }

    /*注销 系统用户*/
    public void cancelUser(Integer userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(0);
        user.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        dao.updateByPrimaryKeySelective(user);
    }


    /**
     * 获取加密密码
     *
     * @param password
     * @return
     */
    public String getEncryptPassword(String password) {
        return Md5Utils.encryptString(password);
    }


    /**
     * 密码验证
     *
     * @return 验证通过返回true, 失败返回false
     */
    public boolean checkPwd(Integer userId, String logPwd) {  // 密码 已加密
        String oldPassword = this.getEncryptPassword(logPwd);
        User user = new User();
        user.setId(userId);
        List<User> users = dao.select(user);
        String password = users.get(0).getPassword();
        return password.equals(oldPassword);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param logPwd
     * @param updateBy
     */
    public void modifyPwd(Integer userId, String logPwd, Integer updateBy) {
        String password = this.getEncryptPassword(logPwd);
        User user = new User();
        user.setId(userId);
        user.setPassword(password);
        user.setUpdateBy(updateBy);
        dao.updateByPrimaryKeySelective(user);
    }

    /**
     * 重置密码
     *
     * @param userName
     */
    public void resetPwd(String userName) {
        String password = this.getEncryptPassword("123456");
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        dao.updateByUserName(user);
    }

    /**
     * 根据用户名+邮箱地址查询用户信息
     */
    public List<User> findUserInfoByEmailAndUserName(String email, String userName) {
        return dao.findUserInfoByEmailAndUserName(email, userName);
    }


    /**
     * 根据用户名+邮箱地址，修改登录密码
     */
    public void updatePwdByUserNameAndEmailAddr(String email, String userName, String pwd) {
        dao.updatePwdByUserNameAndEmail(email, userName, pwd);
    }

    /**
     * 发送邮箱验证码，找回密码
     * 账号+邮箱地址验证用户合法性
     */
    public void getEmailCheckCode(String emailAddr, String userName) {
        if (emailAddr == null || userName == null) {
            throw new MyException("参数错误，请核实！");
        }
        boolean macth = PatternMatch.matchEmailAddr(emailAddr);
        if (!macth) {
            throw new MyException("邮箱格式错误，请核实！");
        }
        int size = findUserInfoByEmailAndUserName(emailAddr, userName).size();
        if (size != 1) {
            throw new MyException("邮箱和账号信息不匹配，请核实！");
        }
        String info = (String) userEmailCheckCode.get(userName + emailAddr);
        String[] code = info != null ? info.split(",") : null;
        long current = System.currentTimeMillis();
        if (code != null && Math.abs(current - Long.parseLong(code[1])) <= 1000 * 60) {
            throw new MyException("验证码获取过于频繁，请稍后操作！");
        }
        String checkCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        String subject = "密码找回";
        String content = "[共享单车治理]\n" +
                "账号" + userName + "正在通过邮箱找回密码；" +
                "您获取的验证码为：" + checkCode +
                "，请在30分钟内使用，过期验证码将失效" +
                "，若不是您个人操作，请忽略该条信息。";
        int ret = MailUtil.sendMailBySSL(subject, content, emailAddr);
        if (ret == 0) {
            userEmailCheckCode.put(userName + emailAddr, checkCode + "," + System.currentTimeMillis());
        } else {
            throw new MyException("邮箱服务器错误，无法发送验证码！");
        }
    }

    /**
     * 账号邮箱验证码验证
     *
     * @param userName
     * @param emailAddr
     * @param checkCode
     * @return
     */
    public String checkInLaw(String emailAddr, String userName, String checkCode) {
        if (emailAddr == null || userName == null) {
            throw new MyException("参数错误，请核实！");
        }
        boolean match = PatternMatch.matchEmailAddr(emailAddr);
        if (!match) {
            throw new MyException("邮箱格式错误，请核实！");
        }
        String info = (String) userEmailCheckCode.get(userName + emailAddr);
        String[] code = info != null ? info.split(",") : null;
        if (code == null) {
            throw new MyException("验证码过期，请重新获取！");
        }
        String codeInfo = code[0];
        Long codeTime = Long.parseLong(code[1]);
        Long current = System.currentTimeMillis();
        if (Math.abs(current - codeTime) > 1000 * 60 * 30) {
            throw new MyException("验证码过期，请重新获取！");
        }
        if (!codeInfo.equals(checkCode)) {
            throw new MyException("验证码错误，请重新获取！");
        } else {
            byte[] encryptResultStrname = new byte[0];
            try {
                encryptResultStrname = BackAES.encrypt(userName + emailAddr + ",success," + codeTime, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userEmailCheckCode.remove(userName + emailAddr);
            return new String(encryptResultStrname);
        }
    }


    /**
     * 修改密码
     *
     * @param emailAddr
     * @param userName
     * @param newPwd
     * @param lawInfo
     * @return
     * @throws Exception
     */
    public void updatePwdByEmailCode(String emailAddr, String userName, String newPwd, String lawInfo) {
        if (lawInfo == null || emailAddr == null || userName == null || newPwd == null) {
            throw new MyException("参数错误，请核实！");
        }
        lawInfo = lawInfo.replace(" ", "+");
        String tmpLaw = null;
        try {
            tmpLaw = BackAES.decrypt(lawInfo, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tmpLaw == null || tmpLaw.trim().length() == 0) {
            throw new MyException("非法操作");
        }
        String[] law = tmpLaw.split(",");
        if (law.length < 3) {
            throw new MyException("非法操作");
        }
        String lawUser = law[0];
        String lawSuccess = law[1];
        Long lawTime = Long.parseLong(law[2]);    // 获取验证码的时间
        Long current = System.currentTimeMillis();
        if ((userName + emailAddr).equals(lawUser) && "success".equals(lawSuccess)) {
            if (Math.abs(lawTime - current) > 1000 * 60 * 30) {
                throw new MyException("验证信息过期，请重新获取验证码");
            }
            updatePwdByUserNameAndEmailAddr(emailAddr, userName, getEncryptPassword(newPwd));
        } else {
            throw new MyException("非法操作");
        }
    }



    public void updatePushIdByUserName(String pushId, String userName) {
        if (userName == null || "".equals(userName)
                || pushId == null || "".equals(pushId)) {
            throw new MyException("参数错误");
        }
        dao.updatePushIdByPushId(pushId);
        dao.updatePushIdByUserNamePassword(pushId, userName);
    }



}

