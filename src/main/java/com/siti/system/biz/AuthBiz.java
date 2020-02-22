package com.siti.system.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.siti.common.BaseBiz;
import com.siti.common.constant.ConstantsButton;
import com.siti.security.LoginUserInfo;
import com.siti.system.ctrl.LoginCtrl;
import com.siti.system.mapper.AuthMapper;
import com.siti.system.mapper.RoleAuthMapper;
import com.siti.system.po.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2018/7/25.
 */
@Service
public class AuthBiz extends BaseBiz<AuthMapper, Auth> {
    @Autowired
    private RoleAuthMapper roleAuthDao;
    /*
     * 通过sort来查询所有的权限信息
     * @param page
     * @param pageSize
     * @param sort
     */

    public PageInfo<Auth> findBySort(int page, int pageSize, Integer sort) {
        Integer minus = null;
        String equalSort = null;
        if (sort != null) {
            minus = splitArr(sort) * 2;
            equalSort = sort.toString().substring(0, sort.toString().length() - minus);
        }
        PageHelper.startPage(page, pageSize);
        List<Auth> list = dao.findBySort(equalSort, minus);
        return new PageInfo<>(list);
    }

    private Integer splitArr(Integer sort) { // 500000000
        Integer count = 0;
        String str = String.valueOf(sort);
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }// 0500000000
        String str1 = "";
        String arr[] = new String[str.length() / 2]; // size=5
        for (int i = 0; i < str.length(); i++) {
            str1 += str.charAt(i);
            if ((i + 1) % 2 == 0) {
                arr[i / 2] = str1;
                if (str1.equals("00")) {
                    count++;
                }
                str1 = "";
            }
        }
        return count;
    }
    /*
     * 添加权限信息
     *
     * @param pidSort    父节点的排序号
     * @param insertSort 插入节点的排序号
     */

    @Transactional
    public void saveAuth(Auth auth, Integer pidSort, Integer insertSort, Integer insertType) {
        if (pidSort != null && !"".equals(pidSort)) {
            if (this.splitArr(pidSort) < 1) {
                throw new RuntimeException("不能为该权限添加子权限");
            }
        }
        if (pidSort == null) {    // 是否选择了父节点
            if (insertSort == null) {    // 是否选择了插入节点
                if (dao.isExistSon(auth.getPid(), auth.getId()) != null && dao.isExistSon(auth.getPid(), auth.getId()).size() > 0) {//是否存在可选插入节点，若存在则必选
                    throw new RuntimeException("请选择插入节点");
                }
                Integer maxSort = dao.getMaxMenuSort();
                if (maxSort == null || maxSort >= 2000000000) {    // 不能超过 2000000000，否则会出错
                    throw new RuntimeException("最大菜单sort异常，请联系开发人员");
                }
                auth.setSort(maxSort + 100000000);
            } else {
                if (insertType == 1) {    // 保存节点到插入节点之前
                    updateSort(auth.getPid(), insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(insertSort) * 2);//更新相关节点排序号
                    auth.setSort(insertSort);
                } else {    // 保存节点到插入节点之后
                    updateSort(auth.getPid(), insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(insertSort) * 2);//更新相关节点排序号
                    auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                }
            }
        } else {
            if (insertSort == null) {
                if (dao.isExistSon(auth.getPid(), auth.getId()) != null && dao.isExistSon(auth.getPid(), auth.getId()).size() > 0) {
                    throw new RuntimeException("请选择插入节点");
                }
                auth.setSort(pidSort + (int) Math.pow(100, splitArr(pidSort) - 1));
            } else {
                if (insertType == 1) {
                    updateSort(auth.getPid(), insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(insertSort) * 2);//更新相关节点排序号
                    auth.setSort(insertSort);
                } else {
                    updateSort(auth.getPid(), insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(insertSort) * 2);//更新相关节点排序号
                    auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                }
            }
        }
        if (auth.getPid() == null || "".equals(auth.getPid())) {
            auth.setPid(1);
        }
        if (auth.getType() == 1) {
            auth.setSetRange(0);
        } else if (auth.getType() == 3) {
            auth.setSetRange(1);
        }
        auth.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        dao.insert(auth);
    }

    /**
     * 添加时更新插入节点的排序号
     *
     * @param pid        父节点
     * @param sort       插入节点的排序号
     * @param plusSort   增加的排序号
     * @param insertType 添加节点至插入节点之前（1）或之后（2）
     * @param sortDigit  不匹配位数（从后往前）
     */
    private void updateSort(Integer pid, Integer sort, Integer plusSort, Integer insertType, Integer sortDigit) {
        List<Integer> sortLists = dao.updateSortList(pid, sort, "insert", insertType, null);
        for (int i = 0; i < sortLists.size(); i++) {
            String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
            dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "plus");
        }
    }

    /**
     * 修改权限信息
     *
     * @param oldPid     修改之前的父节点id
     * @param pidSort    修改之后的父节点排序号
     * @param insertSort 插入节点的排序号
     * @param insertType 添加节点至插入节点之前（1）或之后（2）
     */
    @Transactional
    public void updateAuth(Auth auth, Integer oldPid, Integer pidSort, Integer insertSort, Integer insertType) {
        auth.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
        if (oldPid == null && pidSort == null && insertSort == null && insertType == null) {
            dao.updateByPrimaryKeySelective(auth);
            return;
        }
        if (this.splitArr(auth.getSort()) >= this.splitArr(pidSort)) {
            throw new RuntimeException("不能将此权限修改为该权限的子权限");
        }
        Integer oldSort = auth.getSort();
        if (auth.getPid().equals(oldPid)) { //更换了父节点
            if (auth.getPid() != null) { //新父节点不为null
                if (insertType != null) { //插入节点不为null
                    if (insertType == 1) {
                        auth.setSort(insertSort);
                    } else if (insertType == 2) {
                        auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                    }
                } else { //插入节点为null
                    auth.setSort(pidSort + (int) Math.pow(100, (splitArr(pidSort) - 1)));
                }
            } else { //新父节点为null
                if (insertType == 1) {
                    auth.setSort(insertSort);
                } else if (insertType == 2) {
                    auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                }
            }
            updateSort(auth.getPid(), insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(insertSort) * 2);
            String sort = auth.getSort().toString();
            dao.updateSonSort(splitArr(oldSort) * 2, oldSort.toString().substring(0, oldSort.toString().length() - splitArr(oldSort) * 2), sort.substring(0, sort.length() - splitArr(auth.getSort()) * 2), splitArr(auth.getSort()) * 2, oldPid);
            updateSortD(oldPid, oldSort, (int) Math.pow(100, splitArr(oldSort)), splitArr(oldSort) * 2);
        } else { //未更换父节点
            if (auth.getPid() != null) { //新父节点不为null
                if (insertType != null) { //插入节点不为null
                    if (insertType == 1) {
                        if (auth.getSort() > insertSort)
                            auth.setSort(insertSort);
                        else
                            auth.setSort(insertSort - (int) Math.pow(100, splitArr(insertSort)));
                    } else if (insertType == 2) {
                        if (auth.getSort() > insertSort)
                            auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                        else
                            auth.setSort(insertSort);
                    }
                }
            } else { //新父节点为null
                if (insertType == 1) {
                    if (auth.getSort() > insertSort)
                        auth.setSort(insertSort);
                    else
                        auth.setSort(insertSort - (int) Math.pow(100, splitArr(insertSort)));
                } else if (insertType == 2) {
                    if (auth.getSort() > insertSort)
                        auth.setSort(insertSort + (int) Math.pow(100, splitArr(insertSort)));
                    else
                        auth.setSort(insertSort);
                }
            }
            String sort = auth.getSort().toString();
            dao.updateSonSortOne(splitArr(oldSort) * 2, oldSort.toString().substring(0, oldSort.toString().length() - splitArr(oldSort) * 2), splitArr(auth.getSort()) * 2, oldPid);
            updateSortU(auth.getPid(), oldSort, insertSort, (int) Math.pow(100, splitArr(insertSort)), insertType, splitArr(oldSort) * 2);//更新相关节点排序号
            dao.updateSonSortTwo(sort.substring(0, sort.length() - splitArr(auth.getSort()) * 2));
        }
        dao.updateByPrimaryKey(auth);
    }

    /**
     * 修改时更新插入节点的排序号
     *
     * @param pid        父节点
     * @param sort       排序号
     * @param insertSort 插入节点的排序号
     * @param insertType 添加节点至插入节点之前（1）或之后（2）
     * @param plusSort   增加的排序号
     * @param sortDigit  不匹配位数（从后往前）
     */
    private void updateSortU(Integer pid, Integer sort, Integer insertSort, Integer plusSort, Integer insertType, Integer sortDigit) {
        List<Integer> sortLists = dao.updateSortList(pid, sort, "update", insertType, insertSort);
        if (insertType == 1) {
            if (sort > insertSort) {
                for (int i = 0; i < sortLists.size(); i++) {
                    String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
                    dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "plus");
                }
            } else {
                for (int i = 0; i < sortLists.size(); i++) {
                    String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
                    dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "minus");
                }
            }
        } else {
            if (sort > insertSort) {
                for (int i = 0; i < sortLists.size(); i++) {
                    String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
                    dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "plus");
                }
            } else {
                for (int i = 0; i < sortLists.size(); i++) {
                    String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
                    dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "minus");
                }
            }
        }
    }

    /**
     * 判断某节点是否存在子节点
     *
     * @param pid
     * @param id
     * @return
     */
    public List<Auth> findSid(Integer pid, Integer id) {
        return dao.getSon(pid);
    }


    /**
     * 删除前先检查是否有子节点，有则不能删除，同时删除角色权限关联信息
     *
     * @param id
     * @param pid
     * @param sort
     */
    @Transactional
    public void deleteById(Integer id, Integer pid, Integer sort) {
        List<Auth> authList = this.findSid(id, null);
        if (authList != null && authList.size() > 0) {
            throw new RuntimeException("存在子节点信息，不能删除");
        }
        //删除角色权限关联表信息
        roleAuthDao.deleteByAuthId(id);
        dao.deleteByPrimaryKey(id);
        updateSortD(pid, sort, (int) Math.pow(100, splitArr(sort)), splitArr(sort) * 2);
    }


    /**
     * 删除时更新插入节点的排序号
     *
     * @param pid       父节点
     * @param sort      插入节点的排序号
     * @param plusSort  增加的排序号
     * @param sortDigit 不匹配位数（从后往前）
     */
    private void updateSortD(Integer pid, Integer sort, Integer plusSort, Integer sortDigit) {
        List<Integer> sortLists = dao.updateSortList(pid, sort, "delete", null, null);
        for (int i = 0; i < sortLists.size(); i++) {
            String sortLike = sortLists.get(i).toString().substring(0, sortLists.get(i).toString().length() - sortDigit);
            dao.updatePlusOrMinus(sortDigit, plusSort, sortLike, "minus");
        }
    }


    /**
     * 根据权限编码，获取权限范围
     *
     * @param authCode
     * @return my-self：限本人
     * my-orga：限所属部门（部门信息以英文“,”分割）
     * all：所有数据
     * null：无此权限
     */
    public String getAuthRange(String authCode) {
        LoginUserInfo user = LoginCtrl.getLoginUserInfo();
        Integer authId = this.getAuthIdByCode(authCode);
        if (user == null || authCode == null || authId == null || "".equals(authCode.trim()) || authId == 0) {
            return null;
        }
        int count = 0;
        List<AuthRange> authRangeList = this.getAuthRangeById(user.getId(), authId);
        if (authRangeList == null || authRangeList.size() == 0) {
            return null;
        }
        List<Integer> orgIdList = new ArrayList<Integer>();
        for (AuthRange range : authRangeList) {
            if ("all".equals(range.getRanges())) {
                return "all";
            }
            if ("my-orga".equals(range.getRanges())) {
                count++;
            }
            orgIdList.add(range.getOrgId());
        }
        if (count == 0) {
            return "my-self";
        }
        return null;
    }


    /**
     * 根据权限编码获取权限ID
     *
     * @param authCode
     * @return
     */
    public Integer getAuthIdByCode(String authCode) {
        if (authCode == null || "".equals(authCode.trim())) {
            return null;
        }
        return dao.getAuthIdByCode(authCode);
    }

    /**
     * 返回 权限范围列表
     *
     * @param userId
     * @param authId
     * @return
     */
    public List<AuthRange> getAuthRangeById(Integer userId, Integer authId) {
        if (authId == null || "".equals(authId) || authId == 0) {
            return null;
        }
        return dao.getAuthRangeById(userId, authId);
    }


    /**
     * 根据用户ID获取权限信息
     *
     * @param userId
     * @return
     */
    public List<Auth> getByUserId(Integer userId) {
        return dao.getByUserId(userId);
    }

    /**
     * 查询权限菜单树（不包含按钮权限）
     *
     * @return
     */
    public List<Auth> getAuthsTreeWithoutBtn() {
        String ranges = this.getAuthRange(ConstantsButton.SEARCH_AUTH);
        List<Integer> roleIds = new ArrayList<Integer>();
        if (!"all".equals(ranges)) {
            User user = LoginCtrl.getLoginUserInfo();
            for (Role role : user.getRoles()) {
                roleIds.add(role.getId());
            }
            if (roleIds.size() == 0) {
                roleIds = null;
            }
        } else {
            roleIds = null;
        }
        return dao.getAuthsTreeWithoutBtn(roleIds);
    }

    /**
     * 根据角色ID获取权限信息
     *
     * @param roleId
     * @return
     */
    public List<AuthRange> getByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        return dao.getByRoleId(roleId);
    }

    public List<Auth> getAuthsByPid(Integer pid) {
        if (pid == null) {
            pid = 1;
        }
        return dao.getAuthsByPid(pid);
    }

    public List<Auth> getAuthsTree() {
        /*
         * my-self：限本人
         * my-orga：限所属部门
         * all：所有数据
         */
        String ranges = this.getAuthRange(ConstantsButton.SEARCH_AUTH);
        List<Integer> roleIds = new ArrayList<Integer>();
        if (!"all".equals(ranges)) {
            User user = LoginCtrl.getLoginUserInfo();
            for (Role role : user.getRoles()) {
                roleIds.add(role.getId());
            }
            if (roleIds.size() == 0) {
                roleIds = null;
            }
        } else {
            roleIds = null;
        }
        return dao.getAuthsTree(roleIds);
    }

    public List<Auth> getAuthsButton() {
        return dao.getAuthsButton();
    }

    /*
     * 获取菜单
     *
     * @param roleIds
     * @return
     **/
    public List<Auth> getMenu(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return dao.getMenu(roleIds);
    }

}
