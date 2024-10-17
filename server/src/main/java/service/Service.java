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

    public UserData register(UserData newUser){
        try {
            if(null!=userdao.get(newUser.username())){
                throw new RuntimeException("ERROR: User already exists");
            }
            userdao.create(newUser);
            authdao.create(newUser.username());

            return newUser;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
