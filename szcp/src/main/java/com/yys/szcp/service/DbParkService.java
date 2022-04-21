package com.yys.szcp.service;

import com.yys.szcp.entity.DbPark;

import java.util.List;
import java.util.Map;


public interface DbParkService {

    /**
     * 添加园区
     * @param park
     * @return
     */
    int addPark(DbPark park);

    /**
     * 查询园区列表
     * @param map
     * @return
     */
    List<Map> findParkList(Map map);
    int findParkListCount(Map map);

    List<Map> findParkAllList(Map map);

    /**
     * 根据id 查询园区
     * @param parkId
     * @return
     */
    DbPark findParkById(Integer parkId);

    /**
     * 更新园区
     * @param park
     * @return
     */
    int updatePark(DbPark park);

    int deletePark(DbPark park);


}
