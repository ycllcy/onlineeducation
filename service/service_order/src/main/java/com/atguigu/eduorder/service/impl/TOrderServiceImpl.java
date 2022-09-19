package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.mapper.TOrderMapper;
import com.atguigu.eduorder.service.TOrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-15
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 创建订单，返回订单号
    @Override
    public String createOrders(String courseId, String memberId) {
        // 远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);
        // 远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        TOrder tOrder = new TOrder();
        tOrder.setOrderNo(OrderNoUtil.getOrderNo());
        tOrder.setCourseId(courseId);
        tOrder.setCourseTitle(courseInfoOrder.getTitle());
        tOrder.setCourseCover(courseInfoOrder.getCover());
        tOrder.setTeacherName(courseInfoOrder.getTeacherName());
        tOrder.setMemberId(memberId);
        tOrder.setNickname(userInfoOrder.getNickname());
        tOrder.setMobile(userInfoOrder.getMobile());
        tOrder.setTotalFee(courseInfoOrder.getPrice());
        tOrder.setPayType(1);
        tOrder.setStatus(0);

        baseMapper.insert(tOrder);

        return tOrder.getOrderNo();
    }
}
