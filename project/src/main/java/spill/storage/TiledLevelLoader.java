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

import spill.game.Exit;
import spill.game.Level;

public class TiledLevelLoader implements LevelLoader {
    public Level load(int number){
        //Load level file from Tiled (https://www.mapeditor.org/)
        try {
            //Thanks to Stackoverflow "Fego" for how to load file as String https://stackoverflow.com/a/56478035/7169558
            //Loads level file to JSON Object
            Path levelPath = new File(getClass()
            .getResource("/levels/"+ number + ".json")
            .getFile()).toPath();
            String levelJSONString = Files.readString(levelPath);
            JSONObject levelJSON = new JSONObject(levelJSONString);

            //Currently supports only one tileset
            //Loads tileset fil to JSON Object
            Path tilesetPath = new File(getClass()
            .getResource("/textures/tileset.json")
            .getFile()).toPath();
            String tilesetJSONString = Files.readString(tilesetPath);
            JSONObject tilesetJSON = new JSONObject(tilesetJSONString);

            int tileSize = levelJSON.getInt("tileheight"); //Assuming square tiles

            //Create HashMap that maps tile id to wall
            HashMap<Integer, Wall> tileMap = new HashMap<>();
            Iterator<Object> tileIterator = tilesetJSON.getJSONArray("tiles").iterator();
            while (tileIterator.hasNext()) {
                JSONObject tileJSON = (JSONObject) tileIterator.next();
                String textureFile = tileJSON.getString("image");
                String textureName = textureFile.substring(0, textureFile.length()-4);
                tileMap.put(tileJSON.getInt("id") + 1, new Wall(textureName));
            }

            Vector startPostion = new Vector(1.5, 1.5); //Default start position

            //Gets music name
            String musicName = levelJSON.getJSONArray("properties").getJSONObject(0).getString("value");

            
            Wall[][] walls = new Wall[0][0];
            Collection<Entity> entities = new ArrayList<>();
            Collection<Exit> exits = new ArrayList<>();

            //Iterates through layers and adds them to either entitiy list or walls
            Iterator<Object> layerIterator = levelJSON.getJSONArray("layers").iterator();
            while (layerIterator.hasNext()) {
                JSONObject layer = (JSONObject) layerIterator.next();
                //A layer is either a tile layer, an object group or a group (folder)
                if (layer.getString("type").equals("tilelayer")) {
                    //If it is a tile layer, it is an array of wall IDs
                    walls = loadWalls(layer, tileMap);
                } else if (layer.getString("name").equals("start")) {
                    //Get start position from start layer
                    double startX = layer.getJSONArray("objects").getJSONObject(0).getDouble("x") / (float)tileSize;
                    double startY = layer.getJSONArray("objects").getJSONObject(0).getDouble("y") / (float)tileSize;
                    startPostion = new Vector(startX, startY);
                } else if (layer.getString("name").equals("exits")) {
                    exits = loadExits(layer, tileSize);
                } else if (layer.getString("name").equals("entities")){
                    Iterator<Object> entityTypeIterator = layer.getJSONArray("layers").iterator();
                    while(entityTypeIterator.hasNext()){
                        entities.addAll(loadEntities((JSONObject) entityTypeIterator.next(), tileSize));
                    }
                }
            }
            

            return new Level(walls, entities, exits, startPostion, musicName);
        } catch (Exception err) {
            System.err.println(err.getMessage());
            return new Level();
        }
    }
    
    private Wall[][] loadWalls(JSONObject wallJSON, HashMap<Integer, Wall> tileMap){
        int levelWidth = wallJSON.getInt("width");
        int levelHeight = wallJSON.getInt("height");
        Wall[][] walls = new Wall[levelWidth][levelHeight];
        Iterator<Object> wallIterator = wallJSON.getJSONArray("data").iterator();
        for (int y = 0; y < levelWidth; y++) {
            for (int x = 0; x < levelHeight; x++) {
                int wallId = (int) wallIterator.next();
                if (wallId != 0) {
                    walls[x][y] = tileMap.get(wallId);
                } else {
                    walls[x][y] = Wall.AIR;
                }
                
            }
        }
        return walls;
    }

    private Collection<Entity> loadEntities(JSONObject entityJSON, int tileSize){
        String name = entityJSON.getString("name");
        Collection<Entity> entities = new ArrayList<>();
        Iterator<Object> entityIterator = entityJSON.getJSONArray("objects").iterator();
        while (entityIterator.hasNext()) {
            JSONObject eJSON = (JSONObject) entityIterator.next();
            Vector startPosition = new Vector(eJSON.getFloat("x") / tileSize,eJSON.getFloat("y") / tileSize);
            Entity entity = new Entity(startPosition, name);
            entities.add(entity);
        }
        return entities;
    }

    private Collection<Exit> loadExits(JSONObject exitsJSON, int tileSize){
        Collection<Exit> exits = new ArrayList<>();
        Iterator<Object> exiIterator = exitsJSON.getJSONArray("objects").iterator();
        while (exiIterator.hasNext()) {
            JSONObject exitJSON = (JSONObject) exiIterator.next();
            Vector position = new Vector(exitJSON.getFloat("x") / tileSize,exitJSON.getFloat("y") / tileSize);
            int targetLevel = 0;
            float targetX = 0;
            float targetY = 0;
            Iterator<Object> propertyIterator = exitJSON.getJSONArray("properties").iterator();
            while (propertyIterator.hasNext()) {
                JSONObject property = (JSONObject) propertyIterator.next();
                if (property.getString("name").equals("targetLevel")) {
                    targetLevel = property.getInt("value");
                } else if (property.getString("name").equals("targetX")) {
                    targetX = property.getFloat("value");
                } else if (property.getString("name").equals("targetY")) {
                    targetY = property.getFloat("value");
                }
            }
            exits.add(new Exit(position, targetLevel, new Vector (targetX, targetY)));
        }
        return exits;
    }
}
