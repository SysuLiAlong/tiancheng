package com.tiancheng.ms.common.context;

public class ContextHolder {
    public static ThreadLocal<ContextUser> userThreadLocal = new ThreadLocal();

    public static ContextUser getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(ContextUser user) {
        userThreadLocal.set(user);
    }
}

