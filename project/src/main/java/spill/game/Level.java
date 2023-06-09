package spill.game;

import java.util.ArrayList;
import java.util.Collection;

import spill.game.util.Vector;
import spill.game.entities.Entity;

public class Level {

    private int id;
    private Wall[][] walls; //[width][height] array of walls. Cannot be null
    private Collection<Entity> entities;
    private Collection<Exit> exits;
    private int width;
    private int height;
    private Vector startPosition;
    private String musicName;

    
    
    //Creates level from an array of walls
    public Level(int id, Wall[][] walls, Collection<Entity> entities, Collection<Exit> exits, Vector startPosition, String musicName) {
        try {
            this.id = id;
            this.startPosition = startPosition;
            this.musicName = musicName;
            this.entities = entities;
            this.exits = exits;
            width = walls.length;
            height = walls[0].length;
            for (Wall[] row : walls) {
                if(row.length != height){
                    throw new IllegalArgumentException("Levels must be a NxM 2d matrix");
                }
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Levels must be a NxM 2d matrix and cannot be empty");
        }
        this.walls = walls;
        
    }

    //Error level
    public Level(){
        id = 0;
        musicName = "spooky";
        startPosition = new Vector(10,10);
        walls = new Wall[20][20];
        width = walls.length;
        height = walls[0].length;
        for (int x = 0; x < walls.length; x++) {
            for (int y = 0; y < walls[0].length; y++) {
                if (x == 0 || x== 19 || y == 0 || y == 19) {
                    walls[x][y] = Wall.GREEN;
                } else {
                    walls[x][y] = Wall.AIR;
                }
            }
        }
        this.entities = new ArrayList<>();
        this.exits = new ArrayList<>();
    }

    public Wall getWall(int x, int y){
        return walls[x][y];
    }

    public Wall getWall(Vector pos){
        try {
            return walls[(int)Math.floor(pos.getX())][(int)Math.floor(pos.getY())];
        } catch (ArrayIndexOutOfBoundsException e) {
            return Wall.AIR;
        }
        
    }

    public Collection<Entity> getEntities() {
        return entities;
    }

    public Collection<Exit> getExits(){
        return exits;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Vector getStartPosition(){
        return startPosition.copy();
    }

    public String getMusicName(){
        return musicName;
    }

    public int getId(){
        return id;
    }
}
