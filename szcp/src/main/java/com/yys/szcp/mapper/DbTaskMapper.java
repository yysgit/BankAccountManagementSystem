package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbTask;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbTaskMapper {

    /**
     * 添加任务
     * @param task
     * @return
     */
    int addTask(DbTask task);

    /**
     * 查询任务列表
     * @param map
     * @return
     */
    @MapKey("id")
    List<Map> findTaskList(Map map);
    int findTaskListCount(Map map);

    @MapKey("id")
    List<Map> findTaskAllList(Map map);


    /**
     * 根据id 查询任务
     * @param taskId
     * @return
     */
    DbTask findTaskById(Integer taskId);

    /**
     * 更新任务
     * @param task
     * @return
     */
    int updateTask(DbTask task);
    int updateUserTask(DbTask task);


    /**
     * 删除任务
     * @param task
     * @return
     */
    int deleteTask(DbTask task);

}