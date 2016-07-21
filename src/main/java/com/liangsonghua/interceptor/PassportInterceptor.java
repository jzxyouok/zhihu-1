package com.liangsonghua.interceptor;

import com.liangsonghua.dao.LoginTicketDAO;
import com.liangsonghua.dao.UserDAO;
import com.liangsonghua.model.HostHolder;
import com.liangsonghua.model.LoginTicket;
import com.liangsonghua.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by liangsonghua on 16-7-21.
 */
@SuppressWarnings("ALL")
public class PassportInterceptor implements HandlerInterceptor{
        @Autowired
        LoginTicketDAO loginTicketDAO;

        @Autowired
        UserDAO userDAO;

        @Autowired
        HostHolder hostHolder;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
               String ticket = null;
                if(request.getCookies()!=null) {
                     for(Cookie cookie: request.getCookies()) {
                             if(cookie.getName().equals("ticket")) {
                                     ticket = cookie.getValue();
                                     break;
                             }
                     }
                }
                if(ticket!=null) {
                        LoginTicket loginTicket = loginTicketDAO.selectTicket(ticket);
                        if(loginTicket==null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=1) {
                                return  true;
                        }
                        User user =userDAO.selectById(loginTicket.getUserId());
                        hostHolder.setUser(user);
                }
                return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
                if(modelAndView!=null) {
                        //所有渲染页面都可以直接访问当前登陆用户信息
                        modelAndView.addObject("user",hostHolder.getUser());
                }
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
                hostHolder.clear();
        }
}
