package com.tiancheng.ms.common.aop;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private HashSet<String> ignoreApis;

    public HashSet<String> getIgnoreApis() {
        return ignoreApis;
    }

    public void setIgnoreApis(HashSet<String> ignoreApis) {
        this.ignoreApis = ignoreApis;
    }
}
