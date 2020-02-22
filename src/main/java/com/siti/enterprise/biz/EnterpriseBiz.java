package com.siti.enterprise.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siti.config.EasyCache;
import com.siti.enterprise.mapper.*;
import com.siti.enterprise.po.EnterpriseInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;


@Service
public class EnterpriseBiz {
    @Resource
    EnterpriseMapper enterpriseMapper;


    @Resource
    private EasyCache easyCache;

    /**
     * 企业信息搜素
     * */
    public PageInfo<EnterpriseInfo> getEnterprise(Integer page, Integer pageSize, String enterpriseName) {
        try {
            PageHelper.startPage(page,pageSize);
            List<EnterpriseInfo> enterprise = new ArrayList<>();
            enterprise = (List<EnterpriseInfo>) easyCache
                    .putAndGetSupplier("hospitallistType"+enterpriseName,
                            "redissonkeyEnterpriseInfo", () -> {
                                List<EnterpriseInfo> list =enterpriseMapper.getEnterprise(enterpriseName);
                                return list;
                            });
            return new PageInfo<>(enterprise);
        }catch (Exception e){
            return null;
        }
    }

    public int insert(EnterpriseInfo enterpriseInfo) {

        List<EnterpriseInfo> list =enterpriseMapper.getEnterprise(enterpriseInfo.getEntName());
            return enterpriseMapper.insert(enterpriseInfo);
    }

    public int delete(int id) {
            return enterpriseMapper.deleteByPrimaryKey(id);
    }

    public int update(EnterpriseInfo enterpriseInfo) {
            return enterpriseMapper.updateByPrimaryKeySelective(enterpriseInfo);
    }
}
