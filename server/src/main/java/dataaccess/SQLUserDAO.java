package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class SQLUserDAO implements UserDAO{
    @Override
    public void create(UserData newUser) throws DataAccessException {
        String command = "INSERT INTO userDataBase (username, password, email)" +
                "VALUES ('"+ newUser.username() + "','" + newUser.password() + "','" + newUser.email() +"')";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserData get(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String command = "TRUNCATE userDataBase";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<UserData> getDataBase() throws DataAccessException {
        return Set.of();
    }
}
