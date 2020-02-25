package com.siti.enterprise.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siti.config.EasyCache;
import com.siti.enterprise.mapper.PositionReleaseMapper;
import com.siti.enterprise.po.PositionRelease;
import com.siti.enterprise.vo.PositionReleaseVo;
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
    public int insertList(List<PositionRelease> info) {
        try {
            int flag = 0;
            if (info.size() != 0 && info != null) {
                for (PositionRelease release : info) {
                    if (release.getEntId() != 0) {
                        int i = positionReleaseMapper.insert(release);
                        flag =flag+i;
                    }
                }
                return flag;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 删除
     */
    public void deleteById(int id) {
        positionReleaseMapper.deleteById(id);
    }


    public PageInfo<PositionReleaseVo> getPositionRelease(Integer page, Integer pageSize, Integer entId, Integer postId, String benefit, Integer salary,String city,String content) {
        PageHelper.startPage(page, pageSize);
       /* List<PositionRelease> enterprise = new ArrayList<>();
        enterprise = (List<PositionRelease>) easyCache
                .putAndGetSupplier("hospitallistType" + entId + postId,
                        "redissonkeyPositionRelease", () -> {
                            List<PositionRelease> list = positionReleaseMapper.getPositionRelease(entId, postId);
                            return list;
                        });*/
        Integer maxSalary = null;
        Integer minSalary = null;
        if(salary!=null)
        switch (salary) {
            case 1:
                minSalary = 0;
                maxSalary = 1000;
                break;
            case 2:
                minSalary = 1000;
                maxSalary = 2000;
                break;
            case 3:
                minSalary = 2000;
                maxSalary = 3000;
                break;
            case 4:
                minSalary = 3000;
                maxSalary = 5000;
                break;
            case 5:
                minSalary = 5000;
                maxSalary = 8000;
                break;
            case 6:
                minSalary = 8000;
                maxSalary = 12000;
                break;
            case 7:
                minSalary = 12000;
                maxSalary = 20000;
                break;
            case 8:
                minSalary = 20000;
                maxSalary = null;
                break;
            case 0:
                minSalary = null;
                maxSalary = null;
                break;
        }
        String[] benefits;
        if (benefit != null && benefit != "") {
            benefits = benefit.split(",");
        } else {
            benefits = null;
        }
        List<PositionReleaseVo> enterprise = positionReleaseMapper.getPosition(entId, postId, benefits, minSalary, maxSalary,city,content);
        enterprise.forEach(data->{
            if(data.getMaxSalary()!=0&&data.getMinSalary()!=0) {
                data.setSalary(data.getMinSalary() + "-" + data.getMaxSalary());
            }else if(data.getMaxSalary()==0&&data.getMinSalary()!=0){
                data.setSalary("20000及以上");
            }else if(data.getMaxSalary()!=0&&data.getMinSalary()==0){
                data.setSalary("0-1000");
            }else{
                data.setSalary("面议");
            }
        });
        return new PageInfo<>(enterprise);
    }

    public int update(PositionRelease positionRelease) {
        return positionReleaseMapper.updateByPrimaryKeySelective(positionRelease);
    }
}
