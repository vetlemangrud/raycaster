package spill.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import spill.game.entities.Entity;
import spill.game.Exit;
import spill.game.Level;
import spill.game.Wall;
import spill.game.util.Vector;

public class SimpleLevelLoader implements LevelLoader {

    /*
    Simple levels are stored in this way:
    First line: level width, w
    Second line: level height, h
    Next h lines: w * wall (See wallCodes for possible characters)
    Next line: Player startX
    Next line: Player startY
    Next line: Entity count, e
    Next 3 * e lines: Entity texture, x pos, y pos
    */
    

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
    
    public Level load(int id){
        //Load .level file
        try (InputStream is = getClass().getResourceAsStream("/levels/"+ id + "." + LEVEL_EXTENSION)) { 
            try (Scanner scanner = new Scanner(is)) {
                //Get level size
                int width = Integer.parseInt(scanner.nextLine());
                int height = Integer.parseInt(scanner.nextLine());

                //Get walls
                Wall[][] walls = new Wall[width][height];
                for (int y = 0; y < height; y++) {
                    String row = scanner.nextLine();
                    for (int x = 0; x < height; x++) {
                        walls[x][y] = wallCodes.get(row.charAt(x));
                    }
                }

                //Get player start position
                double startX = Double.parseDouble(scanner.nextLine());
                double startY = Double.parseDouble(scanner.nextLine());
                Vector startPosition = new Vector(startX, startY);

                //Get entities
                ArrayList<Entity> entities = new ArrayList<>();
                int entityCount = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < entityCount; i++) {
                    String spriteName = scanner.nextLine();
                    double entityX = Double.parseDouble(scanner.nextLine());
                    double entityY = Double.parseDouble(scanner.nextLine());
                    entities.add(new Entity(new Vector(entityX, entityY), spriteName));
                }

                return new Level(id, walls, entities, new ArrayList<Exit>(), startPosition, "jazz");
            }
        } catch (IOException err) {
            System.err.println(err.getMessage());
            return new Level();
        }
    }
}
