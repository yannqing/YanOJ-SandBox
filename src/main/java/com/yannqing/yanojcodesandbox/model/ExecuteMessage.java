package com.yannqing.yanojcodesandbox.model;

import lombok.Data;

/**
 * @description: 执行程序信息
 * @author: yannqing
 * @create: 2024-08-15 15:20
 * @from: <更多资料：yannqing.com>
 **/
@Data
public class ExecuteMessage {
    private Integer exitCode;
    private String message;
    private String errorMessage;
    private Long memory;
    private long time;
}
