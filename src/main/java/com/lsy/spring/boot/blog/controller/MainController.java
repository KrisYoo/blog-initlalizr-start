package com.lsy.spring.boot.blog.controller;

import com.lsy.spring.boot.blog.domain.Authority;
import com.lsy.spring.boot.blog.domain.User;
import com.lsy.spring.boot.blog.service.AuthorityService;
import com.lsy.spring.boot.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页控制器.
 * @Author: yoo
 * @Date: 2019-01-04 20:47
 */
@Controller
public class MainController {

    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/blogs";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，用户名或密码错误");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

//    @GetMapping("/register-error")
//    public String registerError(Model model) {
//        model.addAttribute("registerError", true);
//        model.addAttribute("registerMsg", "注册失败，用户名已存在");
//        return "register";
//    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("registerError", true);
            model.addAttribute("registerMsg", "注册失败，用户名已存在");
            return "register";
        } if (userService.emailExists(user.getEmail())) {
            model.addAttribute("registerError", true);
            model.addAttribute("registerMsg", "注册失败，邮箱已存在");
            return "register";
        } else {
            List<Authority> authorities = new ArrayList<>();
            authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
            user.setAuthorities(authorities);
            userService.registerUser(user);
            return "redirect:/login";
        }
    }

    @GetMapping("search")
    public String search() {
        return "search";
    }
}