package dataaccess;

import model.AuthData;
import model.UserData;
import service.UnauthorizedException;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DAAuth implements AuthDAO{

    @Override
    public AuthData create(String username) {
        //add generator here
        Random rand = new Random();
        int newAuthNumber = rand.nextInt(1000);
        AuthData auth = new AuthData(Integer.toString(newAuthNumber),username);
        authDataBase.add(auth);
        return auth;
    }

    @Override
    public AuthData get(String authToken) {
        for(AuthData auth: authDataBase){
            if(auth.authToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        authDataBase = new HashSet<>();
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{
        for(AuthData auth:authDataBase){
            if(auth.authToken().equals(authToken)){
                authDataBase.remove(auth);
            }
        }
    }

    @Override
    public Set<AuthData> getDataBase() throws DataAccessException {
        return authDataBase;
    }

    private Set<AuthData> authDataBase = new HashSet<>();

}
