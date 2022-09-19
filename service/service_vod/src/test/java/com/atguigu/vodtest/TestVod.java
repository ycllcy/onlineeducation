package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

/**
 * @author LcY
 * @create 2022-09-09 21:38
 */
public class TestVod {
    public static void main(String[] args) throws Exception {
//        String accessKeyId = "LTAI5tHDVcM4DWyL9tPzQCST";
//        String accessKeySecret = "jcPRa39Sg4XpiteTMZYPitguQJrhKk";
//        String title = "douyin-upload by sdk";
//        String fileName = "C:/Users/LCY/Desktop/douyin.mp4";
//        // 上传视频的代码
//        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
//        request.setPartSize(2 * 1024 * 1024L);
//        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
//        request.setTaskNum(1);
//
//        UploadVideoImpl uploader = new UploadVideoImpl();
//        UploadVideoResponse response = uploader.uploadVideo(request);
//        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
//        if (response.isSuccess()) {
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//        } else {
//            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//            System.out.print("ErrorCode=" + response.getCode() + "\n");
//            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
//        }

        getPlayAuth();
    }

    public static void getPlayAuth() throws Exception {
        // 根据视频id获取视频播放凭证
        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tHDVcM4DWyL9tPzQCST", "jcPRa39Sg4XpiteTMZYPitguQJrhKk");

        // 创建获取视频播放凭证的request对象和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        // 向request对象中设置id
        request.setVideoId("c64af9cda8974fb7a998340be8305dde");

        // 调用初始化对象中的方法
        response = client.getAcsResponse(request);

        System.out.println("PlayAuth:" + response.getPlayAuth());
    }

    public static void getPlayUrl() throws Exception {
        // 1.根据视频ID获取视频播放地址
        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tHDVcM4DWyL9tPzQCST", "jcPRa39Sg4XpiteTMZYPitguQJrhKk");
        // 创建获取播放地址的request和response对象
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        // 向request对象里面设置视频id
        request.setVideoId("57f6042f737943858f7f82a76198d1c0");
        // 调用初始化对象里面的方法传递request，获取数据
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}
