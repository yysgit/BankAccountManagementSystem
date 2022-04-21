package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbComment;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbCommentMapper {

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
    @MapKey("id")
    List<Map> findCommentList(Map map);
    int findCommentListCount(Map map);

    @MapKey("id")
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


    /**
     * 删除评论
     * @param comment
     * @return
     */
    int deleteComment(DbComment comment);

}