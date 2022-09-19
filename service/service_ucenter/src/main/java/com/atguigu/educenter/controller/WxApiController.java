package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;


/**
 * @author LcY
 * @create 2022-09-13 22:06
 */
@Controller // 这里没有配置@RestController
//@RestController
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;

    // 获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            // 获取code，临时票据
            // 拿着code去请求一个固定地址，得到access_token和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            // 拼接三个参数id 秘钥 code
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);

            // 使用httpclient请求accessTokenUrl，得到access_token和openId
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            // 从accessTokenInfo中取到access_token和openId
            // 使用Gson将json字符串解析为map
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);

            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            // 把扫码人信息添加到数据库中，如果数据库中已经有了，就不需要重复添加
            QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", openid);
            UcenterMember member = memberService.getOne(wrapper);

            if (member == null) {
                // 拿着access_token和openid去请求一个固定地址，得到扫码人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

                String userInfo = HttpClientUtils.get(userInfoUrl);

                // 获取userInfo中扫码人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");

                member = new UcenterMember();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);

                memberService.save(member);
            }

            // 使用jwt根据用户信息生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            // 返回首页面
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e) {
            throw new GuliException(20001, "登陆失败");
        }
    }

    // 生成微信二维码
    @GetMapping("login")
    public String getWxCode() {
        // 固定地址，后面拼接参数
//        String url = "https://open.weixin.qq.com/connect/qrconnect?"
//                        + "appid=" + ConstantWxUtils.WX_OPEN_APP_ID + "&"
//                        + "redirect_uri=" + ConstantWxUtils.WX_OPEN_REDIRECT_URL + "&"
//                        + "response_type=code";
        // 微信开放平台授权baseUrl %s相当于占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 对redirect_url进行urlEncode编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        // 重定向到请求微信地址
        return "redirect:" + url;
    }
}
