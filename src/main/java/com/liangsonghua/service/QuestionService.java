package com.liangsonghua.service;

import com.liangsonghua.dao.QuestionDAO;
import com.liangsonghua.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by liangsonghua on 2016/7/15.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question) {
            //转义
            question.setContent(HtmlUtils.htmlEscape(question.getContent()));
            question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
            //过滤敏感词
            question.setContent(sensitiveService.filter(question.getContent()));
            question.setTitle(sensitiveService.filter(question.getTitle()));
            return questionDAO.addQuestion(question)>0?question.getUserId():0;
    }
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question getById(int id) {
            return  questionDAO.selectById(id);
    }
}
