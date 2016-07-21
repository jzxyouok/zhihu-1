package com.liangsonghua.service;

import org.springframework.stereotype.Service;

/**
 * Created by liangsonghua on 2016/7/10.
 */
@Service
public class ZhihuService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
