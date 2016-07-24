package com.liangsonghua.controller;

import com.liangsonghua.model.HostHolder;
import com.liangsonghua.model.Question;
import com.liangsonghua.service.QuestionService;
import com.liangsonghua.util.ZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by liangsonghua on 16-7-24.
 */

@Controller
public class QuestionsController {

        private static final Logger logger = LoggerFactory.getLogger(QuestionsController.class);

        @Autowired
        QuestionService questionService;

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

        @RequestMapping(value = "/question/{pid}")
        public String questionDetail(Model model,@PathVariable("pid") int pid) {
              Question question = questionService.getById(pid);
                model.addAttribute("title",question.getTitle());
                model.addAttribute("content",question.getContent());
                return "detail";
        }
}
