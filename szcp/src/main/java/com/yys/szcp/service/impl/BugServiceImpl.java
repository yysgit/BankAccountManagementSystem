package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbBug;
import com.yys.szcp.mapper.DbBugMapper;
import com.yys.szcp.service.DbBugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BugServiceImpl implements DbBugService {
    @Autowired
    private DbBugMapper bugMapper;

    @Override
    public int addBug(DbBug bug) {
        return bugMapper.addBug(bug);
    }

    @Override
    public List<Map> findBugList(Map map) {
        return bugMapper.findBugList(map);
    }

    @Override
    public int findBugListCount(Map map) {
        return bugMapper.findBugListCount(map);
    }


    @Override
    public List<Map> findBugAllList(Map map) {
        return  bugMapper.findBugAllList(map);
    }

    @Override
    public DbBug findBugById(Integer bugId) {
        return bugMapper.findBugById(bugId);
    }

    @Override
    public int updateBug(DbBug bug) {
        return bugMapper.updateBug(bug);
    }


    @Override
    public int deleteBug(DbBug bug) {
        return bugMapper.deleteBug(bug);
    }


}
