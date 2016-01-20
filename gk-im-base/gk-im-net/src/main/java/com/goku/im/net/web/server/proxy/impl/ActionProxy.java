package com.goku.im.net.web.server.proxy.impl;


import com.goku.im.net.web.server.proxy.ActionExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author moueimei
 */
public class ActionProxy {
    private final static ActionProxy PROXY = new ActionProxy();

    private ActionProxy() {
    }

    public static ActionProxy getInstance() {
        return PROXY;
    }

    public ActionExecutor getProxy(ActionExecutor executor) {
        InvocationHandler handler = new ActionInvocationHandler(executor);
        Class<?> cls = executor.getClass();
        ClassLoader loader = cls.getClassLoader();
        Class<?>[] interfaces = cls.getInterfaces();
        if (null == interfaces || interfaces.length == 0) {
            Class<?> superCls = cls.getSuperclass();
            if (null != superCls)
                interfaces = superCls.getInterfaces();
        }
        return (ActionExecutor) Proxy.newProxyInstance(loader, interfaces, handler);
    }
}