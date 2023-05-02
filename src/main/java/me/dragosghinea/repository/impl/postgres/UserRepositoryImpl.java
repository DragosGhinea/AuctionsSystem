package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.UserMapper;
import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.repository.UserRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper = UserMapper.getInstance();

    @Override
    public Optional<User> getUserByEmail(String email){
        String sql = "SELECT * FROM UserDetails WHERE email ILIKE ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, email);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            return Optional.of(new User(details));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username){
        String sql = "SELECT * FROM UserDetails WHERE username ILIKE ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, username);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            return Optional.of(new User(details));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO UserDetails (user_id, email, username, birth_date, first_name, last_name, password_hash) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        UserDetails details = user.getUserDetails();

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            stmt.setObject(1, details.getUserId()); // set user_id
            stmt.setString(2, details.getEmail()); // set email
            stmt.setString(3, details.getUsername()); // set username
            stmt.setDate(4, Date.valueOf(details.getBirthDate())); // set birth_date
            stmt.setString(5, details.getFirstName()); // set first_name
            stmt.setString(6, details.getLastName()); // set last_name
            stmt.setString(7, details.getPasswordHash()); // set password_hash

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUserById(UUID id) {
        String sql = "DELETE FROM UserDetails WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        String sql = "SELECT * FROM UserDetails WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, id);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            return Optional.of(new User(details));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
