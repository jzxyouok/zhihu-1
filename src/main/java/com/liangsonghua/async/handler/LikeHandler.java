package com.liangsonghua.async.handler;

import com.liangsonghua.async.EventHandler;
import com.liangsonghua.async.EventModel;
import com.liangsonghua.async.EventType;
import com.liangsonghua.model.Message;
import com.liangsonghua.model.User;
import com.liangsonghua.service.MessageService;
import com.liangsonghua.service.UserService;
import com.liangsonghua.util.ZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 16-8-4.
 */
@Component
public class LikeHandler implements EventHandler {
        @Autowired
        MessageService messageService;

        @Autowired
        UserService userService;

        @Override
        public void doHandle(EventModel model) {
                Message message = new Message();
                message.setFromId(ZhihuUtil.SYSTEM_USERID);
                message.setToId(model.getEntityOwnerId());
                message.setCreatedDate(new Date());
                User user = userService.getUser(model.getActorId());
                message.setContent("用户" + user.getName()
                        + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

                messageService.addMessage(message);
        }

        @Override
        public List<EventType> getSupportEventType() {
                return null;
        }


}
