package com.yannqing.yanojcodesandbox;

import com.yannqing.yanojcodesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * @description: java 原生代码沙箱（直接复用模板方法）
 * @author: yannqing
 * @create: 2024-08-23 21:26
 * @from: <更多资料：yannqing.com>
 **/
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
