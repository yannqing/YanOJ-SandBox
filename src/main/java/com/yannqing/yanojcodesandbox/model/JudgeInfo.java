package com.yannqing.yanojcodesandbox.model;

import lombok.Data;

/**
 * @description: 数据库对应字段的json类
 * @title: 判题信息
 * @author: yannqing
 * @create: 2024-07-21 15:59
 **/
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 消耗时间（ms）
     */
    private Long time;
}
