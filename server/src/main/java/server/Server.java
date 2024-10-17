package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.ErrorModel;
import model.UserData;
import service.Service;
import service.ServiceException;
import spark.*;

public class Server {

    private UserDAO userdao = new dAUser();
    private AuthDAO authdao = new dAAuth();
    private GameDAO gamedoa = new dAGame();
//    maybe I don't need these?
    private Service s = new Service(userdao);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", (req, res) -> createUser(req,res));

//        Spark.delete("/db", (req, res)-> "{}");
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String createUser(Request req, Response res){
        Gson g = new Gson();
        UserData regUser = g.fromJson(req.body(), UserData.class);
        AuthData auth;
        try{
            auth = s.register(regUser);
        }catch (ServiceException e){
            res.status(403);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
        //make a item missing exception.
        res.status(200);
        return g.toJson(auth);
    }
}
