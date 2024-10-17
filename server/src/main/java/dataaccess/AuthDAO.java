package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData create(String username) throws DataAccessException;
    AuthData get(String username) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean deleteAuthToken(String authToken) throws DataAccessException;
}
