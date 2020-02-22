package com.siti.system.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import com.siti.system.po.RoleAuth;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleAuthMapper extends Mapper<RoleAuth> {

	/**
	 * 根据角色ID删除角色权限关系
	 */
	@Delete("delete from sys_rela_role_auth where role_id =#{roleId}")
	void deleteByRoleId(@Param("roleId") int roleId);

	/**
	 * 根据权限ID删除角色权限关系
	 */
	@Delete("delete from sys_rela_role_auth where auth_id =#{authId}")
	void deleteByAuthId(@Param("authId") int authId);

	/**
	 * 根据当前角色ID获取该ID对应的角色信息和低等级角色
	 *
	 * @param roleId
	 * @return List roleIds
	 */
	@SelectProvider(type = RoleAuthProvider.class, method = "getLowGradeRoleId")
	List<Integer> getLowGradeRoleId(@Param("roleId") int roleId);

	/**
	 * 根据权限ID获取关联角色ID
	 */
	@Select("select role_id from sys_rela_role_auth where auth_id =#{authId}")
	List<Integer> getRoleIdByAuthId(@Param("authId") int authId);

}
