package dataaccess;

import dataaccess.*;
import model.AddPlayer;
import model.AuthData;
import model.GameData;
import model.UserData;
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


}
