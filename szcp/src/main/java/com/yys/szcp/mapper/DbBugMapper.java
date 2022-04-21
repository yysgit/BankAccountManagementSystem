package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbBug;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbBugMapper {

    /**
     * 添加缺陷
     * @param bug
     * @return
     */
    int addBug(DbBug bug);

    /**
     * 查询缺陷列表
     * @param map
     * @return
     */
    @MapKey("id")
    List<Map> findBugList(Map map);
    int findBugListCount(Map map);

    @MapKey("id")
    List<Map> findBugAllList(Map map);


    /**
     * 根据id 查询缺陷
     * @param bugId
     * @return
     */
    DbBug findBugById(Integer bugId);

    /**
     * 更新缺陷
     * @param bug
     * @return
     */
    int updateBug(DbBug bug);


    /**
     * 删除缺陷
     * @param bug
     * @return
     */
    int deleteBug(DbBug bug);

}