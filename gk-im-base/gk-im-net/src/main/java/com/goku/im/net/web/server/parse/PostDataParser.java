package com.goku.im.net.web.server.parse;

import java.util.Map;

/**
 * @author moueimei
 */
public interface PostDataParser {
    Map<String, String> parse(String postData) throws Exception;
}