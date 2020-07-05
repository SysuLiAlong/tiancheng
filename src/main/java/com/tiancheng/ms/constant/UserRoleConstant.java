package com.tiancheng.ms.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "role")
@Component
@Data
public class UserRoleConstant {
    public String superAdmin;

    public String superAdminName;

    public String admin;

    public String adminName;

    public String commonUser;

    public String commonUserName;
}
