package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-15
 */
public interface TPayLogService extends IService<TPayLog> {
    // 生成二维码
    Map createNative(String orderNo);

    // 向支付表中添加支付记录，同时更新订单支付状态
    void updateOrderStatus(Map<String, String> map);

    // 根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
