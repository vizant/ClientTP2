import com.google.gson.Gson;
import communication.PlayerMove;
import communication.message.PlayerMessage;
import communication.message.ServerMessage;
import communication.message.ServerMessage.ServerMessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wisher extends Strategy {

    private final boolean isWordWished;

    public Wisher(BufferedReader reader, PrintWriter writer, boolean isWordWished) {
        super(reader, writer);
        this.isWordWished = isWordWished;
    }

    @Override
    public void execute() throws IOException {
        if (!isWordWished)
            wish();

        System.out.println("Enter *exit* on you turn to finish the game!");
        System.out.println("Enter *save* on you turn to save and finish the game!");

        Gson gson = new Gson();
        while (true) {
            String messageJson = reader.readLine();
            ServerMessage serverMessage = gson.fromJson(messageJson, ServerMessage.class);
            ServerMessageType serverMessageType = serverMessage.getServerMessageType();

            if (serverMessageType.equals(ServerMessageType.SERVER_START)
                    || serverMessageType.equals(ServerMessageType.SERVER_END)) {
                System.out.println(serverMessage.getInformation());

                if (serverMessageType.equals(ServerMessageType.SERVER_END))
                    break;
            } else {
                PlayerMove playerMove = serverMessage.getPlayerMove();
                System.out.println(playerMove);

            }

            System.out.print("YOU: ");
            String msg = validateMessage();

            PlayerMessage playerMessage = new PlayerMessage(msg);
            String myMoveJson = gson.toJson(playerMessage);
            writer.println(myMoveJson);
        }
    }

    private void wish() {
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Wish the word: ");
        String word = scanner.nextLine();
        String jsonWord = gson.toJson(word, String.class);
        writer.println(jsonWord);
    }

    private String validateMessage() {
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        while (!msg.equalsIgnoreCase("yes")
                && !msg.equalsIgnoreCase("no")
                && !msg.equalsIgnoreCase("*save*")
                && !msg.equalsIgnoreCase("*exit*")) {
            System.out.print("Incorrect input (enter yes/no): ");
            msg = scanner.nextLine();
        }
        return msg;
    }
}
