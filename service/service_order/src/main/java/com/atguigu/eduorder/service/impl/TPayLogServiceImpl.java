package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.entity.TPayLog;
import com.atguigu.eduorder.mapper.TPayLogMapper;
import com.atguigu.eduorder.service.TOrderService;
import com.atguigu.eduorder.service.TPayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-15
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {
    @Autowired
    private TOrderService orderService;

    // 生成二维码
    @Override
    public Map createNative(String orderNo) {
        try {
            // 根据订单号查询订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(wrapper);
            // 创建map，添加生成二维码所需要的参数
            Map map = new HashMap();
            map.put("appid", "wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());
            map.put("out_trade_no", orderNo);
            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url",
                    "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");

            // 发送httpclient请求，传递xml格式参数，微信提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(map, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            // 得到请求返回的结果
            // 返回的数据类型是xml类型
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            // 封装最终返回的map集合
            Map finalMap = new HashMap();
            finalMap.put("out_trade_no", orderNo);
            finalMap.put("course_id", order.getCourseId());
            finalMap.put("total_fee", order.getTotalFee());
            finalMap.put("result_code", resultMap.get("result_code"));  // 返回二维码操作状态
            finalMap.put("code_url", resultMap.get("code_url"));    // 返回二维码地址

            return finalMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "生成二维码失败");
        }
    }

    // 向支付表中添加支付记录，同时更新订单支付状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 从map中获取订单号
        String orderNo = map.get("out_trade_no");

        // 根据订单号查询订单信息
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);

        // 更新订单状态
        if (order.getStatus().intValue() == 1) {
            return;
        }

        order.setStatus(1);
        orderService.updateById(order);

        // 向订单表中添加记录
        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);
    }

    // 查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            // 封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 发送httpclient请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);

            client.post();

            // 得到请求返回的内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
