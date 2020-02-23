package com.siti.enterprise.mapper;

import com.siti.enterprise.po.EnterpriseInfo;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EnterpriseMapper extends Mapper<EnterpriseInfo> {


    @Select("<script>" +
            "select * from enterprise_info where 1=1 " +
            "<if test = \"enterpriseName!=null and enterpriseName!=''\"> where ent_name like '%${enterpriseName}%' </if>" +
            "</script>")
    List<EnterpriseInfo> getEnterprise(@Param("enterpriseName") String enterpriseName);

    /**
     * 根据唯一确定的主键项查找enterprise
     *
     * @param fullName
     * @param tel
     */
    @Select("<script>" +
            "SELECT * FROM enterprise_info WHERE ent_fullname=#{fullName} AND tel=#{tel}" +
            "</script>")
    EnterpriseInfo getEnterpriseByKey(@Param("fullName") String fullName, @Param("tel") String tel);

    /**
     * 修改enterprise
     *
     * @param info
     */
    @Update("UPDATE `enterprise_info` SET `ent_name`=#{obj.entName}, `ent_address`=#{obj.entAddress}," +
            " `ent_pic`=#{obj.entPic}, `city`=#{obj.city}, `province`=#{obj.province}, `lng`=#{obj.lng}, `lat`=#{obj.lat}," +
            " `contact_name`=#{obj.contactName}, `email_address`=#{obj.emailAddress}, `industry`=#{obj.industry}," +
            " `ent_type`=#{obj.entType}, `staff_amount`=#{obj.staffAmount}, `quali_certificate`=#{obj.qualiCertificate}" +
            "  WHERE  (`ent_fullname`=#{obj.entFullname}) AND (`tel`=#{obj.tel})")
    void updateEnterpriseByPO(@Param("obj") EnterpriseInfo info);
}
