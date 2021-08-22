package com.company.app;

import com.company.app.common.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AfterApplicationStart implements ApplicationRunner {

    @Value("${my-param.security.open-dev-controller}")
    private Boolean openDevController;

    @Autowired
    private LogUtil logUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logUtil.info(this, "风险控制器 [DevController] 配置状态 : " + (openDevController ? "开启" : "关闭"));
    }
}
