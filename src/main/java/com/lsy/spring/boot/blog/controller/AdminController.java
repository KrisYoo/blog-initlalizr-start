package com.lsy.spring.boot.blog.controller;

import com.lsy.spring.boot.blog.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理控制器
 * @Author: yoo
 * @Date: 2019-01-04 21:22
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

    /**
     * 获取后台管理主页面
     * @return
     */
    @GetMapping
    public ModelAndView listUsers(Model model) {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("用户管理", "/users"));
        list.add(new Menu("博客管理", "/adminblogs"));
        list.add(new Menu("分类管理", "/admincatalogs"));
        //list.add(new Menu("标签管理", "/admintags"));
        model.addAttribute("list", list);
        return new ModelAndView("/admins/index", "model", model);
    }
}