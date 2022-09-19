package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-16
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient client;
    
    // 统计某一天的注册人数，生成统计数据
    @Override
    public void registerCount(String day) {
        // 添加数据之前，先删除表中相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        int delete = baseMapper.delete(wrapper);
        System.out.println(delete);

        // 远程调用的到某一天的注册人数
        R result = client.countRegister(day);
        Integer count = (Integer) result.getData().get("countRegister");

        // 把获取到的数据添加到数据库中，统计分析表
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(count);  // 注册人数
        sta.setDateCalculated(day);
        sta.setCourseNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(sta);
    }

    // 图表显示，返回两部分数据，日期的json数组，数量的json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);

        // 前端要求返回json数组，对应后端list集合
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        // 遍历所有数据list集合，进行封装
        for (int i = 0; i < list.size(); i++) {
            StatisticsDaily daily = list.get(i);
            // 封装日期
            date_calculatedList.add(daily.getDateCalculated());

            // 封装数据
            switch (type) {
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        Map<String, Object> map = new HashMap();
        map.put("date_calculatedList", date_calculatedList);
        map.put("numDataList", numDataList);

        return map;
    }
}
