package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbPark;
import com.yys.szcp.mapper.DbParkMapper;
import com.yys.szcp.service.DbParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ParkServiceImpl implements DbParkService {
    @Autowired
    private DbParkMapper parkMapper;

    @Override
    public int addPark(DbPark park) {
        return parkMapper.addPark(park);
    }

    @Override
    public List<Map> findParkList(Map map) {
        return parkMapper.findParkList(map);
    }

    @Override
    public int findParkListCount(Map map) {
        return parkMapper.findParkListCount(map);
    }


    @Override
    public List<Map> findParkAllList(Map map) {
        return  parkMapper.findParkAllList(map);
    }

    @Override
    public DbPark findParkById(Integer parkId) {
        return parkMapper.findParkById(parkId);
    }

    @Override
    public int updatePark(DbPark park) {
        return parkMapper.updatePark(park);
    }


    @Override
    public int deletePark(DbPark park) {
        return parkMapper.deletePark(park);
    }


}
