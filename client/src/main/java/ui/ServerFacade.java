package ui;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }
    //Addsomething to change serverUrl depending on port

    public AuthData registerUser(UserData user) throws ResponseException{
        String path = "/user";
        RequestModel req = new RequestModel("POST",path,user,null);
        return this.makeRequest(req,AuthData.class);
    }

    public AuthData loginUser(UserData user) throws ResponseException {
        String path = "/session";
        RequestModel req = new RequestModel("POST",path,user,null);
        return this.makeRequest(req,AuthData.class);
    }

    public void logoutUser(String authToken) throws ResponseException {
        String path = "/session";
        RequestModel req = new RequestModel("DELETE",path,"",authToken);
        //WHAT DO I DO WHEN THE RESPONSE DON'T MATTER?
        this.makeRequest(req, null);
        return;
    }

    public ListGamesResult listGames(String authToken) throws ResponseException{
        String path = "/game";
        RequestModel req = new RequestModel("GET",path,"",authToken);
        return this.makeRequest(req,ListGamesResult.class);
    }

    public void joinGame(String iD, String color, String authToken) throws ResponseException{
        String path = "/game";
        RequestModel req = new RequestModel("PUT",path,"",authToken);
        this.makeRequest(req,null);
    }

    public GameIDOnly createGame(String gameName, String authToken) throws ResponseException {
        String path = "/game";
        GameData game = new GameData(-1,null,null,gameName, null);
        RequestModel req = new RequestModel("POST",path,game,authToken);
        //FOUND AN ISSUE HERE!!!
        return this.makeRequest(req,GameIDOnly.class);
    }



    //I
    private <T> T makeRequest(RequestModel req, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + req.path())).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(req.method());
            if(req.authToken()!=null){
                http.setRequestProperty("authorization",req.authToken());
            }

            http.setDoOutput(true);

            writeBody(req.request(), http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
