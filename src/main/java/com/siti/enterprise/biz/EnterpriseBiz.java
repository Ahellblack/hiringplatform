package com.siti.enterprise.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siti.config.EasyCache;
import com.siti.enterprise.mapper.*;
import com.siti.enterprise.po.EnterpriseInfo;
import com.siti.system.po.User;
import com.siti.utils.GetLatAndLngByGaode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class EnterpriseBiz {
    @Resource
    EnterpriseMapper enterpriseMapper;


    @Resource
    private EasyCache easyCache;

    private static Logger logger = LoggerFactory.getLogger(EnterpriseBiz.class);

    /**
     * 企业信息搜素
     */
    public PageInfo<EnterpriseInfo> getEnterprise(Integer page, Integer pageSize, String enterpriseName) {
        try {
            PageHelper.startPage(page, pageSize);
            List<EnterpriseInfo> enterprise = new ArrayList<>();
            enterprise = (List<EnterpriseInfo>) easyCache
                    .putAndGetSupplier("hospitallistType" + enterpriseName,
                            "redissonkeyEnterpriseInfo", () -> {
                                List<EnterpriseInfo> list = enterpriseMapper.getEnterprise(enterpriseName);
                                return list;
                            });
            return new PageInfo<>(enterprise);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 新增企业
     *
     * @param enterpriseInfo
     */
    public int insert(EnterpriseInfo enterpriseInfo) {
        EnterpriseInfo info = enterpriseMapper.getEnterpriseByKey(enterpriseInfo.getEntFullname(), enterpriseInfo.getTel());
        // 地址转换高德经纬度坐标
        try {
            Map<String, Double> lngLat = GetLatAndLngByGaode.getLngAndLat(enterpriseInfo.getProvince()
                    + enterpriseInfo.getCity() + enterpriseInfo.getEntAddress(), "b413b3183893f11ebc39ed32450112ae");
            if (!lngLat.isEmpty()) {
                enterpriseInfo.setLng(lngLat.get("lng"));
                enterpriseInfo.setLat(lngLat.get("lat"));
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        if (info != null) { // 企业已录入过信息,返回2
            if (info.getIsVerify() != 1) { // 企业已录入,但未认证,做更新操作
                try {
                    enterpriseInfo.setEnterTime(info.getEnterTime());
                    enterpriseMapper.updateEnterpriseByPO(enterpriseInfo);
//                    enterpriseMapper.updateByPrimaryKeySelective(enterpriseInfo);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
            return 2;
        } else { // 企业没有录入过信息，新增
            enterpriseInfo.setIsVerify(0);
            // 插入企业信息
            return enterpriseMapper.insert(enterpriseInfo);
        }
    }

    public int delete(int id) {
        return enterpriseMapper.deleteByPrimaryKey(id);
    }

    public int update(EnterpriseInfo enterpriseInfo) {
        return enterpriseMapper.updateByPrimaryKeySelective(enterpriseInfo);
    }

}
