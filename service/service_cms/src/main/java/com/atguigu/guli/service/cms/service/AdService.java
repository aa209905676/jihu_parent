package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-09-18
 */
public interface AdService extends IService<Ad> {

    IPage<AdVo> selectPage(Long page, Long limit);

    Boolean removeAdImageById(String id);

    List<Ad> selectByAdTypeId(String adTypeId);
}
