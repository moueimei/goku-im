package com.goku.im.relation.util;

import com.goku.im.relation.redis.ImUserRedis;
import com.goku.user.model.GkUser;
import com.goku.user.service.GkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * Created by moueimei on 15/12/4.
 */
@Component
public class UserManager {
    @Autowired
    GkUserService gkUserService;
    @Autowired
    ImUserRedis imUserRedis;

    public GkUser getInfo(int userId) throws Exception {
        ///先从redis中获取
        GkUser gkUser = imUserRedis.getUserInfo(userId);
        if (null == gkUser) {
            ///调用dubbo接口获取
            gkUser = gkUserService.findUserByPUId(userId);
            if (null != gkUser) {
                ///保存到redis
                imUserRedis.setUserInfo(gkUser);
                return gkUser;
            }
        }
        return gkUser;
    }
}