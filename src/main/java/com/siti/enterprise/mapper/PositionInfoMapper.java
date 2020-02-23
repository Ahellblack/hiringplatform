package com.siti.enterprise.mapper;

import com.siti.enterprise.po.PositionInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface PositionInfoMapper extends Mapper<PositionInfo> {

    /**
     * 获取一级分类
     *
     * @param firstType
     */
    @Select({"<script>", "SELECT DISTINCT first_post_type firstType FROM position_info WHERE 1=1" +
            "<if test = \"firstType!=null and firstType!=''\">  and first_post_type LIKE '${firstType}%' </if>" +
            " ORDER BY CONVERT(first_post_type USING gbk)", "</script>"})
    List<String> getFirstType(@Param("firstType") String firstType);

    /**
     * 获取二级分类
     *
     * @param firstType
     * @param secondType
     */
    @Select({"<script>", "SELECT DISTINCT second_post_type secondType FROM position_info WHERE first_post_type=#{firstType}" +
            "<if test = \"secondType!=null and secondType!=''\">  and second_post_type LIKE '${secondType}%' </if>" +
            " ORDER BY CONVERT(second_post_type USING gbk)", "</script>"})
    List<String> getSecondType(@Param("firstType") String firstType, @Param("secondType") String secondType);

    /**
     * 获取具体职位
     *
     * @param firstType
     * @param secondType
     * @param position
     */
    @Select({"<script>", "SELECT post_name postName,id postId FROM position_info WHERE first_post_type=#{firstType}" +
            " and second_post_type=#{secondType}" +
            "<if test = \"position!=null and position!=''\">  and post_name LIKE '${position}%' </if>" +
            " ORDER BY CONVERT(post_name USING gbk)", "</script>"})
    List<Map<String, Object>> getPositions(@Param("firstType") String firstType, @Param("secondType") String secondType,
                                           @Param("position") String position);

    @Select("SELECT DISTINCT(second_post_type) FROM position_info")
    List<String> getSecondPositionList();

    @Select("SELECT DISTINCT(first_post_type) FROM position_info where second_post_type=#{secondType}")
    List<String> getFirstPositionList(@Param("secondType") String data);

}
