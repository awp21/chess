package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.GameData;

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

    public void logoutUser(String authToken)throws UnauthorizedException{
        try{
            if(!authdao.deleteAuthToken(authToken)){
                throw new UnauthorizedException("Error: unauthorized");
            }

        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData loginUser(UserData user) throws UnauthorizedException, BadRequestException {
        try {
            if(null==userdao.get(user.username())){
                throw new BadRequestException("Error: user does not exist");
            }
            if(!user.password().equals(userdao.get(user.username()).password())){
                throw new UnauthorizedException("Error: incorrect password");
            }

            return authdao.create(user.username());
        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
