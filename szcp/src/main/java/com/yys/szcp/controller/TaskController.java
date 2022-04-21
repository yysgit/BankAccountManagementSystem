package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;

import com.yys.szcp.entity.DbTask;
import com.yys.szcp.service.DbTaskService;
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
@RequestMapping("sys/task/")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


    @Autowired
    private DbTaskService taskService;



    /**
     * 添加任务
     *
     * @param request
     * @param task
     * @return
     */
    @RequestMapping("addTask")
    @ResponseBody
    public ResultUtil addTask(HttpServletRequest request, String task) {

        try {
            //封装数据
            Map task1 =(Map) JSONUtils.parse(task);
            DbTask taskMy=new DbTask();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            taskMy.setManagerUserId(adminUser.getId());
            taskMy.setName(StringISNULLUtil.mapToString(task1.get("name")));
            taskMy.setApply(StringISNULLUtil.mapToString(task1.get("apply")));
            taskMy.setCommissioningTime(StringISNULLUtil.mapToDateyyyyMMdd(task1.get("commissioningTime")));


            taskService.addTask(taskMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加任务错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询任务
     *
     * @param request
     * @param taskId
     * @return
     */
    @RequestMapping("findTaskById")
    @ResponseBody
    public ResultUtil findTaskById(HttpServletRequest request, Integer taskId) {
        try {
            DbTask task = taskService.findTaskById(taskId);
            return ResultUtil.success(task);
        } catch (Exception e) {
            logger.error("根据id  查询任务错误: " + e.getMessage());
            return ResultUtil.error("根据id查询任务失败!");
        }
    }


    /**
     * 更新任务
     *
     * @param request
     * @param task
     * @return
     */
    @RequestMapping("updateTask")
    @ResponseBody
    public ResultUtil updateTask(HttpServletRequest request, String  task) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map task1 =(Map) JSONUtils.parse(task);
            DbTask taskMy=new DbTask();
            taskMy.setId(StringISNULLUtil.mapToInteger(task1.get("id").toString()));
            taskMy.setName(StringISNULLUtil.mapToString(task1.get("name")));
            taskMy.setApply(StringISNULLUtil.mapToString(task1.get("apply")));
            taskMy.setCommissioningTime(StringISNULLUtil.mapToDateyyyyMMdd(task1.get("commissioningTime")));

            taskService.updateTask(taskMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改任务错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }


    /**
     * 更新任务
     *
     * @param request
     * @param task
     * @return
     */
    @RequestMapping("updateUserTask")
    @ResponseBody
    public ResultUtil updateUserTask(HttpServletRequest request, String  task) {
        try {

            //封装数据
            Map task1 =(Map) JSONUtils.parse(task);
            DbTask taskMy=new DbTask();
            taskMy.setId(StringISNULLUtil.mapToInteger(task1.get("id").toString()));
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            taskMy.setTestUserId(StringISNULLUtil.mapToInteger(task1.get("testUserName")));
            taskMy.setId(StringISNULLUtil.mapToInteger(task1.get("id")));


            taskService.updateUserTask(taskMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改任务错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }




    /**
     * 删除任务
     *
     * @param request
     * @param taskId
     * @return
     */
    @RequestMapping("deleteTask")
    @ResponseBody
    public ResultUtil deleteTask(HttpServletRequest request, Integer taskId) {
        try {
            DbTask task=new DbTask();
            task.setId(taskId);
            taskService.deleteTask(task);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除任务错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }



    /**
     * 分页查询任务列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findTaskList")
    @ResponseBody
    public ResultUtil findTaskList(HttpServletRequest request, String searchPream) {
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

            map.put("nameSearch",StringISNULLUtil.mapToString(searchPreamMy.get("nameSearch")));
            map.put("applySearch", StringISNULLUtil.mapToString(searchPreamMy.get("applySearch")));
            map.put("testUserNameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("testUserNameSearch")));
            map.put("managerUserNameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("managerUserNameSearch")));


            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(taskService.findTaskListCount(map));
            resultUtil.setData(taskService.findTaskList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加任务错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有任务列表
     * @param request
     * @return
     */
    @RequestMapping("findTaskAllList")
    @ResponseBody
    public ResultUtil findTaskAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> taskList=taskService.findTaskAllList(map);
            return ResultUtil.success(taskList);
        } catch (Exception e) {
            logger.error("添加任务错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}
