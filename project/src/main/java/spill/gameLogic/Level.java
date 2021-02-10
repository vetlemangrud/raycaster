package spill.gameLogic;

import java.io.IOException;

import spill.storage.Storage;
import spill.storage.StorageInteface;

public class Level {
    
    public static void main(String[] args) {
        StorageInteface storage = new Storage(1);
        try {
            storage.writeSave("HEIGHT", "147");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
    }
}
