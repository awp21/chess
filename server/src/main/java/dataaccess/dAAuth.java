package dataaccess;

import model.AuthData;
import service.UnauthorizedException;

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

    @Override
    public boolean deleteAuthToken(String authToken) throws DataAccessException{
        for(AuthData auth:authDataBase){
            if(auth.authToken().equals(authToken)){
                authDataBase.remove(auth);
                return true;
            }
        }
        return false;
    }

    private Set<AuthData> authDataBase = new HashSet<>();

}
