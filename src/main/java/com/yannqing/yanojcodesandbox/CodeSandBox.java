package com.yannqing.yanojcodesandbox;

import com.yannqing.yanojcodesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeResponse;

/**
 * @description: 代码沙箱接口
 * @author: yannqing
 * @create: 2024-08-06 17:16
 **/
public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
