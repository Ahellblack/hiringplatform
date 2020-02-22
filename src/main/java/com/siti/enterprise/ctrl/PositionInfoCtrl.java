package com.siti.enterprise.ctrl;

import com.siti.common.ReturnResult;
import com.siti.enterprise.biz.PositionInfoBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("position")
public class PositionInfoCtrl {

    @Resource
    private PositionInfoBiz positionInfoBiz;

    private static Logger logger = LoggerFactory.getLogger(PositionInfoCtrl.class);

    // 一级分类
    @GetMapping("getFirstType")
    public ReturnResult getFirstType (@RequestParam(name = "firstType", required = false) String firstType) {
        try {
            List<String> list = positionInfoBiz.getFirstType(firstType);
            return new ReturnResult(1,"查询成功", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ReturnResult(-1, "查询失败");
        }
    }

    // 二级分类
    @GetMapping("getSecondType")
    public ReturnResult getSecondType (String firstType, @RequestParam(name = "secondType", required = false) String secondType) {
        try {
            List<String> list = positionInfoBiz.getSecondType(firstType, secondType);
            return new ReturnResult(1,"查询成功", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ReturnResult(-1, "查询失败");
        }
    }

    // 具体岗位列表
    @GetMapping("getJobs")
    public ReturnResult getPositions (String firstType, String secondType, @RequestParam(name = "position", required = false) String position) {
        try {
            List<Map<String, Object>> list = positionInfoBiz.getPositions(firstType, secondType, position);
            return new ReturnResult(1,"查询成功", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ReturnResult(-1, "查询失败");
        }
    }
}
