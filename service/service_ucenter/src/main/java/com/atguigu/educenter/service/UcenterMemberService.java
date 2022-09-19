package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-13
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    // 登录方法
    String login(UcenterMember member);

    // 注册方法
    void register(RegisterVo registerVo);

    // 查询某一天的注册人数
    Integer countRegisterDay(String day);
}
