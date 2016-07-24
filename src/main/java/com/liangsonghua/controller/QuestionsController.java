package com.liangsonghua.controller;

import com.liangsonghua.model.*;
import com.liangsonghua.service.CommentService;
import com.liangsonghua.service.QuestionService;
import com.liangsonghua.service.UserService;
import com.liangsonghua.util.ZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangsonghua on 16-7-24.
 */

@Controller
public class QuestionsController {

        private static final Logger logger = LoggerFactory.getLogger(QuestionsController.class);

        @Autowired
        QuestionService questionService;

        @Autowired
        CommentService commentService;

        @Autowired
        UserService userService;

        @Autowired
        HostHolder hostHolder;


        @RequestMapping(value = "/question/add",method = RequestMethod.POST)
       @ResponseBody
        public String add(@RequestParam("title") String title,
                          @RequestParam("content") String content) {
                try{
                        Question question = new Question();
                        question.setTitle(title);
                        question.setContent(content);
                        question.setCommentCount(0);
                        question.setCreatedDate(new Date());
                        if(hostHolder.getUser()==null) {
                                question.setUserId(ZhihuUtil.ANONYMOUS_USERID);
                                return ZhihuUtil.getJSONString(999);
                        }
                        else {
                                question.setUserId(hostHolder.getUser().getId());
                        }
                        if(questionService.addQuestion(question)>0) {
                                return ZhihuUtil.getJSONString(0);
                        }
                }catch (Exception e){
                   logger.error("内部错误"+e.getMessage());
                }
                return ZhihuUtil.getJSONString(1,"发布失败");
        }

        @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
        public String questionDetail(Model model, @PathVariable("qid") int qid) {
                Question question = questionService.getById(qid);
                model.addAttribute("question", question);
                List<Comment> commentList =commentService .getCommentsByEntityId(qid, EntityType.ENTITY_QUESTION);
                List<ViewObject> vos = new ArrayList<ViewObject>();
                for (Comment comment : commentList) {
                        ViewObject vo = new ViewObject();
                        vo.set("comment", comment);
                        vo.set("user", userService.getUser(comment.getUserId()));
                        vos.add(vo);
                }
                model.addAttribute("comments", vos);
                return "detail";
        }
}
