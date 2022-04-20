package com.yys.szcp.service;

import com.yys.szcp.entity.DbTask;

import java.util.List;
import java.util.Map;


public interface DbTaskService {

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
    List<Map> findTaskList(Map map);
    int findTaskListCount(Map map);

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
    int deleteTask(DbTask task);


}
