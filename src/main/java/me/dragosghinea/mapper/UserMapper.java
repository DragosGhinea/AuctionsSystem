package me.dragosghinea.mapper;

import me.dragosghinea.model.UserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper {

    private static final UserMapper instance = new UserMapper();

    public static UserMapper getInstance() {
        return instance;
    }

    private UserMapper(){}

    public UserDetails mapToUserDetails(ResultSet set) throws SQLException {
        if(!set.next())
            return null;

        return UserDetails.builder()
                .email(set.getString("email"))
                .userId(set.getObject("user_id", UUID.class))
                .firstName(set.getString("first_name"))
                .lastName(set.getString("last_name"))
                .username(set.getString("username"))
                .birthDate(set.getDate("birth_date").toLocalDate())
                .passwordHash(set.getString("password_hash"))
                .build();
    }
}
