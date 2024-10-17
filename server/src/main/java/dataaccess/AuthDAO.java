package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void create(String username) throws DataAccessException;
    AuthData get(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
