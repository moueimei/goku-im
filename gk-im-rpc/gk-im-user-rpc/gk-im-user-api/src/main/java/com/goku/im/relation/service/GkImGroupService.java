package com.goku.im.relation.service;


import com.goku.im.relation.model.GkImGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by moueimei on 15/12/1.
 */
public interface GkImGroupService
{
    /**
     * 创建群组
     * @param userId 创建者ID
     * @param groupName 群组名称
     * @param icon 群组封面
     * @param intro 群组介绍
     * @return
     */
    Map<String, Object> create(int userId, String groupName, String icon, String intro);

    /**
     *
     * @param userId 创建者ID
     * @param groupId 群组ID
     * @param groupName 群组名称
     * @param icon 群组封面
     * @param intro 群组介绍
     * @return
     */
    int edit(int userId, int groupId, String groupName, String icon, String intro);

    /**
     * 解散群组
     * @param operatorId 操作用户ID
     * @param groupId 群组ID
     * @return
     */
    int dismiss(int operatorId, int groupId);

    /**
     * 获取群组信息
     * @param groupId 群组ID
     * @return
     */
    GkImGroup getInfo(int groupId);

    /**
     * 判定用户是否为群主
     * @param userId 用户ID
     * @param groupId 群组ID
     * @return
     */
    boolean isMaster(int userId, int groupId);

    /**
     * 根据群组ID列表获取群组列表
     * @param groupIds 群组ID列表
     * @return
     */
    List<GkImGroup> getGroupsByIds(List<Integer> groupIds);
}