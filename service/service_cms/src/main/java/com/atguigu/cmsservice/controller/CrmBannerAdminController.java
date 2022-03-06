package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.ResultEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表（后台系统） 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/eduservice/crm-banner-admin")
//@CrossOrigin
public class CrmBannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{currentPage}/{limit}")
    public ResultEntity pageBanner(@PathVariable long currentPage, @PathVariable long limit) {
        Page<CrmBanner> crmBannerPage = new Page<>(currentPage, limit);
        crmBannerService.page(crmBannerPage, null);
        long total = crmBannerPage.getTotal();
        List<CrmBanner> crmBannerList = crmBannerPage.getRecords();
        return ResultEntity.ok().data("total", total).data("crmBannerList", crmBannerList);
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("deleteBanner/{id}")
    public ResultEntity deleteBanner(@PathVariable String id) {
        crmBannerService.removeById(id);
        return ResultEntity.ok();
    }

    @ApiOperation(value = "新增Banner")
    @PostMapping("addBanner")
    public ResultEntity addBanner(@RequestBody CrmBanner crmBanner) {
        crmBannerService.save(crmBanner);
        return ResultEntity.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("updateBanner")
    public ResultEntity updateBanner(@RequestBody CrmBanner crmBanner) {
        crmBannerService.updateById(crmBanner);
        return ResultEntity.ok();
    }
}

