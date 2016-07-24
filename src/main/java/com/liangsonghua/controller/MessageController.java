package com.liangsonghua.controller;

import com.liangsonghua.model.HostHolder;
import com.liangsonghua.model.Message;
import com.liangsonghua.model.User;
import com.liangsonghua.model.ViewObject;
import com.liangsonghua.service.MessageService;
import com.liangsonghua.service.UserService;
import com.liangsonghua.util.ZhihuUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangsonghua on 16-7-24.
 */

@Controller
public class MessageController {

        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MessageController.class);

        @Autowired
        MessageService messageService;

        @Autowired
        UserService userService;

        @Autowired
        HostHolder hostHolder;

        @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
        @ResponseBody
        public String addMessage(@RequestParam("toName") String toName,
                                 @RequestParam("content") String content) {
                try {
                        if (hostHolder.getUser() == null) {
                                return ZhihuUtil.getJSONString(999, "未登录");
                        }
                        User user = userService.selectByName(toName);
                        if (user == null) {
                                return ZhihuUtil.getJSONString(1, "用户不存在");
                        }

                        Message msg = new Message();
                        msg.setContent(content);
                        msg.setFromId(hostHolder.getUser().getId());
                        msg.setToId(user.getId());
                        msg.setCreatedDate(new Date());
                        messageService.addMessage(msg);
                        return ZhihuUtil.getJSONString(0);
                } catch (Exception e) {
                        logger.error("增加站内信失败" + e.getMessage());
                        return ZhihuUtil.getJSONString(1, "插入站内信失败");
                }
        }

        @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
        public String conversationDetail(Model model) {
                try {
                        int localUserId = hostHolder.getUser().getId();
                        List<ViewObject> conversations = new ArrayList<ViewObject>();
                        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
                        for (Message msg : conversationList) {
                                ViewObject vo = new ViewObject();
                                vo.set("conversation", msg);
                                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                                User user = userService.getUser(targetId);
                                vo.set("user", user);
                                vo.set("unread", messageService.getConvesationUnreadCount(localUserId,msg.getConversationId()));
                                conversations.add(vo);
                        }
                        model.addAttribute("conversations", conversations);
                } catch (Exception e) {
                        logger.error("获取站内信列表失败" + e.getMessage());
                }
                return "letter";
        }

        @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
        public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
                try {
                        List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
                        List<ViewObject> messages = new ArrayList<ViewObject>();
                        for (Message msg : conversationList) {
                                ViewObject vo = new ViewObject();
                                vo.set("message", msg);
                                User user = userService.getUser(msg.getFromId());
                                if (user == null) {
                                        continue;
                                }
                                vo.set("headUrl", user.getHeadUrl());
                                vo.set("userId", user.getId());
                                messages.add(vo);
                        }
                        model.addAttribute("messages", messages);
                } catch (Exception e) {
                        logger.error("获取详情消息失败" + e.getMessage());
                }
                return "letterDetail";
        }

}
