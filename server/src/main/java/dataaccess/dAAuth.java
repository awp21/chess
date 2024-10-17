package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class dAAuth implements AuthDAO{

    @Override
    public AuthData create(String username) {
        //add generator here
        AuthData auth = new AuthData("100",username);
        authDataBase.add(auth);
        return auth;
    }

    @Override
    public AuthData get(String username){
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        authDataBase = new HashSet<>();
    }

    private Set<AuthData> authDataBase = new HashSet<>();

}
