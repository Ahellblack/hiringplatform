package com.siti.enterprise.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siti.config.EasyCache;
import com.siti.enterprise.mapper.PositionReleaseMapper;
import com.siti.enterprise.po.PositionRelease;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionReleaseBiz {

    @Resource
    private PositionReleaseMapper positionReleaseMapper;

    @Resource
    private EasyCache easyCache;

    /**
     * 新增
     */
    public int insert(PositionRelease info) {
        return positionReleaseMapper.insert(info);
    }

    /**
     * 新增
     */
    public List<PositionRelease> insertList( List<PositionRelease> info) {
        try {
            if(info.size()!=0 && info!=null) {
                for (PositionRelease release : info) {
                    if (release.getEntId() != 0) {
                        positionReleaseMapper.insert(release);
                    }
                }
                return positionReleaseMapper.getPositionRelease((int) info.get(0).getEntId(), null);
            }
            return new ArrayList<>();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    /**
     * 删除
     */
    public void deleteById(int id) {
        positionReleaseMapper.deleteById(id);
    }


    public PageInfo<PositionRelease> getPositionRelease(Integer page, Integer pageSize, Integer entId, Integer postId) {
        PageHelper.startPage(page, pageSize);
       /* List<PositionRelease> enterprise = new ArrayList<>();
        enterprise = (List<PositionRelease>) easyCache
                .putAndGetSupplier("hospitallistType" + entId + postId,
                        "redissonkeyPositionRelease", () -> {
                            List<PositionRelease> list = positionReleaseMapper.getPositionRelease(entId, postId);
                            return list;
                        });*/
        List<PositionRelease> enterprise = positionReleaseMapper.getPositionRelease(entId, postId);
        return new PageInfo<>(enterprise);
    }

}
