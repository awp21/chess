package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public AuthData create(String username) throws DataAccessException {
        String authToken = String.valueOf(UUID.randomUUID());
        String command = "INSERT INTO authDataBase (authtoken, username)" +
                "VALUES (?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.setString(1,authToken);
            preparedStatement.setString(2,username);
            preparedStatement.executeUpdate();
            return new AuthData(authToken,username);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData get(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT authtoken, username FROM authdatabase WHERE authtoken=?");
            preparedStatement.setString(1, authToken);
            var rs = preparedStatement.executeQuery();
            if(!rs.next()){
                return null;
            }
            var name = rs.getString("authtoken");
            if(name.equals(authToken)){
                return new AuthData(rs.getString("authtoken"),rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String command = "TRUNCATE authDataBase";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String command = "DELETE FROM authDataBase WHERE authtoken=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(command);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<AuthData> getDataBase() throws DataAccessException {
        return Set.of();
    }
}
