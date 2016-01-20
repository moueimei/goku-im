package com.goku.im.relation.push;

import com.goku.im.pushservice.PushServiceNotifyTypeConst;
import com.goku.im.pushservice.model.Notify;
import com.goku.im.pushservice.service.GkImNotifyService;
import com.goku.im.relation.service.GkImGroupService;
import com.goku.im.relation.service.GkImGroupUserService;
import com.goku.im.relation.util.UserManager;
import com.goku.user.model.GkUser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by moueimei on 15/12/4.
 * 调用push service的dubbo服务
 * 参数为json,格式如下:
 * {
 * "fromUserId" : 10001,
 * "toUserId" : 10002,
 * "content" : {
 * "alert" : "通知栏标题",
 * "groupId" : 0,
 * "replyType" : 1
 * ......
 * },
 * "notifyType" : "friend_apply",
 * "createTime" : 139883423423
 * }
 */
@Component
public class AsyncNotifyPusher implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AsyncNotifyPusher.class);

    private final static ExecutorService PUSH_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    @Autowired
    UserManager userManager;
    @Autowired
    GkImGroupService gkImGroupService;
    @Autowired
    GkImGroupUserService gkImGroupUserService;
    @Autowired
    GkImNotifyService gkImNotifyService;

    /**
     * 发送申请加为好友通知
     *
     * @param notify
     */
    public void pushFriendApply(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.FRIEND_APPLY);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送申请加为好友回复通知
     *
     * @param notify
     */
    public void pushFriendReply(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.FRIEND_REPLY);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送删除好友通知
     *
     * @param notify
     */
    public void pushFriendDelete(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.FRIEND_DELETE);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送申请加入群组通知
     *
     * @param notify
     */
    public void pushGroupUserApply(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_USER_APPLY);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送申请加入群组回复通知
     *
     * @param notify
     */
    public void pushGroupUserReply(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_USER_REPLY);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送添加用户入群通知
     *
     * @param notify
     */
    public void pushGroupUserAdd(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_USER_ADD);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送用户退群通知
     *
     * @param notify
     */
    public void pushGroupUserQuit(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_USER_QUIT);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送群组解散通知
     *
     * @param notify
     */
    public void pushGroupDismiss(Notify notify, Set<String> groupUserIds) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_DISMISS);
                List<Integer> toUserIds = new ArrayList<>();
                groupUserIds.forEach(userId -> toUserIds.add(Integer.parseInt(userId)));
                notify.setToUserIds(toUserIds);

                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送删除群组成员通知
     *
     * @param notify
     */
    public void pushGroupUserDelete(Notify notify) {
        Runnable task = () ->
        {
            try {
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_USER_DELETE);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    /**
     * 发送新用户入群通知
     *
     * @param notify
     */
    public void pushGroupNewUserAdd(Notify notify, List<Integer> exceptList) {
        Runnable task = () ->
        {
            try {
                int groupId = 0;
                JSONObject content = new JSONObject(notify.getContent());
                if (null != content) {
                    groupId = content.optInt("groupId", 0);
                    JSONObject jsonAddUser = content.optJSONObject("addUser");
                    if (null != jsonAddUser) {
                        int userId = jsonAddUser.optInt("puId", 0);
                        if (userId > 0) {
                            GkUser gkUser = userManager.getInfo(userId);
                            if (null != gkUser) {
                                jsonAddUser.put("nick", gkUser.getNick());
                                jsonAddUser.put("photo", gkUser.getPhoto());
                                jsonAddUser.put("sex", gkUser.getSex());
                            }
                        }
                    }
                }

                List<Integer> userIds = gkImGroupUserService.getGroupUserIds(groupId);
                if (null == userIds || userIds.size() == 0)
                    return;

                userIds.removeAll(exceptList);
                notify.setToUserIds(userIds);
                notify.setNotifyType(PushServiceNotifyTypeConst.GROUP_NEW_USER_ADD);
                ///调用dubbo服务接口
                gkImNotifyService.push(notify);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        };
        PUSH_THREAD_POOL.execute(task);
    }

    @Override
    public void destroy() throws Exception {
        if( !PUSH_THREAD_POOL.isShutdown()){
            PUSH_THREAD_POOL.shutdown();
        }
    }
}