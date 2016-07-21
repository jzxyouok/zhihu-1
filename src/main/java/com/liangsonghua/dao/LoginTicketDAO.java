package com.liangsonghua.dao;

import com.liangsonghua.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by liangsonghua on 16-7-21.
 */
@Mapper
public interface LoginTicketDAO {
        // 注意空格
        String TABLE_NAME = " login_ticket ";
        String INSERT_FIELDS = " user_id, expired, status, ticket ";
        String SELECT_FIELDS = " id, " + INSERT_FIELDS;

        @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") value(#{userId},#{expired},#{status},#{ticket})"})
        int addTicket(LoginTicket ticket);

        @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=(#{ticket})"})
        LoginTicket selectTicket(String ticket);

        @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
        void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
