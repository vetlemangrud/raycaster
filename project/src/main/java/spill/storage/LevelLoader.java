package spill.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import java.util.Scanner;

public class LevelLoader {
    private static final String LEVEL_EXTENSION = "level";
    private static final String LEVEL_PATH = "../../levels/";
    
    public static void loadLevel(int number){
        try (InputStream is = new FileInputStream(LEVEL_PATH + number + "." + LEVEL_EXTENSION)) {
            try (Scanner scanner = new Scanner(is)) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (IOException err) {
            System.err.println(err.getMessage());
        }
    }
}
