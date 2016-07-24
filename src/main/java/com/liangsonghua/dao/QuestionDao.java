package com.liangsonghua.dao;

import com.liangsonghua.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by liangsonghua on 2016/7/2.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select * from  ", TABLE_NAME, "where id=#{id}"})
    Question selectById(int pid);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update",TABLE_NAME," set commentCount=#{commentCount} where id=#{id}"})
    void updateCount(@Param("id") int id,@Param("commentCount") int commentCount);

}
