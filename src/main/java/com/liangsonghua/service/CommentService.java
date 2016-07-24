package com.liangsonghua.service;

import com.liangsonghua.dao.CommentDAO;
import com.liangsonghua.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liangsonghua on 16-7-24.
 */
@Service
public class CommentService {

        @Autowired
       private CommentDAO commentDAO;

        @Autowired
        private SensitiveService sensitiveService;


        public int addComment(Comment comment) {
                comment.setContent(sensitiveService.filter(comment.getContent()));
                return  commentDAO.addComment(comment)>0?comment.getUserId():0;
        }

        public void delComment(int entityId,int entityType,int status) {
                  commentDAO.updateStatus(entityId,entityType,status);
        }

        public   List<Comment> getCommentsByEntityId(int entityId,int entityType) {
                return  commentDAO.selectByEntity(entityId,entityType);
        }

        public int getCommentCount(int entityId, int entityType) {
             return commentDAO.getCommentCount(entityId, entityType);
        }
}
