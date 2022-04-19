package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.config.jwt.JwtEntity;
import com.yys.szcp.config.jwt.JwtIgnore;
import com.yys.szcp.config.jwt.JwtUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbMenu;

import com.yys.szcp.entityVo.MenuTreeUtil;
import com.yys.szcp.service.DbAdminUserService;
import com.yys.szcp.service.DbMenuService;
import com.yys.szcp.utils.MD5;
import com.yys.szcp.utils.ResultUtil;
import com.yys.szcp.utils.StringISNULLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sys/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private DbAdminUserService adminService;
    @Autowired
    private JwtEntity jwtEntity;
    @Autowired
    private DbMenuService menuService;


    /**
     * 管理员登陆
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @JwtIgnore // 加此注解, 请求不做token验证
    @ResponseBody
    public ResultUtil login(String username, String password, HttpServletRequest request) {
       // int i= 1/0;

        try {

            logger.debug("输出debug级别的日志.....");
            logger.info("输出info级别的日志.....");
            logger.error("输出error级别的日志.....");

            DbAdminUser adminUser = adminService.login(username, password);
            if (adminUser != null) {
                String access_token = JwtUtils.createToken(adminUser.getId().toString(), jwtEntity);
                if (access_token == null) {
                    System.out.println("===== 用户签名失败 =====");
                    return ResultUtil.error("用户签名失败!");
                }
                System.out.println("===== 用户" + username + "生成签名" + access_token + "=====");
                return ResultUtil.success(JwtUtils.getAuthorizationHeader(access_token));
            } else {
                return ResultUtil.error("用户名或者密码错误!");
            }
        } catch (Exception e) {

            logger.error("登陆异常:" + e);
            return ResultUtil.error("登陆异常!");
        }
    }

    /**
     * 得到用户的所有信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/findAdminUserInfo")
    @ResponseBody
    public ResultUtil findAdminUserInfo(HttpServletRequest request) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            List<DbMenu> list = menuService.findMenuByRoleId(adminUser.getRoleId());
            Map map = new HashMap();
            map.put("adminUser", adminUser);
            map.put("meunList", MenuTreeUtil.getMenuTree(list));
            map.put("authentionList", list);
            return ResultUtil.success(map);
        } catch (Exception e) {

            logger.error("得到用户信息异常:" + e);
            return ResultUtil.error("得到用户信息异常!");
        }
    }


    /**
     * 添加管理用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/addAdminUser")
    @ResponseBody
    public ResultUtil addAdminUser(HttpServletRequest request, String adminUser) {
        try {

            //封装数据
            Map adminUser1 = (Map) JSONUtils.parse(adminUser);
            DbAdminUser adminUserMy = new DbAdminUser();
            adminUserMy.setAdminName(StringISNULLUtil.mapToString(adminUser1.get("adminPhone")));
            adminUserMy.setAdminFullname(StringISNULLUtil.mapToString(adminUser1.get("adminFullname")));
            adminUserMy.setAdminPhone(StringISNULLUtil.mapToString(adminUser1.get("adminPhone")));
            adminUserMy.setAddress(StringISNULLUtil.mapToString(adminUser1.get("address")));
            adminUserMy.setIdcard(StringISNULLUtil.mapToString(adminUser1.get("idcard")));

            adminUserMy.setRoleId(StringISNULLUtil.mapToInteger(adminUser1.get("roleId")));

            adminUserMy.setAdminPassword(MD5.MD5Pwd(adminUserMy.getAdminName(), "888888"));
            //验证名称是否重复
            List<DbAdminUser> adminUserList = adminService.findAdminUserByAdminName(null, adminUserMy.getAdminName());
            if (adminUserList != null && adminUserList.size() > 0) {
                return ResultUtil.error("添加失败,名称重复!");
            }
            adminService.addAdminUser(adminUserMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {

            logger.error("添加管理用户错误: " + e);
            return ResultUtil.error("添加失败!");
        }
    }

    /**
     * 查询管理用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/findAdminUserListByOrganId")
    @ResponseBody
    public ResultUtil findAdminUserListByOrganId(HttpServletRequest request, String searchPream) {
        try {
            ResultUtil resultUtil = new ResultUtil();
            //封装数据
            Map search = (Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (Integer.valueOf(search.get("page").toString()) - 1) * 10);
            map.put("limit", search.get("limit"));
            map.put("roleId", adminUser.getRoleId()+"a");
            map.put("userId", adminUser.getId());
            map.put("adminPhoneSearch", StringISNULLUtil.mapToString(search.get("adminPhoneSearch")));
            map.put("adminFullnameSearch", StringISNULLUtil.mapToString(search.get("adminFullnameSearch")));
            map.put("addressSearch",StringISNULLUtil.mapToString(search.get("addressSearch")));
            map.put("idcardSearch", StringISNULLUtil.mapToString(search.get("idcardSearch")));


            resultUtil.setData(adminService.findAdminUserListByOrganId(map));
            resultUtil.setCount(adminService.findAdminUserListByOrganIdCount(map));
            resultUtil.setMsg("查询成功!");
            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            return resultUtil;
        } catch (Exception e) {

            logger.error("查询管理用户列表错误: " + e);
            return ResultUtil.error("查询失败!");
        }
    }


    /**
     * 更新用户
     *
     * @return
     * @par后台管理am request
     */
    @RequestMapping("/updateAdminUser")
    @ResponseBody
    public ResultUtil updateAdminUser(HttpServletRequest request, String adminUser) {
        try {

            //封装数据
            Map adminUser1 = (Map) JSONUtils.parse(adminUser);
            DbAdminUser adminUserMy = new DbAdminUser();

            adminUserMy.setId(StringISNULLUtil.mapToInteger(adminUser1.get("id")));
            adminUserMy.setAdminName(StringISNULLUtil.mapToString(adminUser1.get("adminPhone")));
            adminUserMy.setAdminFullname(StringISNULLUtil.mapToString(adminUser1.get("adminFullname")));
            adminUserMy.setAdminPhone(StringISNULLUtil.mapToString(adminUser1.get("adminPhone")));
            adminUserMy.setAddress(StringISNULLUtil.mapToString(adminUser1.get("address")));
            adminUserMy.setIdcard(StringISNULLUtil.mapToString(adminUser1.get("idcard")));

            adminUserMy.setRoleId(StringISNULLUtil.mapToInteger(adminUser1.get("roleId")));
            //验证名称是否重复
            List<DbAdminUser> adminUserList = adminService.findAdminUserByAdminName(adminUserMy.getId(), adminUserMy.getAdminName());
            if (adminUserList != null && adminUserList.size() > 0) {
                return ResultUtil.error("更新失败,名称重复!");
            }

            adminService.updateAdminUser(adminUserMy);
            return ResultUtil.success("更新成功!");
        } catch (Exception e) {

            logger.error("更新管理用户错误: " + e);
            return ResultUtil.error("更新失败!");
        }
    }

    /**
     * 更新用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/deleteAdminUser")
    @ResponseBody
    public ResultUtil deleteAdminUser(HttpServletRequest request, Integer adminUserId) {
        try {
            DbAdminUser adminUser = new DbAdminUser();
            adminUser.setId(adminUserId);
            adminUser.setDeleteStatus(1);
            adminService.deleteAdminUser(adminUser);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {

            logger.error("删除管理用户错误: " + e);
            return ResultUtil.error("删除失败!");
        }
    }

    /**
     * 初始化密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/editPasswordAdminUser")
    @ResponseBody
    public ResultUtil editPasswordAdminUser(HttpServletRequest request, Integer adminUserId) {
        try {
            DbAdminUser adminUser = adminService.findAdminUserById(adminUserId);

            adminUser.setId(adminUserId);
            adminUser.setAdminPassword(MD5.MD5Pwd(adminUser.getAdminName(), "888888"));
            adminService.editPasswordAdminUserInit(adminUser);
            return ResultUtil.success("初始化密码成功!");
        } catch (Exception e) {

            logger.error("初始化密码错误: " + e);
            return ResultUtil.error("初始化密码失败!");
        }
    }

    /**
     * 更新密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/updatePasswordAdminUser")
    @ResponseBody
    public ResultUtil updatePasswordAdminUser(HttpServletRequest request, String adminUser) {
        try {

            //封装数据
            Map adminUser1 = (Map) JSONUtils.parse(adminUser);

            DbAdminUser adminUser2 = adminService.findAdminUserById(Integer.valueOf(StringISNULLUtil.mapToInteger(adminUser1.get("id"))));

            String oldPassword = StringISNULLUtil.mapToString(adminUser1.get("oldPassword"));
            String newPassword = StringISNULLUtil.mapToString(adminUser1.get("newPassword"));

            DbAdminUser adminUser3 = adminService.login(adminUser2.getAdminName(), oldPassword);
            if (adminUser3 != null) {
                DbAdminUser adminUser4 = new DbAdminUser();
                adminUser4.setId(adminUser2.getId());
                adminUser4.setAdminPassword(MD5.MD5Pwd(adminUser2.getAdminName(), newPassword));
                adminService.editPasswordAdminUserInit(adminUser4);
                return ResultUtil.success("修改密码成功!");
            } else {
                return ResultUtil.error("原始密码不对!");
            }

        } catch (Exception e) {
            logger.error("修改密码错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }


    /**
     * 根据id 查询管理员用户返回用户对象
     *
     * @param request
     * @return
     */
    @RequestMapping("/findAdminUserById")
    @ResponseBody
    public ResultUtil findAdminUserById(HttpServletRequest request, Integer adminUserId) {

        try {
            DbAdminUser adminUser = adminService.findAdminUserById(adminUserId);
            return ResultUtil.success(adminUser);
        } catch (Exception e) {
            logger.error("查询管理用户错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 通过id查询单个管理员用户详细信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/findAdminUserINFOById")
    @ResponseBody
    public ResultUtil findAdminUserINFOById(HttpServletRequest request, Integer adminUserId) {

        try {
            Map map = adminService.findAdminUserINFOById(adminUserId);
            return ResultUtil.success(map);
        } catch (Exception e) {
            logger.error("查询管理用户错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }



}
