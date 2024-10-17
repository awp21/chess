package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;

public class Service {
    private UserDAO userdao;
    private AuthDAO authdao = new dAAuth();
    private GameDAO gamedao = new dAGame();

    public Service(UserDAO userdao) {
        this.userdao = userdao;
    }

    public void deleteData() throws DataAccessException {
        userdao.clear();
        authdao.clear();
        gamedao.clear();
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        return authdao.create(user.username());
    }

    public AuthData register(UserData newUser) throws AlreadyTakenException, BadRequestException{
        try {
            if(newUser.username() == null || newUser.email() == null || newUser.password() == null){
                throw new BadRequestException("Error: bad request");
            }

            if(null!=userdao.get(newUser.username())){
                throw new AlreadyTakenException("Error: already taken");
            }

            userdao.create(newUser);
            AuthData auth = authdao.create(newUser.username());
            return auth;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
