package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author LcY
 * @create 2022-09-16 21:11
 */
@FeignClient(name = "service-order")
@Component
public interface OrdersClient {
    // 根据用户id和课程id查询用户订单信息
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable(value = "courseId") String courseId, @PathVariable(value = "memberId") String memberId);
}
