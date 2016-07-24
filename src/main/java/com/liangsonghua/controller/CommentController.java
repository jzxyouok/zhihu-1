package com.liangsonghua.controller;

import com.liangsonghua.model.Comment;
import com.liangsonghua.model.EntityType;
import com.liangsonghua.model.HostHolder;
import com.liangsonghua.service.CommentService;
import com.liangsonghua.service.QuestionService;
import com.liangsonghua.util.ZhihuUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by liangsonghua on 16-7-24.
 */
@Controller
public class CommentController {

        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommentController.class);

         @Autowired
        CommentService commentService;

        @Autowired
        HostHolder hostHolder;

        @Autowired
        QuestionService questionService;

        @RequestMapping(value = "/addComment",method = RequestMethod.POST)
        public String addComment(@RequestParam("questionId") int questionId,
                                 @RequestParam("content") String content) {
                try{
                        Comment comment = new Comment();
                        if(hostHolder.getUser()==null) {
                                comment.setUserId(ZhihuUtil.ANONYMOUS_USERID);
                        }  else {
                                comment.setUserId(hostHolder.getUser().getId());
                        }
                        comment.setContent(content);
                        comment.setCreatedDate(new Date());
                        comment.setEntityId(questionId);
                        comment.setEntityType(EntityType.ENTITY_QUESTION);
                        comment.setStatus(0);
                        commentService.addComment(comment);
                        //更新题目里的评论数量
//                        int count = commentService.getCommentCount(questionId,comment.getEntityType());
//                        questionService.updateCount(questionId,count);
                        int count  = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
                        questionService.updateCount(comment.getEntityId(),count);

                }catch (Exception e) {
                        logger.error("内部错误"+e.getMessage());
                }
                return "redirect:/question/"+String.valueOf(questionId);
        }

}
