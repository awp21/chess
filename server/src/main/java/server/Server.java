package server;

import com.google.gson.Gson;
import model.UserData;
import service.Service;
import spark.*;

public class Server {

    private Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", (req, res) -> createUser(req,res));
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String createUser(Request req, Response res){
        Gson g = new Gson();
        UserData regUser=g.fromJson(req.body(), UserData.class);
        UserData user = s.register(regUser);
        res.status(202);

        res.body(g.toJson(user));
        return res.body();
    }




}
