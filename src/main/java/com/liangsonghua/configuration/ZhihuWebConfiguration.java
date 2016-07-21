package com.liangsonghua.configuration;

import com.liangsonghua.interceptor.LoginRequredInterceptor;
import com.liangsonghua.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by liangsonghua on 16-7-21.
 */
@Component
//注册自定义拦截器
public class ZhihuWebConfiguration extends WebMvcConfigurerAdapter{

        @Autowired
        PassportInterceptor passportInterceptor;

        @Autowired
        LoginRequredInterceptor loginRequredInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(passportInterceptor);
                registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");
                super.addInterceptors(registry);
        }

}
