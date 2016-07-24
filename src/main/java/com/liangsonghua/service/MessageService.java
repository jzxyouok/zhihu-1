package com.liangsonghua.service;

import com.liangsonghua.dao.MessageDAO;
import com.liangsonghua.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liangsonghua on 16-7-24.
 */

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class MessageService {

        @Autowired
        MessageDAO messageDAO;


        @Autowired
        SensitiveService sensitiveService;

        public void addMessage(Message message) {
                message.setContent(sensitiveService.filter(message.getContent()));
                messageDAO.addMessage(message);
        }

        public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
                return messageDAO.getConversationDetail(conversationId, offset, limit);
        }

        public List<Message> getConversationList(int userId, int offset, int limit) {
                return messageDAO.getConversationList(userId, offset, limit);
        }

        public int getConvesationUnreadCount(int userId, String conversationId){
            return messageDAO.getConvesationUnreadCount(userId, conversationId);
        }

}
