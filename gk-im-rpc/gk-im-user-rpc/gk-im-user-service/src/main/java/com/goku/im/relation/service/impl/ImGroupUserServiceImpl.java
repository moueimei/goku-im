package com.goku.im.relation.service.impl;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.dao.ImGroupUserMapper;
import com.goku.im.relation.entity.ImGroupUser;
import com.goku.im.relation.service.ImGroupUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imGroupUserService")
public class ImGroupUserServiceImpl implements ImGroupUserService {
    @Autowired
    private ImGroupUserMapper imGroupUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImGroupUserServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.imGroupUserMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public ImGroupUser selectByPrimaryKey(Integer id) throws ServiceException {
        return this.imGroupUserMapper.selectByPrimaryKey(id);
    }

    public List<ImGroupUser> selectByQuery(Query query) throws ServiceException {
        return this.imGroupUserMapper.selectByQuery(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.imGroupUserMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(ImGroupUser record) throws ServiceException {
        return this.imGroupUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ImGroupUser record) throws ServiceException {
        return this.imGroupUserMapper.updateByPrimaryKey(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.imGroupUserMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(ImGroupUser record, Query query) throws ServiceException {
        return this.imGroupUserMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(ImGroupUser record, Query query) throws ServiceException {
        return this.imGroupUserMapper.updateByMap(record, query);
    }

    public int insertSelective(ImGroupUser record) throws ServiceException {
        return this.imGroupUserMapper.insertSelective(record);
    }

    @Override
    public List<Integer> selectUserIdByGroupId(Query query) {
        return this.imGroupUserMapper.selectUserIdByGroupId(query);
    }

    @Override
    public List<Integer> selectGroupIdByUserId(Query query) throws ServiceException {
        return this.imGroupUserMapper.selectGroupIdByUserId(query);
    }
}