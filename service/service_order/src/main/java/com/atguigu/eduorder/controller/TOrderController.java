package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-15
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class TOrderController {
    @Autowired
    private TOrderService tOrderService;

    // 生成订单
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId,
                       HttpServletRequest request) {
        // 生成订单，返回订单号
        String orderNo = tOrderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId", orderNo);
    }

    // 根据订单号查询订单信息
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = tOrderService.getOne(wrapper);

        return R.ok().data("item", order);
    }

    // 根据用户id和课程id查询用户订单信息
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,
                               @PathVariable String memberId) {

        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);

        int count = tOrderService.count(wrapper);

        if (count > 0) { // 已经支付
            return true;
        }

        return false;   // 未支付
    }
}

