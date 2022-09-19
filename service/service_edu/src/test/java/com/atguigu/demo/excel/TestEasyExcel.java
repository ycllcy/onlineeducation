package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LcY
 * @create 2022-09-03 22:08
 */
public class TestEasyExcel {
    public static void main(String[] args) {
        // 实现excel写操作
        // 1、设置写入文件夹地址和excel文件名称
//        String filename = "D:\\write.xlsx";

        // 2、调用EasyExcel中的方法实现写操作
//        EasyExcel.write(filename, DemoData.class)
//                .sheet("学生列表")
//                .doWrite(getData());

        // 实现excel读操作
        String filename = "D:\\write.xlsx";

        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead(); // 通过监听其中的invoke方法对每行数据进行操作
    }

    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy" + i);
            list.add(demoData);
        }
        return list;
    }
}
