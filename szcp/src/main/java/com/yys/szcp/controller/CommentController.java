package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbComment;
import com.yys.szcp.service.DbCommentService;
import com.yys.szcp.utils.ResultUtil;
import com.yys.szcp.utils.StringISNULLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("sys/comment/")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    private DbCommentService commentService;




    /**
     * 添加评论
     *
     * @param request
     * @param comment
     * @return
     */
    @RequestMapping("addComment")
    @ResponseBody
    public ResultUtil addComment(HttpServletRequest request, String comment) {

        try {
            //封装数据
            Map comment1 =(Map) JSONUtils.parse(comment);
            DbComment commentMy=new DbComment();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");


            commentMy.setParkId(StringISNULLUtil.mapToInteger(comment1.get("parkId")));
            commentMy.setContent(StringISNULLUtil.mapToString(comment1.get("content")));
            commentMy.setCreateUserId(adminUser.getId());

            commentService.addComment(commentMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加评论错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询评论
     *
     * @param request
     * @param commentId
     * @return
     */
    @RequestMapping("findCommentById")
    @ResponseBody
    public ResultUtil findCommentById(HttpServletRequest request, Integer commentId) {
        try {
            DbComment comment = commentService.findCommentById(commentId);
            return ResultUtil.success(comment);
        } catch (Exception e) {
            logger.error("根据id  查询评论错误: " + e.getMessage());
            return ResultUtil.error("根据id查询评论失败!");
        }
    }


    /**
     * 更新评论
     *
     * @param request
     * @param comment
     * @return
     */
    @RequestMapping("updateComment")
    @ResponseBody
    public ResultUtil updateComment(HttpServletRequest request, String  comment) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map comment1 =(Map) JSONUtils.parse(comment);
            DbComment commentMy=new DbComment();
            commentMy.setId(StringISNULLUtil.mapToInteger(comment1.get("id").toString()));
            commentMy.setParkId(StringISNULLUtil.mapToInteger(comment1.get("parkId")));
            commentMy.setContent(StringISNULLUtil.mapToString(comment1.get("content")));

            commentService.updateComment(commentMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改评论错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }






    /**
     * 删除评论
     *
     * @param request
     * @param commentId
     * @return
     */
    @RequestMapping("deleteComment")
    @ResponseBody
    public ResultUtil deleteComment(HttpServletRequest request, Integer commentId) {
        try {
            DbComment comment=new DbComment();
            comment.setId(commentId);
            commentService.deleteComment(comment);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除评论错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }


    /**
     * 分页查询评论列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findCommentList")
    @ResponseBody
    public ResultUtil findCommentList(HttpServletRequest request, String searchPream) {
        try {

            //封装数据
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);

            ResultUtil resultUtil = new ResultUtil();

            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (StringISNULLUtil.mapToInteger(searchPreamMy.get("page").toString()) - 1) * 10);
            map.put("limit", StringISNULLUtil.mapToInteger(searchPreamMy.get("limit")));
            map.put("userId", adminUser.getId());
            map.put("roleId", adminUser.getRoleId()+"a");

            map.put("parkIdSearch",StringISNULLUtil.mapToInteger(searchPreamMy.get("parkIdSearch")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(commentService.findCommentListCount(map));
            resultUtil.setData(commentService.findCommentList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加评论错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有评论列表
     * @param request
     * @return
     */
    @RequestMapping("findCommentAllList")
    @ResponseBody
    public ResultUtil findCommentAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> commentList=commentService.findCommentAllList(map);
            return ResultUtil.success(commentList);
        } catch (Exception e) {
            logger.error("添加评论错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}
