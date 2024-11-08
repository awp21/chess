package ui;

import java.util.Scanner;

public class PreLogin {
    private String reg = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n";
    private String login = "login <USERNAME> <PASSWORD> - to play chess\n";
    private String quit = "quit - playing chess\n";
    private String help = "help - with possible commands\n";
    private String loggedOutSet = "[LOGGED_OUT] >>> ";
    Scanner reader = new Scanner(System.in);

    public PreLogin(){

    }

    public void preLogLooper(){
        boolean continueLoop = true;
        String response;
        String [] parsedResponse;
        while(continueLoop) {
            System.out.print(loggedOutSet);
            response = reader.next();
            parsedResponse = response.split(" ");
            switch (parsedResponse[0]) {
                case "help":
                    System.out.println(reg + login + quit + help);
                    break;
                case "register":
                    continueLoop = false;
                    System.out.println("Registering!");
                    break;
                case "login":
                    continueLoop = false;
                    System.out.println("Logging in!");
                    break;
                case "quit":
                    continueLoop = false;
                    System.out.println("Quitting!");
                    break;
                default:
                    System.out.println("Command not understood, try again");
            }
        }
    }


}
