package dataaccess;

import model.AuthData;

import java.util.Set;

public interface AuthDAO {
    AuthData create(String username) throws DataAccessException;
    AuthData get(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    Set<AuthData> getDataBase() throws DataAccessException;
}
