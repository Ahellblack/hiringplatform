package com.siti.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import com.siti.system.po.Auth;
import com.siti.system.po.AuthRange;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by zyw on 2018/7/25.
 */
public interface AuthMapper extends Mapper<Auth> {

    @Select("select * from sys_auth where code=#{authCode}")
    Auth selectAuthByCode(@Param("authCode") String authCode);

    @Select("<script> select * from sys_auth where 1=1 " +
            "<if test='sort!=null'> and substring(sort,1,length(sort)-#{minus})=#{sort} </if> " +
            "order by sort asc " +
            "</script>")
    List<Auth> findBySort(@Param("sort") String sort, @Param("minus") Integer minus);

    /**
     * 根据权限编码获取权限ID
     *
     * @param authCode
     * @return
     */
    @Select("select id from sys_auth where code=#{authCode}")
    Integer getAuthIdByCode(@Param("authCode") String authCode);

    /**
     * 返回 权限范围列表
     *
     * @param userId
     * @param authId
     * @return
     */
    @Select("select a.user_id,a.org_id,a.role_id,b.auth_id,b.ranges from sys_rela_user_org_role a " +
            " left join sys_rela_role_auth b on a.role_id=b.role_id " +
            " where a.user_id=#{userId} and b.auth_id=#{authId}")
    List<AuthRange> getAuthRangeById(@Param("userId") Integer userId, @Param("authId") Integer authId);

    /**
     * 根据角色ID获取授权信息
     */
    @Select({"<script>",
            " select * from sys_auth where id in (" +
                    " select distinct auth_id from sys_rela_role_auth where role_id in " +
                    "<foreach collection =\"roleIds\" item=\"roleId\" index= \"index\" open=\"(\" separator=\",\" close=\")\"> #{roleId} </foreach>" +
                    ") and (code != '' and code is not null) ",
            "</script>"})
    List<Auth> getPermissionByRoleId(@Param("roleIds") List<Integer> roleIds);

    /**
     * 根据角色ID获取权限信息
     */
    @Select("select srra.role_id,srra.auth_id as id,srra.ranges,su.pid,su.type,su.set_range,su.name as auth_name,scd.val as range_name " +
            " from sys_rela_role_auth srra " +
            " left join sys_auth su on srra.auth_id=su.id " +
            " left join sys_const_dict scd on srra.ranges=scd.name " +
            "where role_id=#{roleId} ")
    List<AuthRange> getByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据用户ID获取权限信息
     */
    @Select("select * from sys_auth where id in (select distinct auth_id from sys_rela_role_auth where role_id in(select distinct role_id from sys_rela_user_role where user_id = #{userId}))")
    List<Auth> getByUserId(Integer userId);

    /**
     * 获取菜单
     */
    @Select({"<script>",
            "select * from sys_auth " +
                    " where id in (select distinct auth_id from sys_rela_role_auth where role_id in " +
                    "<foreach collection =\"roleIds\" item=\"roleId\" index= \"index\" open=\"(\" separator=\",\" close=\")\"> #{roleId} </foreach>) " +
                    " and type = 1 order by sort asc ",
            "</script>"})
    List<Auth> getMenu(@Param("roleIds") List<Integer> roleIds);

    /**
     * 判断某节点是否存在子节点
     */
    @Select("<script> select * from (select * from sys_auth where 1=1 <when test='id==null'> <when test='pid==null'> and pid is null </when> <otherwise> and pid =#{pid} </otherwise> </when><otherwise> <when test='pid==null'> and pid is null and id != #{id} </when> <otherwise> and pid =#{pid} and id != #{id} </otherwise></otherwise> limit 1) a </script>")
    List<Auth> isExistSon(@Param("pid") Integer pid, @Param("id") Integer id);

    /**
     * 判断某节点是否存在子节点
     */
    @Select("select * from sys_auth where 1=1  and pid =#{pid} limit 1")
    List<Auth> getSon(@Param("pid") Integer pid);

    /**
     * 查询权限树
     */
    @Select({"<script>",
            " select * from sys_auth where 1=1 " +
                    "<if test=\"roleIds!=null\"> and id in ( " +
                    " select distinct auth_id from sys_rela_role_auth where role_id in " +
                    "<foreach collection =\"roleIds\" item=\"roleId\" index= \"index\" open=\"(\" separator=\",\" close=\")\"> #{roleId} </foreach>" +
                    ") </if>" +
                    " order by sort asc",
            "</script>"})
    List<Auth> getAuthsTree(@Param("roleIds") List<Integer> roleIds);

    /**
     * 查询权限菜单树（不包含按钮权限）
     */
    @Select({"<script>",
            " select * from sys_auth where type =1 " +
                    "<if test=\"roleIds!=null\"> and id in ( " +
                    " select distinct auth_id from sys_rela_role_auth where role_id in " +
                    "<foreach collection =\"roleIds\" item=\"roleId\" index= \"index\" open=\"(\" separator=\",\" close=\")\"> #{roleId} </foreach>" +
                    ") </if>" +
                    " order by sort asc",
            "</script>"})
    List<Auth> getAuthsTreeWithoutBtn(@Param("roleIds") List<Integer> roleIds);

    /**
     * 查询子权限
     */
    @Select("select * from sys_auth where pid = #{pid} order by sort asc")
    List<Auth> getAuthsByPid(Integer pid);

	/*----------------------以下均为更新排序号所需接口------------------------------------*/


    //所有受影响的节点排序号加或减
    @UpdateProvider(type = AuthProvider.class, method = "updatePlusOrMinus")
    void updatePlusOrMinus(@Param("sortDigit") Integer sortDigit, @Param("plusSort") Integer plusSort, @Param("sortLike") String sortLike, @Param("type") String type);

    //查询需要更新的同级节点序号
    @SelectProvider(type = AuthProvider.class, method = "updateSortList")
    List<Integer> updateSortList(@Param("pid") Integer pid, @Param("sort") Integer sort, @Param("type") String type, @Param("insertType") Integer insertType, @Param("insertSort") Integer insertSort);

    //更新改变节点以及其子节点的序号
    @UpdateProvider(type = AuthProvider.class, method = "updateSonSort")
    void updateSonSort(@Param("sortDigit") Integer sortDigit, @Param("oldSort") String oldSort, @Param("sort") String sort, @Param("updateSort") Integer updateSort, @Param("oldPid") Integer oldPid);

    //分两步修改子节点的排序号----第一步 将子节点的排序号修改，给修改时更新的节点的子节点挪位置（非最终修改）
    @UpdateProvider(type = AuthProvider.class, method = "updateSonSortOne")
    void updateSonSortOne(@Param("sortDigit") Integer sortDigit, @Param("oldSort") String oldSort, @Param("updateSort") Integer updateSort, @Param("oldPid") Integer oldPid);

    //分两步修改子节点的排序号----第二步 最终修改子节点的排序号
    @UpdateProvider(type = AuthProvider.class, method = "updateSonSortTwo")
    void updateSonSortTwo(@Param("sort") String sort);



    /*---------------------------------------------------------  hongtu ADD -------------------------------------------------*/

    /**
     * 返回最大的根目录sort
     *
     * @return
     */
    @Select("select MAX(sort) as sort from sys_auth where sort like '%00000000' ")
    Integer getMaxMenuSort();

    @Select("select id,name from sys_auth where type=3 ")
    List<Auth> getAuthsButton();


}
