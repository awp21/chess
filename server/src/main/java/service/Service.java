package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.dAAuth;
import model.AuthData;
import model.UserData;

public class Service {
    private UserDAO userdao;
    private AuthDAO authdao = new dAAuth();;

    public Service(UserDAO userdao) {
        this.userdao = userdao;
    }


    public AuthData register(UserData newUser){
        try {
            if(null!=userdao.get(newUser.username())){
                throw new RuntimeException("ERROR: User already exists");
            }
            userdao.create(newUser);
            AuthData auth = authdao.create(newUser.username());
            return auth;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
