import com.google.gson.Gson;
import communication.PlayerMove;
import communication.message.PlayerMessage;
import communication.message.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Guesser extends Strategy {

    public Guesser(BufferedReader reader, PrintWriter writer) {
        super(reader, writer);
    }

    @Override
    public void execute() throws IOException {

        System.out.println("Enter *exit* on you turn to finish the game!");
        System.out.println("Enter *save* on you turn to save and finish the game!");

        Gson gson = new Gson();

        while (true) {
            String serverMessageJson = reader.readLine();
            ServerMessage serverMessage = gson.fromJson(serverMessageJson, ServerMessage.class);
            ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();

            if (serverMessageType.equals(ServerMessage.ServerMessageType.SERVER_START)
                    || serverMessageType.equals(ServerMessage.ServerMessageType.SERVER_END)) {
                System.out.println(serverMessage.getInformation());

                if (serverMessageType.equals(ServerMessage.ServerMessageType.SERVER_END))
                    break;
            } else {
                PlayerMove playerMove = serverMessage.getPlayerMove();
                System.out.println(playerMove);
            }

            System.out.print("YOU: ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();

            PlayerMessage playerMessage = new PlayerMessage(msg);
            String myMoveJson = gson.toJson(playerMessage);
            writer.println(myMoveJson);
        }
    }
}

