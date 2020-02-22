package com.siti.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserRoleMapper {

    /**
     * 根据用户id获取相关的角色id
     */
    @Select("select role_id from sys_rela_user_org_role where user_id =#{userId}")
    List<Integer> getRoleIdByUserId(@Param("userId") int userId);

    /**
     * 根据用户id删除用户角色关系
     */
    @Delete("delete from sys_rela_user_org_role where user_id =#{userId}")
    void deleteByUserId(@Param("userId") int userId);

    /**
     * 根据角色ID删除用户角色关系
     */
    @Delete("delete from sys_rela_user_org_role where role_id =#{roleId}")
    void deleteByRoleId(@Param("roleId") int roleId);

}
