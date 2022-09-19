package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.TPayLog;
import com.atguigu.eduorder.service.TPayLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-15
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class TPayLogController {
    @Autowired
    private TPayLogService payLogService;

    // 生成微信支付二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        // 返回信息，包括二维码地址，还有一些其他信息
        Map map = payLogService.createNative(orderNo);

        return R.ok().data(map);
    }

    // 根据订单号查询订单支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
         Map<String, String> map = payLogService.queryPayStatus(orderNo);
         if (map == null) {
             return R.error().message("支付出错了");
         }

         // 如果订单不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")) {
            // 向支付表中加记录，并更新订单表的状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }
}

