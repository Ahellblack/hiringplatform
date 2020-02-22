package com.siti.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.siti.system.po.Role;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2018/7/25.
 */
public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据角色名查询是否存在角色信息
     *
     * @param name
     * @param id
     * @return
     */
    @Select("<script> select count(1) from sys_role where name =#{name} <if test='id!=null'> and id!=#{id} </if></script>")
    Boolean findExistByName(@Param("name") String name, @Param("id") Integer id);

    /**
     * 根据角色编码查询是否存在角色信息
     *
     * @param code
     * @param id
     * @return
     */
    @Select("<script> select count(1) from sys_role where code =#{code} <if test='id!=null'> and id!=#{id} </if></script>")
    Boolean findExistByCode(@Param("code") String code, @Param("id") Integer id);

    /*
     * 根据角色名查找所有角色的信息
     */
    @Select({"<script>",
            "select * from sys_role where 1=1 " +
                    "<if test=\"roleName!=null and roleName!=''\"> and (name like \"%\"#{name}\"%\")</if>",
            "</script>"})
    List<Role> getRole(@Param("roleName") String roleName);

    /**
     * 根据权限id获取角色信息
     *
     * @param authId
     * @return
     */
    @Select("select * from sys_role where id in (select distinct role_id from sys_rela_user_org_role where auth_id =#{authId})")
    List<Role> getRolesByAuthId(@Param("authId") Integer authId);

    /**
     * 根据角色id查询角色信息
     * @param roleIds
     * @return
     */
    @Select("<script> select * from sys_role where id in <foreach collection ='roleIds' item='roleId' index= 'index' open='(' separator=',' close=')'> #{roleId} </foreach></script>")
    List<Role> findByIds(@Param("roleIds") List<Integer> roleIds);

    /**
     * 根据用户id查询角色信息
     *
     * @param userId
     * @return
     */
    @Select("select * from sys_role where id in(select role_id from sys_rela_user_org_role where user_id=#{userId})")
    List<Role> getByUserId(@Param("userId") Integer userId);


    /**
     * 角色部分所属部门
     *
     * @param ranges
     * @param uId
     * @param name
     * @return
     */
    @Select({"<script>",
            " select * from sys_role " +
                    " where 1=1 " +
                    "<choose>" +
                    "<when test=\"ranges=='all'\"></when>" +
                    "<otherwise> and update_by=#{uId} </otherwise>" +
                    "</choose>" +
                    "<if test=\"name!=null\"> and name like \"%\"#{name}\"%\" </if>",
            "</script >"})
    List<Role> findByNameLike(@Param("ranges") String ranges, @Param("uId") Integer uId, @Param("name") String name);

    @Select({"<script>",
            "SELECT * FROM sys_role where 1=1 " +
                    "<if test=\"type!=null and type!=''\"> and type=#{type}</if>",
            "</script >"})
    List<Role> getRoles(@Param("type") String type);

    @Select("select * from sys_role where code=#{code}")
    Role getRoleByCode(@Param("code") String code);

    /**
     * 系统管理-运维用户-选择组织下拉框
     * @param type */
    @Select({"<script>",
            "select DISTINCT so.id orgId,so.`name` orgName from sys_user su left join sys_user su1 on su.update_by=su1.id" +
                    "  join sys_rela_user_org_role uor on su.id=uor.user_id" +
                    " left join sys_org so on uor.org_id=so.id left join sys_role sr on uor.role_id=sr.id" +
                    " where su.status!=0" +
                    "<if test=\"type!=null and type!=''\"> and su.role_code in ('manage','clean')</if>"
            ,"</script >"})
    List<Map<String, Object>> getOrgNameByRole(@Param("type") String type);
}
