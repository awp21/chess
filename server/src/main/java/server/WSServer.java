package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WSServer {
    private Session session;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        this.session = session;
        //SEND DIFFERENT SESSIONS??? MAKE A LIST THAT SORTS SESSIONS FOR LOADGAME/NOTIFICATIONS
        session.getRemote().sendString(message);
    }


}