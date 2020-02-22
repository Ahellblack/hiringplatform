package com.siti.enterprise.mapper;

import com.siti.enterprise.po.PositionRelease;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface PositionReleaseMapper extends Mapper<PositionRelease> {

    /**
     * 删除
     *
     * @param id
     */
    @Delete("delete from position_release where id=#{id}")
    void deleteById(@Param("id") int id);
}
