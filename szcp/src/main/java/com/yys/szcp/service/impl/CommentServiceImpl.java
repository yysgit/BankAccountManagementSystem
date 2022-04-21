package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbComment;
import com.yys.szcp.mapper.DbCommentMapper;
import com.yys.szcp.service.DbCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements DbCommentService {
    @Autowired
    private DbCommentMapper commentMapper;

    @Override
    public int addComment(DbComment comment) {
        return commentMapper.addComment(comment);
    }

    @Override
    public List<Map> findCommentList(Map map) {
        return commentMapper.findCommentList(map);
    }

    @Override
    public int findCommentListCount(Map map) {
        return commentMapper.findCommentListCount(map);
    }


    @Override
    public List<Map> findCommentAllList(Map map) {
        return  commentMapper.findCommentAllList(map);
    }

    @Override
    public DbComment findCommentById(Integer commentId) {
        return commentMapper.findCommentById(commentId);
    }

    @Override
    public int updateComment(DbComment comment) {
        return commentMapper.updateComment(comment);
    }


    @Override
    public int deleteComment(DbComment comment) {
        return commentMapper.deleteComment(comment);
    }


}
