package com.liangsonghua.dao;

import com.liangsonghua.model.Message;
import org.apache.ibatis.annotations.*;


import java.util.List;

/**
 * Created by liangsonghua on 16-7-24.
 */
@Mapper
public interface MessageDAO {
        // 注意空格
        String TABLE_NAME = " message ";
        String INSERT_FIELDS = " from_id,to_id, content, created_date, has_read,conversation_id";
        String SELECT_FIELDS = " id, " + INSERT_FIELDS;

        @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") value" +
                "(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
        int addMessage(Message message);

        @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
        List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                            @Param("offset") int offset, @Param("limit") int limit);

        @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
        int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

        @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
        List<Message> getConversationList(@Param("userId") int userId,
                                          @Param("offset") int offset, @Param("limit") int limit);


}
