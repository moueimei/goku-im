package com.goku.im.relation.service.impl;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.dao.ImUserFriendMapper;
import com.goku.im.relation.entity.ImUserFriend;
import com.goku.im.relation.service.ImUserFriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imUserFriendService")
public class ImUserFriendServiceImpl implements ImUserFriendService {
    @Autowired
    private ImUserFriendMapper imUserFriendMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImUserFriendServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.imUserFriendMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public ImUserFriend selectByPrimaryKey(Integer id) throws ServiceException {
        return this.imUserFriendMapper.selectByPrimaryKey(id);
    }

    public List<ImUserFriend> selectByQuery(Query query) throws ServiceException {
        return this.imUserFriendMapper.selectByQuery(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.imUserFriendMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(ImUserFriend record) throws ServiceException {
        return this.imUserFriendMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ImUserFriend record) throws ServiceException {
        return this.imUserFriendMapper.updateByPrimaryKey(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.imUserFriendMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(ImUserFriend record, Query query) throws ServiceException {
        return this.imUserFriendMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(ImUserFriend record, Query query) throws ServiceException {
        return this.imUserFriendMapper.updateByMap(record, query);
    }

    public int insertSelective(ImUserFriend record) throws ServiceException {
        return this.imUserFriendMapper.insertSelective(record);
    }
}