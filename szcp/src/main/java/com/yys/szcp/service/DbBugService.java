package com.yys.szcp.service;

import com.yys.szcp.entity.DbBug;

import java.util.List;
import java.util.Map;


public interface DbBugService {

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
    List<Map> findBugList(Map map);
    int findBugListCount(Map map);

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

    int deleteBug(DbBug bug);


}
