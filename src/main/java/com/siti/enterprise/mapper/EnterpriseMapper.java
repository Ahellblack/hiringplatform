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
}
