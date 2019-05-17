package com.lsy.spring.boot.blog.controller;

import com.lsy.spring.boot.blog.domain.*;
import com.lsy.spring.boot.blog.service.*;
import com.lsy.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.lsy.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 用户主页控制器
 * @Author: yoo
 * @Date: 2019-01-04 21:04
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Autowired
    private EsBlogService esBlogService;

    /**
     * 主页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username +"/blogs";
    }

    /**
     * 获取用户信息界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)") //当前用户只能修改自己资料的权限
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());
        //判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        System.out.println(rawPassword);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        System.out.println(encodePasswd);
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        System.out.println(isMatch);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpdateUser(originalUser);
        System.out.println(username);
        return "redirect:/u/" + username +"/profile";
    }

    /**
     * 获取编辑图像界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user) {
        String avatarUrl = user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUser(originalUser);
        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    /**
     * 获取博客列表
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                            @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "catalog",required = false) Long catalogId,
                            @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                            @RequestParam(value = "async",required = false) boolean async,
                            @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                            Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        Page<Blog> page = null;
        if (catalogId != null && catalogId > 0) { //分类查询
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        } else if (order.equals("hot")) { // 最热查询
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
        } else if (order.equals("new")) { // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleVote(user, keyword, pageable);
        }

        List<Blog> list = page.getContent(); //当前所在页面页数列表

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async == true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
    }

    /**
     * 获取博客展示界面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        // 每次读取，阅读量增加1次
        blogService.readingIncrease(id);

        boolean isBlogOwner = false;

        // 判断操作用户是否是博客的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
            && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        // 判断操作用户的点赞情况
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况

        if (principal !=null) {
            for (int i = 0; i < votes.size(); i++) {
                if (votes.get(i).getUser().getUsername().equals(principal.getUsername())) {
                    currentVote = votes.get(i);
                    break;
                } else
                    continue;
            }

        }

        try {
            if (currentVote == null)
                System.out.println("currentVote为空");
            else {
                System.out.println("gkuguigivi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("currentVote", currentVote);
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blog);
        return "/userspace/blog";
    }

    /**
     * 获取新增博客的界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
        //获取用户的分类列表
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("catalogs", catalogs);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 获取编辑博客的界面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        //获取用户的分类列表
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {

        //对Catalog(分类)进行空处理
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false, "未选择分类"));
        }

        try {
            if (blog.getId() != null) {
                Blog originalBlog = blogService.getBlogById(blog.getId());
                originalBlog.setTitle(blog.getTitle());
                originalBlog.setContent(blog.getContent());
                originalBlog.setSummary(blog.getSummary());
                originalBlog.setCatalog(blog.getCatalog());
                originalBlog.setTags(blog.getTags());
                blogService.saveBlog(originalBlog);
            } else {
                User user = (User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs/" +blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

//    /**
//     * 收藏博客
//     * @param blogId
//     * @return
//     */
//    @PostMapping("/collect")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
//    public ResponseEntity<Response> createCollect(Long blogId) {
//        try {
//            blogService.createCollect(blogId);
//        } catch (ConstraintViolationException e) {
//            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
//        } catch (Exception e) {
//            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
//        }
//        return ResponseEntity.ok().body(new Response(true, "收藏成功", null));
//    }
//
//    @GetMapping("/{username}/collect")
//    public String listBlogsByCollect(@PathVariable("username") String username,
//                                     @RequestParam(value = "async",required = false) boolean async,
//                                     @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
//                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
//                                     Model model) throws Exception {
//        Pageable pageable = new PageRequest(pageIndex, pageSize);
//        User user = (User) userDetailsService.loadUserByUsername(username);
//        List<Collect> collects = collectService.listCollectByUserId(user.getId());
//        Page<Blog> page = null;
//        for (int i = 0; i < collects.size(); i++) {
//            page = blogService.listBlogsByCollectId(collects.get(i).getId(), pageable);
//        }
//        List<Blog> blogs = page.getContent();
//        // 判断操作用户是否是分类的所有者
////        boolean isBlogsOwner = false;
////        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
////                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
////            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////            if (principal !=null && user.getUsername().equals(principal.getUsername())) {
////                isBlogsOwner = true;
////            }
////        }
//        model.addAttribute("page", page);
//        //model.addAttribute("isblogsOwner", isBlogsOwner);
//        model.addAttribute("blogList", blogs);
//        return (async == true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
//    }
}
