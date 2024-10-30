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
                "VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.setString(1,newUser.username());
            preparedStatement.setString(2,newUser.password());
            preparedStatement.setString(3,newUser.email());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserData get(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM userdatabase WHERE username=?");
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if(!rs.next()){
                return null;
            }
            var name = rs.getString("username");
            if(name.equals(username)){
                return new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
