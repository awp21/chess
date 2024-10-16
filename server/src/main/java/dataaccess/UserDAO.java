package dataaccess;

import model.UserData;

public interface UserDAO {
    void create(UserData newUser) throws DataAccessException;
    UserData get(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
