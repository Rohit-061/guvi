package model;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameData {
    private static final String FILE_PATH = "game_data.txt";

    // Save game data to a file
    public static void saveGame(String playerWhite, String playerBlack, List<String> moves, String winner) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(playerWhite + "," + playerBlack + "," + String.join(" ", moves) + "," + winner + "\n");
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    // Load all games from the file
    public static List<String> loadGames() {
        List<String> games = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                games.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading games: " + e.getMessage());
        }
        return games;
    }
}
