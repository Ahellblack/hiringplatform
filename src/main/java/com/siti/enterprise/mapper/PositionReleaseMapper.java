package com.siti.enterprise.mapper;

import com.siti.enterprise.po.PositionRelease;
import com.siti.enterprise.vo.PositionReleaseVo;
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
            "       pf.min_salary, pf.max_salary,  pf.social_benefit, work_experience,  pf.work_hour, pf.work_shift, pf.min_education,pf.recruit_num," +
            "       pf.contact_ppl,  pf.contact_tel, pf.remark, is_pub, pf.input_time, pf.update_time,ei.city,ei.province,ei.ent_name,pi.post_name," +
            "       ei.ent_fullname,ei.ent_address,ei.lng,ei.lat,ei.contact_name,ei.tel,ei.email_address,ei.industry,ei.ent_type,ei.quali_certificate,ei.is_verify,ei.remark,ei.staff_amount" +
            "       from position_release pf left join enterprise_info ei on pf.ent_id = ei.id " +
            "       left join position_info pi on pf.post_id = pi.id " +
            "       where 1=1 " +
            "       <if test = \"entId!=null and entId!=''\"> and ent_id =#{entId} </if> " +
            "       <if test = \"postId!=null and postId!=''\"> and post_id =#{postId} </if> " +
            "       <if test = \"benefits!=null and benefits!=''\">  and (" +
            "       <foreach collection=\"benefits\" item=\"item\" index=\"index\" separator=\"or\">" +
            "             find_in_set(#{item},social_benefit) " +
            "       </foreach>" +
            "       )</if> " +
            "       <if test = \"minSalary!=null and minSalary!=''\"> and min_salary &gt;#{minSalary} </if> " +
            "       <if test = \"maxSalary!=null and maxSalary!=''\"> and max_salary &lt;#{maxSalary} </if> " +
            "       <if test = \"city!=null and city!=''\"> and ei.city =#{city} </if> " +
            "       <if test=\"content!=null\"> and (ei.ent_name like concat(concat('%',#{content}),'%') or pi.post_name like concat(concat('%',#{content}),'%')) </if> "+
            "</script>")
    List<PositionReleaseVo> getPosition(@Param("entId")Integer entId, @Param("postId")Integer postId, @Param("benefits")String[] benefits,
                                        @Param("minSalary") Integer minSalary, @Param("maxSalary") Integer maxSalary, @Param("city")String city,@Param("content")String content);
}
