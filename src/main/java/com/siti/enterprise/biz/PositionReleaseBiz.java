package com.siti.enterprise.biz;

import com.siti.enterprise.mapper.PositionReleaseMapper;
import com.siti.enterprise.po.PositionRelease;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PositionReleaseBiz {

    @Resource
    private PositionReleaseMapper positionReleaseMapper;

    /**
     * 新增
     */
    public int insert(PositionRelease info) {
        return positionReleaseMapper.insert(info);
    }

    /**
     * 删除
     */
    public void deleteById(int id) {
        positionReleaseMapper.deleteById(id);
    }
}
