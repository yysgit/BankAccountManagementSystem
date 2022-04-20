package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbTask;
import com.yys.szcp.mapper.DbTaskMapper;
import com.yys.szcp.service.DbTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements DbTaskService {
    @Autowired
    private DbTaskMapper taskMapper;

    @Override
    public int addTask(DbTask task) {
        return taskMapper.addTask(task);
    }

    @Override
    public List<Map> findTaskList(Map map) {
        return taskMapper.findTaskList(map);
    }

    @Override
    public int findTaskListCount(Map map) {
        return taskMapper.findTaskListCount(map);
    }


    @Override
    public List<Map> findTaskAllList(Map map) {
        return  taskMapper.findTaskAllList(map);
    }

    @Override
    public DbTask findTaskById(Integer taskId) {
        return taskMapper.findTaskById(taskId);
    }

    @Override
    public int updateTask(DbTask task) {
        return taskMapper.updateTask(task);
    }
    @Override
    public int updateUserTask(DbTask task) {
        return taskMapper.updateUserTask(task);
    }




    @Override
    public int deleteTask(DbTask task) {
        return taskMapper.deleteTask(task);
    }


}
