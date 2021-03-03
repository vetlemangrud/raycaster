package spill.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import spill.game.Level;
import spill.game.Wall;

public class LevelLoader {
    private static final String LEVEL_EXTENSION = "level";
    private  static Map<Character, Wall> wallCodes; // Thanks baeldung! https://www.baeldung.com/java-initialize-hashmap
    static {
        wallCodes = new HashMap<>();
        wallCodes.put('G', Wall.GREEN);
        wallCodes.put('B', Wall.BLUE);
        wallCodes.put('b', Wall.BRICK);
        wallCodes.put('v', Wall.VINES);
        wallCodes.put(' ', Wall.AIR);
    }
    
    public Level load(int number){
        try (InputStream is = getClass().getResourceAsStream("/levels/"+ number + "." + LEVEL_EXTENSION)) { 
            try (Scanner scanner = new Scanner(is)) {
                int width = Integer.parseInt(scanner.nextLine());
                int height = Integer.parseInt(scanner.nextLine());
                Wall[][] walls = new Wall[width][height];
                for (int y = 0; y < height; y++) {
                    String row = scanner.nextLine();
                    for (int x = 0; x < height; x++) {
                        walls[x][y] = wallCodes.get(row.charAt(x));
                    }
                }
                return new Level(walls);
            }
        } catch (IOException err) {
            System.err.println(err.getMessage());
            return new Level();
        }
    }
}
