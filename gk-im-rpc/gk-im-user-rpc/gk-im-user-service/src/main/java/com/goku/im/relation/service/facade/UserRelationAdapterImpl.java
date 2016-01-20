package com.goku.im.relation.service.facade;

import com.gkframework.model.response.ServiceResponse;
import com.gkframework.model.utils.JsonUtil;
import com.goku.im.relation.UserRelationCodeConst;
import com.goku.im.relation.model.GkImGroup;
import com.goku.im.relation.model.GkImGroupUser;
import com.goku.im.relation.model.GkImUserFriend;
import com.goku.im.relation.service.GkImGroupService;
import com.goku.im.relation.service.GkImGroupUserService;
import com.goku.im.relation.service.GkImUserFriendService;
import com.goku.im.relation.service.UserRelationAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moueimei on 15/12/8.
 */
@Service("userRelationAdapter")
public class UserRelationAdapterImpl implements UserRelationAdapter {

    @Autowired
    GkImGroupService gkImGroupService;
    @Autowired
    GkImGroupUserService gkImGroupUserService;
    @Autowired
    GkImUserFriendService gkImUserFriendService;


    @Override
    public String createGroup(int userId, String groupName, String icon, String intro) {
        Map<String, Object> map = gkImGroupService.create(userId, groupName, icon, intro);
        int code = (Integer)map.get("code");
        Map<String, Object> info = new HashMap<>();
        if(code == 0) {
            int groupId = (Integer) map.get("groupId");
            Date createTime = (Date) map.get("createTime");
            info.put("groupId", groupId);
            info.put("createTime", createTime);
        }
        return JsonUtil.object2Json(new ServiceResponse(null, code, info));
    }

    @Override
    public String editGroup(int userId, int groupId, String groupName, String icon, String intro) {
        int code = gkImGroupService.edit(userId, groupId, groupName, icon, intro);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String dismissGroup(int operatorId, int groupId) {
        int code = gkImGroupService.dismiss(operatorId, groupId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String getGroupInfo(int groupId) {
        GkImGroup gkImGroup = gkImGroupService.getInfo(groupId);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, gkImGroup));
    }

    @Override
    public String isGroupMaster(int userId, int groupId) {
        boolean isMaster = gkImGroupService.isMaster(userId, groupId);
        Map<String, Boolean> info = new HashMap<>();
        info.put("isMaster", isMaster);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String applyJoinGroup(int userId, int groupId) {
        int code = gkImGroupUserService.apply(userId, groupId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String replyJoinGroup(int operatorId, int userId, int groupId, int replyType) {
        int code = gkImGroupUserService.reply(operatorId, userId, groupId, replyType);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String deleteGroupUser(int operatorId, int userId, int groupId) {
        int code = gkImGroupUserService.delete(operatorId, userId, groupId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String quitGroup(int userId, int groupId) {
        int code = gkImGroupUserService.quit(userId, groupId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String inviteFriendJoinGroup(int operatorId, int userId, int groupId) {
        int code = gkImGroupUserService.invite(operatorId, userId, groupId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String isGroupUser(int userId, int groupId) {
        boolean isGroupUser = gkImGroupUserService.isGroupUser(userId, groupId);
        Map<String, Boolean> info = new HashMap<>();
        info.put("isGroupUser", isGroupUser);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String getGroupUsers(int groupId, int page, int size) {
        List<GkImGroupUser> userList = gkImGroupUserService.getGroupUsers(groupId, page, size);
        Map<String, List<GkImGroupUser>> info = new HashMap<>();
        info.put("groupUsers", userList);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String getGroupUserCount(int groupId) {
        int count = gkImGroupUserService.getGroupUserCount(groupId);
        Map<String, Integer> info = new HashMap<>();
        info.put("groupUserCount", count);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String getUserGroups(int userId) {
        List<GkImGroup> groupList = gkImGroupUserService.getUserGroups(userId);
        Map<String, List<GkImGroup>> info = new HashMap<>();
        info.put("groupList", groupList);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String applyFriend(int fromUserId, int toUserId) {
        int code = gkImUserFriendService.apply(fromUserId, toUserId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String replyFriend(int fromUserId, int toUserId, int replyType) {
        int code = gkImUserFriendService.reply(fromUserId, toUserId, replyType);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String deleteFriend(int userId, int friendUserId) {
        int code = gkImUserFriendService.delete(userId, friendUserId);
        return JsonUtil.object2Json(new ServiceResponse(code));
    }

    @Override
    public String isFriend(int userId, int friendId) {
        boolean isFriend = gkImUserFriendService.isFriend(userId, friendId);
        Map<String, Boolean> info = new HashMap<>();
        info.put("isFriend", isFriend);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String getFriends(int userId) {
        List<GkImUserFriend> friends = gkImUserFriendService.getFriends(userId);
        Map<String, List<GkImUserFriend>> info = new HashMap<>();
        info.put("friends", friends);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }

    @Override
    public String getFriendCount(int userId) {
        int count = gkImUserFriendService.getFriendCount(userId);
        Map<String, Integer> info = new HashMap<>();
        info.put("friendCount", count);
        return JsonUtil.object2Json(new ServiceResponse(null, UserRelationCodeConst.SUCCESS, info));
    }
}