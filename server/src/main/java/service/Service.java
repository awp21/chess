package service;


import dataaccess.*;
import model.AddPlayer;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.util.Set;

public class Service {
    private UserDAO userdao;
    private AuthDAO authdao = new dAAuth();
    public GameDAO gamedao = new dAGame();
    //THIS WILL BE FIXED HEHE

    public Service(UserDAO userdao) {
        this.userdao = userdao;
    }


    public void deleteData(){
        try {
            userdao.clear();
            authdao.clear();
            gamedao.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsernameFromAuthToken(String authToken)throws UnauthorizedException{
        try{
            if(authToken != null) {
                if (authdao.get(authToken) != null) {
                    return authdao.get(authToken).username();
                }
            }
            throw new UnauthorizedException("Error: unauthorized");
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public void addPlayertoGame(String authToken, AddPlayer player)throws UnauthorizedException, BadRequestException,AlreadyTakenException{
        try{
            if(authdao.get(authToken)!=null){
                if(gamedao.getGame(player.gameID())!=null && player.playerColor() != null){
                    if(gamedao.spotEmpty(player.playerColor(),player.gameID())){
                        gamedao.updateGame(player);
                    }else{
                        throw new AlreadyTakenException("Error: already taken");
                    }
                }else{
                    throw new BadRequestException("Error: game does not exist");
                }
            }else{
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<GameData> listGames(String auth)throws UnauthorizedException{
        try{
            if(authdao.get(auth)!=null){
                return gamedao.listAllGames();
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData makeGame(String authToken,String gameName)throws UnauthorizedException{
        try{
            if(authdao.get(authToken)!=null){
                return gamedao.createGame(gameName);
            }
            throw new UnauthorizedException("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            return authdao.create(newUser.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
