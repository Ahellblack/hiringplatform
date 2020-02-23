package com.siti.enterprise.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.common.ReturnResult;
import com.siti.enterprise.biz.PositionReleaseBiz;
import com.siti.enterprise.po.EnterpriseInfo;
import com.siti.enterprise.po.PositionRelease;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("release")
public class PositionReleaseCtrl {

    @Resource
    PositionReleaseBiz positionReleaseBiz;

    /**
     * @Param  entId
     * */
    @GetMapping("getPosition")
    public ReturnResult getPositionRelease(Integer page, Integer pageSize, Integer entId,Integer postId){
        try {
            PageInfo<PositionRelease> positionRelease = positionReleaseBiz.getPositionRelease(page, pageSize, entId,postId);
            return new ReturnResult(1,"查询成功",positionRelease);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

    /**
     * @Param  entId
     * */
    @GetMapping("insert")
    public ReturnResult insert(@RequestBody PositionRelease positionRelease){
        try {
            int flag = positionReleaseBiz.insert(positionRelease);
            return new ReturnResult(1,"添加成功",positionRelease);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }


    /**
     * @Param  entId
     * */
    @GetMapping("insert")
    public ReturnResult insert(@RequestBody List<PositionRelease> positionReleases){
        try {
            List<PositionRelease> returnPositions = positionReleaseBiz.insertList(positionReleases);
            return new ReturnResult(1,"添加成功",returnPositions);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

}
