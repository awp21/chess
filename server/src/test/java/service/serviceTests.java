package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

import java.net.HttpURLConnection;
import java.util.*;

public class serviceTests {

    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;
    private Service service;
    UserData alreadyMadeUser = new UserData("Toad","Shroom","wrongemail");


    @BeforeEach
    public void setUp(){
        user = new dAUser();
        auth = new dAAuth();
        game = new dAGame();
        service = new Service(user);
        service.deleteData();
    }



    @Test
    public void makeTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            GameData gdata = service.makeGame(authD.authToken(), "myNewGame");
            Assertions.assertInstanceOf(GameData.class,gdata,"Game data not created");
        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException e) {
            assert false : "Game not created";
        }
    }

    @Test
    public void makeFailedTest(){
        try{
            AuthData badAuth = new AuthData("hehe","gottem");
            GameData gdata = service.makeGame(badAuth.authToken(), "badGame");
            Assertions.assertInstanceOf(GameData.class,gdata,"Game data was created without authorization");
        } catch (UnauthorizedException e) {
            return;
        }
    }

    //Monday 9am Entrance 1 Pedersen tower, past gift shop radiology on the right. show 15 minutes early.

    @Test
    public void registerTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            Assertions.assertInstanceOf(AuthData.class,authD,"Did not return an AuthData");
        } catch (BadRequestException | AlreadyTakenException e) {
            assert false : "Error, bad request or already taken";
        }
    }

    @Test
    public void registerBadRequestTest(){
        UserData jimbo = new UserData(null,"password","Jimbo@byu.edu");
        try{
            AuthData authD = service.register(jimbo);
            assert false : "Did not catch bad username";
        } catch (BadRequestException | AlreadyTakenException e) {
            return;
        }
    }

    @Test
    public void loginTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            service.loginUser(alreadyMadeUser);
            assert auth.getDataBase().isEmpty() : "Login Failed";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException e) {
            return;
        }
    }

    @Test
    public void loginNonexistentUserTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            UserData falseUser = new UserData("Fake","hacker","notreal");
            service.loginUser(falseUser);
            assert !auth.getDataBase().isEmpty() : "Created User that wasn't registered";
            //Maybe an error here???
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException e) {
            return;
        }
    }

    @Test
    public void logoutSuccessTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser(authD.authToken());
            assert auth.getDataBase().isEmpty() : "Logout Unsuccessful";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException e) {
            return;
        }
    }

    @Test
    public void logoutUnAuthorizedTest(){
        try{
            AuthData authD = service.register(alreadyMadeUser);
            service.logoutUser("BadAuthToken");
            assert !auth.getDataBase().isEmpty() : "Didn't catch bad authToken";
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException | DataAccessException e) {
            return;
        }
    }

    @Test
    public void testEmpty(){
        service.deleteData();
        try{
            Set ExpectedUser = new HashSet<>();
            Set UserResult = user.getDataBase();
            Set AuthResult = auth.getDataBase();
            Set GameResult = game.listAllGames();
            Assertions.assertEquals(UserResult,ExpectedUser,"UserDataBase did not clear");
            Assertions.assertEquals(AuthResult,ExpectedUser,"AuthDataBase did not clear");
            Assertions.assertEquals(GameResult,ExpectedUser,"GameDataBase did not clear");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



}
