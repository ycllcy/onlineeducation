package com.atguigu.cmsservice.service;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-11
 */
public interface CrmBannerService extends IService<CrmBanner> {
    // 查询所有Banner
    List<CrmBanner> selectAllBanner();

}
