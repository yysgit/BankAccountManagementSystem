package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbBug;
import com.yys.szcp.service.DbBugService;
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
@RequestMapping("sys/bug/")
public class BugController {
    private static final Logger logger = LoggerFactory.getLogger(BugController.class);


    @Autowired
    private DbBugService bugService;




    /**
     * 添加缺陷
     *
     * @param request
     * @param bug
     * @return
     */
    @RequestMapping("addBug")
    @ResponseBody
    public ResultUtil addBug(HttpServletRequest request, String bug) {


        try {
            //封装数据
            Map bug1 =(Map) JSONUtils.parse(bug);
            DbBug bugMy=new DbBug();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            bugMy.setTitle(StringISNULLUtil.mapToString(bug1.get("title")));
            bugMy.setStatus(StringISNULLUtil.mapToInteger(bug1.get("status")));
            bugMy.setBugIntroducerUserId(StringISNULLUtil.mapToInteger(bug1.get("bug_introducer_user_id")));
            bugMy.setBugZrrUserId(StringISNULLUtil.mapToInteger(bug1.get("bug_zrr_user_id")));
            bugMy.setBugGrade(StringISNULLUtil.mapToString(bug1.get("bug_grade")));
            bugMy.setBugType(StringISNULLUtil.mapToInteger(bug1.get("bug_type")));
            bugMy.setBugCode(StringISNULLUtil.mapToString(bug1.get("but_code")));


            bugService.addBug(bugMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加缺陷错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询缺陷
     *
     * @param request
     * @param bugId
     * @return
     */
    @RequestMapping("findBugById")
    @ResponseBody
    public ResultUtil findBugById(HttpServletRequest request, Integer bugId) {
        try {
            DbBug bug = bugService.findBugById(bugId);
            return ResultUtil.success(bug);
        } catch (Exception e) {
            logger.error("根据id  查询缺陷错误: " + e.getMessage());
            return ResultUtil.error("根据id查询缺陷失败!");
        }
    }


    /**
     * 更新缺陷
     *
     * @param request
     * @param bug
     * @return
     */
    @RequestMapping("updateBug")
    @ResponseBody
    public ResultUtil updateBug(HttpServletRequest request, String  bug) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map bug1 =(Map) JSONUtils.parse(bug);
            DbBug bugMy=new DbBug();
            bugMy.setId(StringISNULLUtil.mapToInteger(bug1.get("id").toString()));
            bugMy.setTitle(StringISNULLUtil.mapToString(bug1.get("title")));
            bugMy.setStatus(StringISNULLUtil.mapToInteger(bug1.get("status")));
            bugMy.setBugIntroducerUserId(StringISNULLUtil.mapToInteger(bug1.get("bug_introducer_user_id")));
            bugMy.setBugZrrUserId(StringISNULLUtil.mapToInteger(bug1.get("bug_zrr_user_id")));
            bugMy.setBugGrade(StringISNULLUtil.mapToString(bug1.get("bug_grade")));
            bugMy.setBugType(StringISNULLUtil.mapToInteger(bug1.get("bug_type")));
            bugMy.setBugCode(StringISNULLUtil.mapToString(bug1.get("but_code")));

            bugService.updateBug(bugMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改缺陷错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }






    /**
     * 删除缺陷
     *
     * @param request
     * @param bugId
     * @return
     */
    @RequestMapping("deleteBug")
    @ResponseBody
    public ResultUtil deleteBug(HttpServletRequest request, Integer bugId) {
        try {
            DbBug bug=new DbBug();
            bug.setId(bugId);
            bugService.deleteBug(bug);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除缺陷错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }


    /**
     * 分页查询缺陷列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findBugList")
    @ResponseBody
    public ResultUtil findBugList(HttpServletRequest request, String searchPream) {
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


            map.put("butCodeSearch",StringISNULLUtil.mapToString(searchPreamMy.get("butCodeSearch")));
            map.put("titleSearch", StringISNULLUtil.mapToString(searchPreamMy.get("titleSearch")));
            map.put("statusSearch", StringISNULLUtil.mapToString(searchPreamMy.get("statusSearch")));
            map.put("bugIntroducerAdminFullnameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("bugIntroducerAdminFullnameSearch")));
            map.put("bugZrrAdminFullnameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("bugZrrAdminFullnameSearch")));
            map.put("bugGradeNameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("bugGradeNameSearch")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(bugService.findBugListCount(map));
            resultUtil.setData(bugService.findBugList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加缺陷错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有缺陷列表
     * @param request
     * @return
     */
    @RequestMapping("findBugAllList")
    @ResponseBody
    public ResultUtil findBugAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> bugList=bugService.findBugAllList(map);
            return ResultUtil.success(bugList);
        } catch (Exception e) {
            logger.error("添加缺陷错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}
