package com.liangsonghua.async;

import com.alibaba.fastjson.JSON;
import com.liangsonghua.util.JedisAdapter;
import com.liangsonghua.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangsonghua on 16-8-3.
 */
@Service
public class EventConsumer implements InitializingBean {

        private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

      private Map<EventType,List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

       private ApplicationContext applicationContext;

        @Autowired
        JedisAdapter jedisAdapter;

        @Override
        public void afterPropertiesSet() throws Exception {
                Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
                if(beans!=null) {
                        for(Map.Entry<String,EventHandler> entry:beans.entrySet()) {
                             List<EventType> eventTypes = entry.getValue().getSupportEventType();
                                 for(EventType type:eventTypes) {
                                    if(!config.containsKey(type)) {
                                      config.put(type,new ArrayList<EventHandler>());
                                    }
                                    config.get(type).add(entry.getValue());
                                 }
                            }
                }
                Thread thread  = new Thread(new Runnable() {
                        @Override
                        public void run() {
                           while(true) {
                                   String key = RedisKeyUtil.getEventQueueKey();
                                   List<String> events = jedisAdapter.brpop(0, key);
                                  for(String message:events) {
                                          if(message.equals(key)) {
                                                 continue;
                                          }
                                          EventModel eventModel = JSON.parseObject(message,EventModel.class);
                                          if(!config.containsKey(eventModel.getType())) {
                                               logger.error("内部错误");
                                                  continue;
                                          }
                                          for(EventHandler handler:config.get(eventModel.getType())) {
                                                  handler.doHandle(eventModel);
                                          }
                                  }

                           }
                        }
                });thread.start();
        }
}
