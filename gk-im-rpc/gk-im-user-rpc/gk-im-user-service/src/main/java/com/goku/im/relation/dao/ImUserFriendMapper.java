package com.goku.im.relation.dao;

import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.entity.ImUserFriend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImUserFriendMapper {
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
    int insert(ImUserFriend record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ImUserFriend record);

    /**
     * 根据条件查询记录集
     */
    List<ImUserFriend> selectByQuery(@Param("condition") Query query);

    /**
     * 根据主键查询记录
     */
    ImUserFriend selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByMapSelective(@Param("record") ImUserFriend record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByMap(@Param("record") ImUserFriend record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ImUserFriend record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ImUserFriend record);
}