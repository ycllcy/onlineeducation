package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author LcY
 * @create 2022-09-12 21:32
 */
@Service
public class MsmServiceImpl implements MsmService {
    // 发送短信的方法
    @Override
    public boolean send(HashMap<String, Object> param, String phone) {
        if (StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", your-access-key-id, your-access-key-secret);
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);

        // 设置固定的相关参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        // 设置发送相关参数
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "阿里云短信测试");
        request.putQueryParameter("TemplateCode", "SMS_154950909");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            // 最终发送
            CommonResponse response = client.getCommonResponse(request);
            return response.getHttpResponse().isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
