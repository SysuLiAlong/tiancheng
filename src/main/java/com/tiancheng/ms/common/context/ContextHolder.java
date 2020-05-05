package com.tiancheng.ms.common.context;

/**
 * @author gongyi
 * Email; 76429197@qq.com
 * Date: 2020-02-09
 */
public class ContextHolder {
    public static ThreadLocal<ContextUser> userThreadLocal = new ThreadLocal();

    public static ContextUser getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(ContextUser user) {
        userThreadLocal.set(user);
    }
}

