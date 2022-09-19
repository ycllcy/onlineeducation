package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-13
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 登录方法
    @Override
    public String login(UcenterMember member) {
        // 获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        // 手机号和密码的非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败");
        }

        // 判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if (mobileMember == null) {
            throw new GuliException(20001, "登陆失败");
        }

        // 判断密码
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new GuliException(20001, "登陆失败");
        }

        // 判断用户是否被禁用
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "登陆失败");
        }

        // 登陆成功
        // 使用jwt工具类生成token字符串
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册方法
    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String nickname = registerVo.getNickname();

        if (StringUtils.isEmpty(mobile) ||
            StringUtils.isEmpty(code) ||
            StringUtils.isEmpty(password) ||
            StringUtils.isEmpty(nickname)) {
            throw new GuliException(20001, "注册失败");
        }

        // 判断验证码
        // 获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败");
        }

        // 判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);

        if (count > 0) {
            throw new GuliException(20001, "注册失败");
        }

        // 把数据存入数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setIsDisabled(false); // 用户不禁用
        ucenterMember.setAvatar("https://edu-gulixueyuan-lcy.oss-cn-beijing.aliyuncs.com/2022/09/03/0063197d6f974943946da9074693a03creboot.jpg");
        baseMapper.insert(ucenterMember);


    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
