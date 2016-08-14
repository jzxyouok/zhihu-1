package com.liangsonghua.async;

import com.alibaba.fastjson.JSONObject;
import com.liangsonghua.util.JedisAdapter;
import com.liangsonghua.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 16-8-4.
 */
@Service
public class EventProducer {
        @Autowired
        JedisAdapter jedisAdapter;

        public boolean fireEvent(EventModel eventModel) {
                try {
                        String json = JSONObject.toJSONString(eventModel);
                        String key = RedisKeyUtil.getEventQueueKey();
                        jedisAdapter.lpush(key, json);
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }
}
