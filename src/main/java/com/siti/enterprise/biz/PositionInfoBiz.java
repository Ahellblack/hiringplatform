package com.siti.enterprise.biz;

import com.siti.config.EasyCache;
import com.siti.enterprise.mapper.PositionInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionInfoBiz {

    @Resource
    private PositionInfoMapper positionInfoMapper;

    @Resource
    private EasyCache easyCache;

    private static Logger logger = LoggerFactory.getLogger(PositionInfoBiz.class);

    /**
     * 一级分类列表
     *
     * @param firstType
     */
    public List<String> getFirstType(String firstType) {
        return positionInfoMapper.getFirstType(firstType);
    }

    /**
     * 二级分类列表
     *
     * @param firstType
     * @param secondType
     */
    public List<String> getSecondType(String firstType, String secondType) {
        return positionInfoMapper.getSecondType(firstType, secondType);
    }

    /**
     * 具体岗位列表
     *
     * @param firstType
     * @param secondType
     */
    public List<Map<String, Object>> getPositions(String firstType, String secondType, String position) {
        return positionInfoMapper.getPositions(firstType, secondType, position);
    }



}
