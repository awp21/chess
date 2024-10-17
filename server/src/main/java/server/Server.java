package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.ErrorModel;
import model.UserData;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;
import spark.*;

public class Server {

    private UserDAO userdao = new dAUser();
    private AuthDAO authdao = new dAAuth();
    private GameDAO gamedao = new dAGame();
//    maybe I don't need these?
    private Service s = new Service(userdao);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req,res) -> deleteAll(res));
        Spark.post("/user", (req, res) -> createUser(req,res));
        Spark.post("/session", (req, res) -> login(req,res));

//        Spark.delete("/db", (req, res)-> "{}");
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String login(Request req, Response res){
        Gson g = new Gson();
        UserData logUser = g.fromJson(req.body(), UserData.class);

        AuthData auth;
        try {
            auth = s.loginUser(logUser);
        }catch (UnauthorizedException |BadRequestException e) {
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
        return g.toJson(auth);
    }



    private String deleteAll(Response res){
        try{
            s.deleteData();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Gson g = new Gson();
        res.status(200);
        return g.toJson(null);
    }

    private String createUser(Request req, Response res){
        Gson g = new Gson();
        UserData regUser = g.fromJson(req.body(), UserData.class);
        AuthData auth;
        try{
            auth = s.register(regUser);
        }catch (AlreadyTakenException e){
            res.status(403);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }catch (BadRequestException e){
            res.status(400);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
        //make a item missing exception.
        res.status(200);
        return g.toJson(auth);
    }
}
