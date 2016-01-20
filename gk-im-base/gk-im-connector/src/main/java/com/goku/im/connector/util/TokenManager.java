package com.goku.im.connector.util;


import com.goku.im.framework.util.StringUtil;

/**
 * Created by moueimei on 15/11/29.
 */
public class TokenManager {
    /**
     * 从用户token中获取用户ID
     *
     * @param userToken
     * @return
     */
    public static int getUserIdByUserToken(String userToken) {
        String plainText = EncryptUtil.decryptString(userToken);
        if (StringUtil.isNullOrEmpty(plainText))
            return 0;

        String[] items = plainText.split(",");
        if (items.length != 5)
            return 0;

        String strUserId = items[2];

        int userId = 0;
        if (StringUtil.isInteger(strUserId))
            userId = Integer.parseInt(strUserId);

        return userId;
    }
}