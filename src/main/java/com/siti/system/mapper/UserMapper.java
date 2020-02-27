package com.siti.system.mapper;

import org.apache.ibatis.annotations.*;

import com.siti.system.po.User;
import com.siti.system.vo.AreaVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Created by zyw on 2018/7/23.
 */
public interface UserMapper extends Mapper<User> {

    @Select({"<script>",
            "select su.id,su.user_name,su.real_name,su.role_code,su.sex,su.email_addr,su.phone_num,su.`status`,su.update_by,su.update_time,su.remark," +
                    " su1.real_name update_name,uor.org_id main_org,so.`name` org_name,CONCAT(IFNULL(so.path,''),',',so.id) org_path,uor.role_id,sr.name role_name,uor.major_region " +
                    " from sys_user su " +
                    " left join sys_user su1 on su.update_by=su1.id " +
                    " left join sys_rela_user_org_role uor on su.id=uor.user_id " +
                    " left join sys_org so on uor.org_id=so.id " +
                    " left join sys_role sr on uor.role_id=sr.id " +
                    " where su.status!=0 " +
                    "<if test=\"orgIds!=null\"> and uor.org_id in (${orgIds})</if>" +
                    "<if test=\"regions!=null\"> and ((su.role_code='manage' and (uor.major_region is null or uor.major_region='' or " +
                    "<foreach collection =\"regions\" item=\"region\" index= \"index\" separator=\"or\" > " +
                    " uor.major_region like concat('%',#{region},'%') " +
                    "</foreach>" +
                    ")) or su.role_code='clean')</if>" +
                    "<if test=\"orgIdsSearch!=null\"> and uor.org_id in (${orgIdsSearch}) </if>" +
                    "<if test=\"userType!=null\"> and su.role_code=#{userType}</if>" +
                    "<if test=\"areaCode!=null\"> and (FUN_INTE_ARRAY(#{areaCode},uor.major_region) or FUN_INTE_ARRAY(#{orgShortNamesSearch},uor.major_region))</if>" +
                    "<choose>" +
                    "<when test=\"type=='manage'\">and su.role_code in (#{roleCode},'global','dispatch')</when>" +
                    "<when test=\"type=='bike'\">and su.role_code in ('manage','clean')</when>" +
                    "<otherwise> and su.role_code in ('#') </otherwise>" +
                    "</choose>" +
                    "<if test=\"userName!=null and userName!=''\"> and (su.user_name like \"%\"#{userName}\"%\" or su.real_name like \"%\"#{userName}\"%\")</if>" +
                    " order by FIELD(su.role_code,'admin','global','dispatch','manage','clean')",
            "</script>"})
    List<User> getUser(@Param("roleCode") String roleCode, @Param("userName") String userName);


    /*根据用户名查询用户信息*/
    @Select("select * from sys_user where phone_num=#{userName}")
    /*@Results(id = "progItem", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.siti.system.mapper.RoleMapper.getByUserId")),
    })*/
    User findUserByUserName(@Param("userName") String userName);


    /**
     * 根据用户名查询是否存在用户信息
     */
    @Select("<script> select count(id) from sys_user where user_name=#{userName} <if test='id!=null'> and id!=#{id} </if></script>")
    Boolean isExistName(@Param("userName") String userName, @Param("id") Integer id);

    /**
     * 重置密码
     *
     * @param user
     */
    @Update("update sys_user set password=#{password},update_by=#{updateBy} where user_name=#{userName} ")
    void updateByUserName(User user);

    /**
     * 根据邮箱+用户名查找用户信息
     */
    @Select("select * from sys_user where email_addr=#{email} and user_name=#{userName}")
    List<User> findUserInfoByEmailAndUserName(@Param("email") String email, @Param("userName") String userName);

    /**
     * 根据手机号查找用户信息
     */
    @Select("select * from sys_user where phone_num=#{phoneNum}")
    List<User> findUserInfoByPhoneNum(@Param("phoneNum") String phoneNum);

    /**
     * 根据用户名+邮箱地址，修改登录密码
     */
    @Update("update sys_user set password=#{pwd} where email_addr=#{email} and user_name=#{userName}")
    void updatePwdByUserNameAndEmail(@Param("email") String email, @Param("userName") String userName, @Param("pwd") String pwd);

    /**
     * 获取组织下所有组织的用户
     */
    @Select({"<script>",
            "select su.*,sr.major_region from sys_user su " +
                    "left join sys_rela_user_org_role sr on su.id=sr.user_id " +
                    "left join sys_role sro on sr.role_id=sro.id " +
                    "where su.`status`=1 and su.id !=#{uId} " +

                    "<if test=\"areaCodesSearch!=null\"> and (FUN_INTE_ARRAY(#{areaCodesSearch},sr.major_region) or FUN_INTE_ARRAY(#{orgShortNamesSearch},sr.major_region))</if>" +

                    "<if test=\"regions!=null\"> and (su.role_code='manage' and (sr.major_region is null or sr.major_region='' or " +
                    "<foreach collection =\"regions\" item=\"region\" index= \"index\" separator=\"or\" > " +
                    " sr.major_region like concat('%',#{region},'%') " +
                    "</foreach>" +
                    "))</if>" +

                    "<if test=\" orgId!=null\">and sr.org_id=#{orgId}</if> " +
                    "<if test=\" roleCode!=null and roleCode!=''\">and sro.code=#{roleCode}</if> ",
            "</script>"})
    List<User> getUserByOrg(@Param("uId") Integer uId, @Param("orgId") Integer orgId, @Param("regions") List<Integer> regions,
                            @Param("roleCode") String roleCode, @Param("areaCodesSearch") String areaCodesSearch, @Param("orgShortNamesSearch") String orgShortNamesSearch);

    @Update("update sys_user set push_id=null where push_id=#{pushId}")
    void updatePushIdByPushId(@Param("pushId") String pushId);

    @Update("update sys_user set push_id=#{pushId} where user_name=#{userName}")
    void updatePushIdByUserNamePassword(@Param("pushId") String pushId, @Param("userName") String userName);


    @Select("select CountyCode,TownCode from t_baseparkingregionalinfo where st_within(geomfromtext(#{point}),PolygonGeom)")
    AreaVo getRegionalCode(@Param("point") String point);

    @Select({"<script>",
            "SELECT id,user_name,real_name from sys_user where id in " +
                    "<foreach collection ='uIdList' item='uid' index= 'index' open='(' separator=',' close=')'> #{uid} </foreach>",
            "</script>"})
    List<User> getUserByOrgIds(@Param("uIdList") List<Integer> uIdList);

    @Select("select * from org_user where id=#{id}")
    User getUserById(@Param("id") String id);

    /**
     * 查询负责企业下面所有的运维人员
     */
    @Select("SELECT user_id FROM `sys_rela_user_org_role` where major_region in " +
            "(SELECT id FROM `sys_org` where pid=#{areaId} and sort=5) and role_id=#{roleId} and org_id=#{orgId}")
    Set<String> getUnderMajorUser(@Param("areaId") String areaId, @Param("orgId") Integer OrgId, @Param("roleId") Integer roleId);
}


