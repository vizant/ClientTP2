import com.google.gson.Gson;
import entities.Game;
import entities.Role;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class Client {
    public static void main(String[] args) {
        System.out.printf("Host: %s\nPort: %s\n", args[0], args[1]);
        try (
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                InputStream input = socket.getInputStream();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer =
                        new PrintWriter(output, true)
        ) {
            String name = sendName(writer);

            boolean isPreviousGameSaved = isPreviousGameSaved(reader);
            Game game = null;

            if (isPreviousGameSaved) {
                game = loadPreviousGame(reader);
                String gameString = game.toString();
                System.out.println(gameString.replace(name, "YOU"));
            }

            Strategy strategy;
            Gson gson = new Gson();

            String jsonRole = reader.readLine();
            Role role = gson.fromJson(jsonRole, Role.class);
            System.out.printf("You are %s!\n", role.name());

            if (role.equals(Role.WISHER)) {
                strategy = new Wisher(reader, writer, isPreviousGameSaved);
                if(!isNull(game))
                    System.out.println("Wished word: " + game.getWord());
            } else {
                strategy = new Guesser(reader, writer);
            }

            strategy.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendName(PrintWriter writer) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name> ");
        String name = scanner.nextLine();
        writer.println(name);
        return name;
    }

    private static boolean isPreviousGameSaved(BufferedReader reader) throws IOException {
        Gson gson = new Gson();
        String savedStatusJson = reader.readLine();
        return gson.fromJson(savedStatusJson, boolean.class);
    }

    private static Game loadPreviousGame(BufferedReader reader) throws IOException {
        Gson gson = new Gson();
        String gameJson = reader.readLine();
        return gson.fromJson(gameJson, Game.class);
    }
}
