package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class dAAuth implements AuthDAO{

    @Override
    public void create(String username) throws DataAccessException {
        //add generator here
        AuthData auth = new AuthData("100",username);
        authDataBase.add(auth);
    }

    @Override
    public AuthData get(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    private Set<AuthData> authDataBase = new HashSet<>();

}
