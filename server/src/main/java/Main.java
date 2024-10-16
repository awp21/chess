import chess.*;
import com.google.gson.Gson;
import server.Server;
import service.Service;
import spark.Request;
import spark.Response;


public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}

