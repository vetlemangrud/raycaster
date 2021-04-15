package spill.storage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;

import spill.game.Wall;
import spill.game.entities.Entity;
import spill.game.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.json.JSONObject;

import spill.game.Level;

public class TiledLevelLoader implements LevelLoader {
    public Level load(int number){
        //Load level file from Tiled (https://www.mapeditor.org/)
        try {
            //Thanks to Stackoverflow "Fego" for how to load file as String https://stackoverflow.com/a/56478035/7169558
            Path levelPath = new File(getClass()
            .getResource("/levels/"+ number + ".json")
            .getFile()).toPath();
            String levelJSONString = Files.readString(levelPath);
            JSONObject levelJSON = new JSONObject(levelJSONString);

            //Currently supports only one tileset
            Path tilesetPath = new File(getClass()
            .getResource("/textures/tileset.json")
            .getFile()).toPath();
            String tilesetJSONString = Files.readString(tilesetPath);
            JSONObject tilesetJSON = new JSONObject(tilesetJSONString);

            HashMap<Integer, String> tileMap = new HashMap<>();
            Iterator<Object> tileIterator = tilesetJSON.getJSONArray("tiles").iterator();
            while (tileIterator.hasNext()) {
                JSONObject tileJSON = (JSONObject) tileIterator.next();
                String textureFile = tileJSON.getString("image");
                String textureName = textureFile.substring(0, textureFile.length()-4);
                tileMap.put(tileJSON.getInt("id") + 1, textureName);
            }
            
            Wall[][] walls = getWalls(levelJSON, tileMap);

            return new Level(walls, new ArrayList<Entity>(), new Vector(1.5,1.5));
        } catch (Exception err) {
            System.err.println(err.getMessage());
            return new Level();
        }
    }
    
    private Wall[][] getWalls(JSONObject levelJSON, HashMap<Integer, String> tileMap){
        int levelWidth = levelJSON.getInt("width");
        int levelHeight = levelJSON.getInt("height");
        Wall[][] walls = new Wall[levelWidth][levelHeight];
        Iterator<Object> wallIterator = levelJSON.getJSONArray("layers").getJSONObject(0).getJSONArray("data").iterator();
        for (int y = 0; y < levelWidth; y++) {
            for (int x = 0; x < levelHeight; x++) {
                int wallId = (int) wallIterator.next();
                if (wallId != 0) {
                    walls[x][y] = new Wall(tileMap.get(wallId));
                } else {
                    walls[x][y] = Wall.AIR;
                }
                
            }
        }
        return walls;
    }
}
