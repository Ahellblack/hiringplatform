package com.siti.enterprise.mapper;

import com.siti.enterprise.po.PositionRelease;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PositionReleaseMapper extends Mapper<PositionRelease> {

    /**
     * 删除
     *
     * @param id
     */
    @Delete("delete from position_release where id=#{id}")
    void deleteById(@Param("id") int id);

    @Select("<script>" +
            "       select id, ent_id, post_id, age_range, other_require, post_dscrpt, " +
            "       min_salary, max_salary, social_benefit, work_experience, " +
            "       contact_ppl, contact_tel, remark, is_pub, input_time, update_time" +
            "       from position_release where 1=1 " +
            "       <if test = \"entId!=null and entId!=''\"> and ent_id =#{entId} </if> " +
            "       <if test = \"postId!=null and postId!=''\"> and post_id =#{postId} </if> " +
            "</script>")
    List<PositionRelease> getPositionRelease(@Param("entId") Integer entId,@Param("postId") Integer postId);
}
