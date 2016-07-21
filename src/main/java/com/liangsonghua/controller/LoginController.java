package com.liangsonghua.controller;


import com.liangsonghua.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 16-7-21.
 */
@Controller
public class LoginController {

        private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

        @Autowired
        UserService userService;

        @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
        public String reg(Model model,@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("next") String next,
                                      HttpServletResponse response) {
                try {
                        Map<String,String> map = userService.regirster(username,password);
                        if(map.containsKey("ticket")) {
                                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                                cookie.setPath("/");
                                response.addCookie(cookie);
                                if(StringUtils.isBlank(next)){
                                     return "redirect:"+next;
                                }

                                return "redirect:/";
                        }
                        else {
                                model.addAttribute("msg",map.get("msg"));
                                return "login";
                        }
                } catch (Exception e) {
                        logger.error("注册失败",e.getMessage());
                        model.addAttribute("msg","服务器内部错误");
                        return "login";
                }

        }

         @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
        public String login( Model model, @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {
                try {
                      Map<String,String> map = userService.login(username,password);
                        if(map.containsKey("ticket")) {
                                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                                cookie.setPath("/");
                                response.addCookie(cookie);
                                return "redirect:/";
                        }
                        else {
                                model.addAttribute("msg",map.get("msg"));
                                return "login";
                        }
                } catch (Exception e) {
                        logger.error("登陆失败",e.getMessage());
                        return "login";
                }
        }

        @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
        public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
                model.addAttribute("next", next);
                return "login";
        }

        @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
        public String logout(@CookieValue("ticket") String ticket) {
                userService.logout(ticket);
                return "redirect:/";
        }
}
