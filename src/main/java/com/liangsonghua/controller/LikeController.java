package com.liangsonghua.controller;

import com.liangsonghua.async.EventModel;
import com.liangsonghua.async.EventProducer;
import com.liangsonghua.async.EventType;
import com.liangsonghua.model.Comment;
import com.liangsonghua.model.EntityType;
import com.liangsonghua.model.HostHolder;
import com.liangsonghua.service.CommentService;
import com.liangsonghua.service.LikeService;
import com.liangsonghua.util.ZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by liangsonghua on 16-7-30.
 */
@Controller
public class LikeController {

        private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LikeController.class);

        @Autowired
        LikeService likeService;

        @Autowired
        CommentService commentService;

        @Autowired
        HostHolder hostHolder;

        @Autowired
        EventProducer eventProducer;

        @RequestMapping(value = {"/like"},method = RequestMethod.POST)
        public String like(@RequestParam("commentId") int commentId) {
                if(hostHolder.getUser()==null) {
                        return ZhihuUtil.getJSONString(999);
                }
                Comment comment = commentService.gerCommentId(commentId);
                eventProducer.fireEvent(new EventModel(EventType.LIKE).
                        setActorId(hostHolder.getUser().getId()).
                        setEntityId(commentId).
                        setEntityType(EntityType.ENTITY_COMMENT).
                        setEntityOwnerId(comment.getUserId())
                        .setExt("questionId", String.valueOf(comment.getEntityId()))
                );

                long like = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
                return ZhihuUtil.getJSONString(0,String.valueOf(like));
        }

        @RequestMapping(value = {"/dislike"},method = RequestMethod.POST)
        public String dislike(@RequestParam("commentId") int commentId) {
                if(hostHolder.getUser()==null) {
                        return ZhihuUtil.getJSONString(999);
                }

                long dislike = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
                return ZhihuUtil.getJSONString(0,String.valueOf(dislike));
        }
}
