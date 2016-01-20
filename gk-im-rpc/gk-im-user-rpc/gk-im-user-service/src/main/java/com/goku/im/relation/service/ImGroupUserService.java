package com.goku.im.relation.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.entity.ImGroupUser;

import java.util.List;

public interface ImGroupUserService {
    int countByQuery(Query query) throws ServiceException;

    ImGroupUser selectByPrimaryKey(Integer id) throws ServiceException;

    List<ImGroupUser> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(ImGroupUser record) throws ServiceException;

    int updateByPrimaryKey(ImGroupUser record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(ImGroupUser record, Query query) throws ServiceException;

    int updateByMap(ImGroupUser record, Query query) throws ServiceException;

    int insertSelective(ImGroupUser record) throws ServiceException;

    /**
     * 根据群组ID获取用户ID列表(获取群组成员Id列表)
     * @param query
     * @return
     */
    List<Integer> selectUserIdByGroupId(Query query) throws ServiceException;

    /**
     * 根据用户ID获取群组ID列表(带分页)
     * @param query
     * @return
     * @throws ServiceException
     */
    List<Integer> selectGroupIdByUserId(Query query) throws ServiceException;
}