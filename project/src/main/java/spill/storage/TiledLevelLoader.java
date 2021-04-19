package spill.storage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;

import spill.game.Wall;
import spill.game.entities.Entity;
import spill.game.util.Vector;

import java.util.Collection;
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

            int tileSize = levelJSON.getInt("tileheight"); //Assuming square tiles

            //Create HasMap that maps tile id to image name (ex. 1 -> "brick")
            HashMap<Integer, String> tileMap = new HashMap<>();
            Iterator<Object> tileIterator = tilesetJSON.getJSONArray("tiles").iterator();
            while (tileIterator.hasNext()) {
                JSONObject tileJSON = (JSONObject) tileIterator.next();
                String textureFile = tileJSON.getString("image");
                String textureName = textureFile.substring(0, textureFile.length()-4);
                tileMap.put(tileJSON.getInt("id") + 1, textureName);
            }

            Iterator<Object> layerIterator = levelJSON.getJSONArray("layers").iterator();
            Wall[][] walls = new Wall[0][0
        ];
            Collection<Entity> entities = new ArrayList<>();
            while (layerIterator.hasNext()) {
                JSONObject layer = (JSONObject) layerIterator.next();
                //A layer is either a tile layer or object group
                if (layer.getString("type").equals("tilelayer")) {
                    walls = getWalls(layer, tileMap);
                } else {
                    entities.addAll(getEntities(layer, tileSize));
                }
            }
            

            return new Level(walls, entities, new Vector(1.5,1.5));
        } catch (Exception err) {
            System.err.println(err.getMessage());
            return new Level();
        }
    }
    
    private Wall[][] getWalls(JSONObject wallJSON, HashMap<Integer, String> tileMap){
        int levelWidth = wallJSON.getInt("width");
        int levelHeight = wallJSON.getInt("height");
        Wall[][] walls = new Wall[levelWidth][levelHeight];
        Iterator<Object> wallIterator = wallJSON.getJSONArray("data").iterator();
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

    private Collection<Entity> getEntities(JSONObject entityJSON, int tileSize){
        String name = entityJSON.getString("name");
        Collection<Entity> entities = new ArrayList<>();
        Iterator<Object> entityIterator = entityJSON.getJSONArray("objects").iterator();
        while (entityIterator.hasNext()) {
            JSONObject eJSON = (JSONObject) entityIterator.next();
            Vector startPosition = new Vector((float) eJSON.getInt("x") / tileSize,(float) eJSON.getInt("y") / tileSize);
            Entity entity = new Entity(startPosition, name);
            entities.add(entity);
        }
        return entities;
    }
}
