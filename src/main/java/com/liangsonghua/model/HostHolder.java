package com.liangsonghua.model;

import org.springframework.stereotype.Component;

/**
 * Created by liangsonghua on 16-7-21.
 */
/*
负责管理user信息
* */
@Component
public class HostHolder {
        //线程本地变量
        private static ThreadLocal<User> user = new ThreadLocal<User>();

        public User getUser() {
           return user.get();
        }
        public void setUser(User user1 ) {
               user.set(user1);
        }
        public void  clear(){
                user.remove();
        }
}

