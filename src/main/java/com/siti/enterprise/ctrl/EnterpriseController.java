package com.siti.enterprise.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.common.ReturnResult;
import com.siti.enterprise.biz.EnterpriseBiz;

import com.siti.enterprise.po.EnterpriseInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("enterprise")
public class EnterpriseController {

    @Resource
    EnterpriseBiz enterpriseBiz;

    /**
     * @Param  enterpriseName
     * */
    @GetMapping("getEnterprise")
    public ReturnResult getEnterprise(Integer page,Integer pageSize,String enterpriseName){
        try {
            PageInfo<EnterpriseInfo> material = enterpriseBiz.getEnterprise(page, pageSize, enterpriseName);
            return new ReturnResult(1,"查询成功",material);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

    @PostMapping("insert")
    public ReturnResult insert(@RequestBody EnterpriseInfo enterpriseInfo){
        try {
            int flag = enterpriseBiz.insert(enterpriseInfo);
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

    @GetMapping("delete")
    public ReturnResult delete(int id){
        try {
            int flag = enterpriseBiz.delete(id);
            if(flag==1){
                return new ReturnResult(1,"删除成功");
            }else{
                return new ReturnResult(0,"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(-1,"异常错误");
        }
    }

    @PostMapping("update")
    public ReturnResult update(@RequestBody EnterpriseInfo enterpriseInfo){
        try {
            int flag = enterpriseBiz.update(enterpriseInfo);
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


}
