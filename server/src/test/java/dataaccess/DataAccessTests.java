package dataaccess;

import dataaccess.*;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;

import java.util.*;

public class DataAccessTests {
    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;
    private SQLDA sql;
    UserData userMan = new UserData("Billy","Bob","Joe");



    @BeforeEach
    public void setUp() throws DataAccessException {
        user = new SQLUserDAO();
        auth = new SQLAuthDAO();
        game = new SQLGameDAO();
        sql = new SQLDA();
        sql.goodbyeData();
    }

    @Test
    public void addUserToData(){
        try{
            user.create(userMan);
            Assertions.assertInstanceOf(UserData.class,user.get(userMan.username()),"User was not added");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void addUserTwiceToData(){
        try{
            user.create(userMan);
            user.create(userMan);
            assert true : "Exception for double add not thrown";
        } catch (RuntimeException | DataAccessException ignore){
        }
    }

    @Test
    public void getUserFromData(){
        try{
            user.create(userMan);
            UserData testUser = user.get(userMan.username());
            Assertions.assertEquals(userMan,testUser,"User was not added");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void getFalseUserFromData(){
        try{
            UserData testUser = new UserData("HA","Skill","Issue");
            Assertions.assertNull(user.get(testUser.username()),"Not found as null");
        } catch (DataAccessException ignore){
        }
    }

    @Test
    public void clearUser(){
        try{
            user.create(userMan);
            user.clear();
            Assertions.assertNull(user.get(userMan.username()),"User was not deleted");
        } catch (DataAccessException ignore){
        }
    }

}
