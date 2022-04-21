package com.yys.szcp.service;

import com.yys.szcp.entity.DbComment;

import java.util.List;
import java.util.Map;


public interface DbCommentService {

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(DbComment comment);

    /**
     * 查询评论列表
     * @param map
     * @return
     */
    List<Map> findCommentList(Map map);
    int findCommentListCount(Map map);

    List<Map> findCommentAllList(Map map);

    /**
     * 根据id 查询评论
     * @param commentId
     * @return
     */
    DbComment findCommentById(Integer commentId);

    /**
     * 更新评论
     * @param comment
     * @return
     */
    int updateComment(DbComment comment);

    int deleteComment(DbComment comment);


}
