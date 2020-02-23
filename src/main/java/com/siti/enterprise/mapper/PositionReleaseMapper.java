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
            "       min_salary, max_salary, social_benefit, work_experience, work_hour,work_shift,min_education," +
            "       contact_ppl, contact_tel, remark, is_pub, input_time, update_time" +
            "       from position_release where 1=1 " +
            "       <if test = \"entId!=null and entId!=''\"> and ent_id =#{entId} </if> " +
            "       <if test = \"postId!=null and postId!=''\"> and post_id =#{postId} </if> " +
            "</script>")
    List<PositionRelease> getPositionRelease(@Param("entId") Integer entId,@Param("postId") Integer postId);

    @Select("<script>" +
            "       select pf.id, pf.ent_id, post_id,  pf.age_range,  pf.other_require,  pf.post_dscrpt, " +
            "       pf.min_salary, pf.max_salary,  pf.social_benefit, work_experience,  pf.work_hour, pf.work_shift, pf.min_education," +
            "       pf.contact_ppl,  pf.contact_tel, pf.remark, is_pub, pf.input_time, pf.update_time,ei.city,ei.province " +
            "       from position_release pf left join enterprise_info ei on pf.ent_id = ei.id where 1=1 " +
            "       <if test = \"entId!=null and entId!=''\"> and ent_id =#{entId} </if> " +
            "       <if test = \"postId!=null and postId!=''\"> and post_id =#{postId} </if> " +
            "       <if test = \"benefits!=null and benefits!=''\">  and (" +
            "       <foreach collection=\"benefits\" item=\"item\" index=\"index\" separator=\"or\">" +
            "             find_in_set(#{item},social_benefit) " +
            "       </foreach>" +
            "       )</if> " +
            "       <if test = \"minSalary!=null and minSalary!=''\"> and min_salary &gt;#{minSalary} </if> " +
            "       <if test = \"maxSalary!=null and maxSalary!=''\"> and max_salary &lt;#{maxSalary} </if> " +
            "       <if test = \"maxSalary!=null and maxSalary!=''\"> and max_salary &lt;#{maxSalary} </if> " +
            "</script>")
    List<PositionRelease> getPosition(@Param("entId")Integer entId, @Param("postId")Integer postId, @Param("benefits")String[] benefits,
                                      @Param("minSalary") Integer minSalary,@Param("maxSalary") Integer maxSalary,@Param("city")String city);
}
