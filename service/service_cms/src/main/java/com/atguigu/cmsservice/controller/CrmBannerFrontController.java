package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表（前台系统） 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/cmsservice/crm-banner-front")
//@CrossOrigin
public class CrmBannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    @GetMapping("getAllBanner")
    public ResultEntity getAllBanner(){
        List<CrmBanner> crmBannerList = crmBannerService.selectBannerList();
        return ResultEntity.ok().data("crmBannerList" , crmBannerList);
    }

}

