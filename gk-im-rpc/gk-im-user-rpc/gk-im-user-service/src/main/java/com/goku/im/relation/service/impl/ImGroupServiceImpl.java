package com.goku.im.relation.service.impl;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.dao.ImGroupMapper;
import com.goku.im.relation.entity.ImGroup;
import com.goku.im.relation.service.ImGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imGroupService")
public class ImGroupServiceImpl implements ImGroupService {
    @Autowired
    private ImGroupMapper imGroupMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImGroupServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.imGroupMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public ImGroup selectByPrimaryKey(Integer id) throws ServiceException {
        return this.imGroupMapper.selectByPrimaryKey(id);
    }

    public List<ImGroup> selectByQuery(Query query) throws ServiceException {
        return this.imGroupMapper.selectByQuery(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.imGroupMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(ImGroup record) throws ServiceException {
        return this.imGroupMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ImGroup record) throws ServiceException {
        return this.imGroupMapper.updateByPrimaryKey(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.imGroupMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(ImGroup record, Query query) throws ServiceException {
        return this.imGroupMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(ImGroup record, Query query) throws ServiceException {
        return this.imGroupMapper.updateByMap(record, query);
    }

    public int insertSelective(ImGroup record) throws ServiceException {
        return this.imGroupMapper.insertSelective(record);
    }

    public List<ImGroup> selectByGroupIds(Query query) throws ServiceException {
        return this.imGroupMapper.selectByGroupIds(query);
    }
}