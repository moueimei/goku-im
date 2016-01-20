package com.goku.im.relation.dao;

import com.gkframework.orm.mybatis.query.Query;
import com.goku.im.relation.entity.ImGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImGroupMapper {
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
    int insert(ImGroup record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ImGroup record);

    /**
     * 根据条件查询记录集
     */
    List<ImGroup> selectByQuery(@Param("condition") Query query);

    /**
     * 根据主键查询记录
     */
    ImGroup selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByMapSelective(@Param("record") ImGroup record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByMap(@Param("record") ImGroup record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ImGroup record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ImGroup record);

    /**
     * 根据groupIds查询group列表
     * @param query id in (1,2,3) and deleted = 0
     * @return
     */
    List<ImGroup> selectByGroupIds(Query query);
}