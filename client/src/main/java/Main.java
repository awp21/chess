import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: Welcome to Chess: Pete Style! Type help to get started");
        PreLogin preLoginDisplay = new PreLogin();
        preLoginDisplay.preLogLooper();
        System.out.println("Thanks for playing, see you soon!");
    }
}