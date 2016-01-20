package com.goku.im.net.web.server.proxy.impl;


import com.goku.im.net.web.server.proxy.ActionLoader;

/**
 * @author moueimei
 */
public class ActionLoaderFactory {
    public static ActionLoader getActionLoader() {
        return new AnnotationActionLoader();
    }
}