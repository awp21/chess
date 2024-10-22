package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Set;

public interface UserDAO {
    void create(UserData newUser) throws DataAccessException;
    UserData get(String username) throws DataAccessException;
    void clear() throws DataAccessException;
    Set<UserData> getDataBase() throws DataAccessException;
}
