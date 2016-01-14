package com.goku.im.relation.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tv.acframework.orm.mybatis.query.Query;
import tv.acfun.im.relation.entity.ImGroupUser;

import java.util.List;
import java.util.Map;

@Repository
public interface ImGroupUserMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByQuery(@Param("condition") Query query);

    /**
     * 根据条件删除记录
     */
    int deleteByQuery(@Param("condition") Query query);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(ImGroupUser record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ImGroupUser record);

    /**
     * 根据条件查询记录集
     */
    List<ImGroupUser> selectByQuery(@Param("condition") Query query);

    /**
     * 根据主键查询记录
     */
    ImGroupUser selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByMapSelective(@Param("record") ImGroupUser record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByMap(@Param("record") ImGroupUser record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ImGroupUser record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ImGroupUser record);

    /**
     * 根据群组ID获取用户ID列表(获取群组成员列表)
     * @param query
     * @return
     */
    List<Integer> selectUserIdByGroupId(Query query);

    /**
     * 根据用户ID获取群组ID列表(带分页)
     * @param query
     * @return
     */
    List<Integer> selectGroupIdByUserId(Query query);
}