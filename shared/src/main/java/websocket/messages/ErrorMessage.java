package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorText() {
        return errorMessage;
    }

    public void setErrorText(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
