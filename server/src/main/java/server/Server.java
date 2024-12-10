package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.*;
import service.Service;
import spark.*;

import java.util.Set;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WSServer.class);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req,res) -> deleteAll(res));
        Spark.post("/user", (req, res) -> createUser(req,res));
        Spark.post("/session", (req, res) -> login(req,res));
        Spark.delete("/session", (req, res) -> logout(req,res));
        Spark.post("/game", (req,res)->createGame(req,res));
        Spark.get("/game", (req,res)->listGames(req,res));
        Spark.put("/game", (req,res)->joinGame(req,res));

        //HERE I PUT MY WSERVER???


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String joinGame(Request req, Response res){
        Gson g = new Gson();
        String authToken = req.headers("authorization");
        AddPlayer player = g.fromJson(req.body(), AddPlayer.class);
        String username;
        try {
            username = Service.getUsernameFromAuthToken(authToken);
            player = new AddPlayer(player.playerColor(), player.gameID(), username);
            Service.addPlayerToGame(authToken,player);
            res.status(200);
            return "{}";
        }catch (UnauthorizedException e){
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }catch (BadRequestException e){
            res.status(400);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        } catch (AlreadyTakenException e) {
            res.status(403);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
    }

    private String listGames(Request req, Response res){
        Gson g = new Gson();
        String authToken = req.headers("authorization");
        try{
            Set<GameData> allGames = Service.listGames(authToken);
            res.status(200);
            ListGamesResult listgamesresult = new ListGamesResult(allGames);
            return g.toJson(listgamesresult);
            //HERE IS THE PROBLEM! WHAT DO I USE TO RETURN ALL GAMES?
        }catch (UnauthorizedException e){
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
    }

    private String createGame(Request req, Response res){
        Gson g = new Gson();
        String authToken = req.headers("authorization");
        try{
            String name = g.fromJson(req.body(),GameData.class).gameName();
            GameData retGame;
            retGame=Service.makeGame(authToken,name);
            res.status(200);
            return g.toJson(new GameIDOnly(retGame.gameID()));
        }catch (UnauthorizedException e){
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
    }

    private String logout(Request req, Response res){
        Gson g = new Gson();
        String authToken = req.headers("authorization");
        try{
            Service.logoutUser(authToken);
            res.status(200);
            return "{}";
        }catch (UnauthorizedException e){
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
    }

    private String login(Request req, Response res){
        Gson g = new Gson();
        UserData logUser = g.fromJson(req.body(), UserData.class);

        AuthData auth;
        try {
            auth = Service.loginUser(logUser);
        }catch (UnauthorizedException |BadRequestException e) {
            res.status(401);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
        return g.toJson(auth);
    }



    private String deleteAll(Response res){
        Service.deleteData();
        Gson g = new Gson();
        res.status(200);
        return "{}";
    }

    private String createUser(Request req, Response res){
        Gson g = new Gson();
        UserData regUser = g.fromJson(req.body(), UserData.class);
        AuthData auth;
        try{
            auth = Service.register(regUser);
        }catch (AlreadyTakenException e){
            res.status(403);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }catch (BadRequestException e){
            res.status(400);
            ErrorModel error = new ErrorModel(e.getMessage());
            return g.toJson(error);
        }
        res.status(200);
        return g.toJson(auth);
    }
}
