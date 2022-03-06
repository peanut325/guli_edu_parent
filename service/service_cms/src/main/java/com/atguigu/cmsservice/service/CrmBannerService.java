package com.atguigu.cmsservice.service;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-23
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> selectBannerList();
}
