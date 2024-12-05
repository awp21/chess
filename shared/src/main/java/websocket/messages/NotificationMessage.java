package websocket.messages;

public class NotificationMessage extends ServerMessage{
    private String message;
    public NotificationMessage(ServerMessageType type,String message) {
        super(type);
        this.message = message;
    }

    public String getMessageText() {
        return message;
    }

    public void setMessageText(String message) {
        this.message = message;
    }
}
