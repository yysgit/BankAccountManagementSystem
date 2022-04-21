package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbPark;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbParkMapper {

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
    @MapKey("id")
    List<Map> findParkList(Map map);
    int findParkListCount(Map map);

    @MapKey("id")
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


    /**
     * 删除园区
     * @param park
     * @return
     */
    int deletePark(DbPark park);

}