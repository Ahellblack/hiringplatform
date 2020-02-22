package com.siti.enterprise.ctrl;

import com.siti.enterprise.biz.PositionReleaseBiz;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("release")
public class PositionReleaseCtrl {

    @Resource
    PositionReleaseBiz positionReleaseBiz;

}
