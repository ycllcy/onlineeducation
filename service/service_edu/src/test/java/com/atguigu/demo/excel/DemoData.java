package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author LcY
 * @create 2022-09-03 22:04
 */
@Data
public class DemoData {
    @ExcelProperty(value = "学生编号", index = 0)
    private Integer sno;
    @ExcelProperty(value = "学生姓名", index = 1)
    private String sname;
}
