package com.goku.im.relation.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.entity.ImGroup;

import java.util.List;

public interface ImGroupService {
    int countByQuery(Query query) throws ServiceException;

    ImGroup selectByPrimaryKey(Integer id) throws ServiceException;

    List<ImGroup> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(ImGroup record) throws ServiceException;

    int updateByPrimaryKey(ImGroup record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(ImGroup record, Query query) throws ServiceException;

    int updateByMap(ImGroup record, Query query) throws ServiceException;

    int insertSelective(ImGroup record) throws ServiceException;

    List<ImGroup> selectByGroupIds(Query query) throws ServiceException;
}