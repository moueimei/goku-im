package com.goku.im.net.web.server.proxy;

import java.util.Map;

/**
 * @author moueimei
 */
public interface ActionLoader {
    public Map<String, ActionExecutor> load(String path) throws Exception;
}