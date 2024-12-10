package ui;
import chess.ChessGame;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WSClient extends Endpoint {
    public Session session;

    public WSClient(InGameUI ui) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                Gson g = new Gson();
                ServerMessage recievedCommand = g.fromJson(message, ServerMessage.class);
                switch(recievedCommand.getServerMessageType()){
                    case NOTIFICATION:
                        NotificationMessage notificationMessage = g.fromJson(message,NotificationMessage.class);
                        System.out.println(notificationMessage.getMessageText());
                        break;
                    case ERROR:
                        ErrorMessage errorMessage = g.fromJson(message,ErrorMessage.class);
                        System.out.println(errorMessage.getErrorText());
                        break;
                    case LOAD_GAME:
                        LoadGameMessage loadGameMessage = g.fromJson(message, LoadGameMessage.class);
                        ui.setGameData(loadGameMessage.getGame());
                        ChessPrinting chessPrinting = new ChessPrinting(loadGameMessage.getGame());
                        chessPrinting.printWhiteBoard();
                        break;
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}