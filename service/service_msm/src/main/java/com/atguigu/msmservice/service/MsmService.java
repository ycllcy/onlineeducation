package com.atguigu.msmservice.service;

import java.util.HashMap;

/**
 * @author LcY
 * @create 2022-09-12 21:32
 */
public interface MsmService {
    // 发送短信的方法
    boolean send(HashMap<String, Object> param, String phone);
}
