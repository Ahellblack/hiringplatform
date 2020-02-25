package com.siti.enterprise.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.common.ReturnResult;
import com.siti.enterprise.biz.PositionReleaseBiz;
import com.siti.enterprise.po.EnterpriseInfo;
import com.siti.enterprise.po.PositionRelease;
import com.siti.enterprise.vo.PositionReleaseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("release")
public class PositionReleaseCtrl {

    @Resource
    PositionReleaseBiz positionReleaseBiz;

    /**
     * @param  entId
     * @param  postId
     * @param  benefit
     * @param  salary
     * 
     *
     * */
    @GetMapping("getPosition")
    public ReturnResult getPositionRelease(Integer page, Integer pageSize, Integer entId,Integer postId,String benefit,Integer salary ,String city,String content ){
        try {
            PageInfo<PositionReleaseVo> positionRelease = positionReleaseBiz.getPositionRelease(page, pageSize, entId,postId,benefit,salary,city,content);
            return new ReturnResult(1,"查询成功",positionRelease);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

    /**
     * @param  positionRelease
     * */
    @PostMapping("insert")
    public ReturnResult insert(@RequestBody PositionRelease positionRelease){
        try {
            int flag = positionReleaseBiz.insert(positionRelease);
            if(flag==1){
                return new ReturnResult(1,"添加成功");
            }else{
                return new ReturnResult(0,"添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

    /**
     * @param  positionRelease
     * */
    @PostMapping("update")
    public ReturnResult update(@RequestBody PositionRelease positionRelease){
        try {
            int flag = positionReleaseBiz.update(positionRelease);
            if(flag==1){
                return new ReturnResult(1,"修改成功");
            }else{
                return new ReturnResult(0,"修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }


    /**
     * @param  positionReleases
     * */
    @PostMapping("insertList")
    public ReturnResult insert(@RequestBody List<PositionRelease> positionReleases){
        try {
            int flag = positionReleaseBiz.insertList(positionReleases);
            if(flag>=1){
                return new ReturnResult(1,"添加成功");
            }else {
                return new ReturnResult(0,"添加失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

}
