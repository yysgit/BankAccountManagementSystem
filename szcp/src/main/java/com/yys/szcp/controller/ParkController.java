package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbPark;
import com.yys.szcp.service.DbParkService;
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
@RequestMapping("sys/park/")
public class ParkController {
    private static final Logger logger = LoggerFactory.getLogger(ParkController.class);


    @Autowired
    private DbParkService parkService;




    /**
     * 添加园区
     *
     * @param request
     * @param park
     * @return
     */
    @RequestMapping("addPark")
    @ResponseBody
    public ResultUtil addPark(HttpServletRequest request, String park) {

        try {
            //封装数据
            Map park1 =(Map) JSONUtils.parse(park);
            DbPark parkMy=new DbPark();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            parkMy.setTitle(StringISNULLUtil.mapToString(park1.get("title")));
            parkMy.setContent(StringISNULLUtil.mapToString(park1.get("content")));
            parkMy.setCreateUserId(adminUser.getId());

            parkService.addPark(parkMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加园区错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询园区
     *
     * @param request
     * @param parkId
     * @return
     */
    @RequestMapping("findParkById")
    @ResponseBody
    public ResultUtil findParkById(HttpServletRequest request, Integer parkId) {
        try {
            DbPark park = parkService.findParkById(parkId);
            return ResultUtil.success(park);
        } catch (Exception e) {
            logger.error("根据id  查询园区错误: " + e.getMessage());
            return ResultUtil.error("根据id查询园区失败!");
        }
    }


    /**
     * 更新园区
     *
     * @param request
     * @param park
     * @return
     */
    @RequestMapping("updatePark")
    @ResponseBody
    public ResultUtil updatePark(HttpServletRequest request, String  park) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map park1 =(Map) JSONUtils.parse(park);
            DbPark parkMy=new DbPark();
            parkMy.setId(StringISNULLUtil.mapToInteger(park1.get("id").toString()));
            parkMy.setTitle(StringISNULLUtil.mapToString(park1.get("title")));
            parkMy.setContent(StringISNULLUtil.mapToString(park1.get("content")));
            parkService.updatePark(parkMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改园区错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }






    /**
     * 删除园区
     *
     * @param request
     * @param parkId
     * @return
     */
    @RequestMapping("deletePark")
    @ResponseBody
    public ResultUtil deletePark(HttpServletRequest request, Integer parkId) {
        try {
            DbPark park=new DbPark();
            park.setId(parkId);
            parkService.deletePark(park);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除园区错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }


    /**
     * 分页查询园区列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findParkList")
    @ResponseBody
    public ResultUtil findParkList(HttpServletRequest request, String searchPream) {
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

            map.put("caseNoSearch",StringISNULLUtil.mapToString(searchPreamMy.get("caseNoSearch")));
            map.put("applySearch", StringISNULLUtil.mapToString(searchPreamMy.get("applySearch")));
            map.put("desingerNameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("desingerNameSearch")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(parkService.findParkListCount(map));
            resultUtil.setData(parkService.findParkList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加园区错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有园区列表
     * @param request
     * @return
     */
    @RequestMapping("findParkAllList")
    @ResponseBody
    public ResultUtil findParkAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> parkList=parkService.findParkAllList(map);
            return ResultUtil.success(parkList);
        } catch (Exception e) {
            logger.error("添加园区错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}
