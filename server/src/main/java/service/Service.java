package service;


import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class Service {
    private UserDAO userdao;

    public Service(UserDAO userdao) {
        this.userdao = userdao;
    }

    public UserData register(UserData newUser){
        try {
            if(null!=userdao.get(newUser.username())){
                throw new RuntimeException("ERROR: User already exists");
            }
            userdao.create(newUser);
            return newUser;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
