package com.goku.im.relation.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.entity.ImUserFriend;

import java.util.List;

public interface ImUserFriendService {
    int countByQuery(Query query) throws ServiceException;

    ImUserFriend selectByPrimaryKey(Integer id) throws ServiceException;

    List<ImUserFriend> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(ImUserFriend record) throws ServiceException;

    int updateByPrimaryKey(ImUserFriend record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(ImUserFriend record, Query query) throws ServiceException;

    int updateByMap(ImUserFriend record, Query query) throws ServiceException;

    int insertSelective(ImUserFriend record) throws ServiceException;
}