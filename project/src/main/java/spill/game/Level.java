package spill.game;

import spill.game.util.Vector;

public class Level {

    private Wall[][] walls; //[width][height] array of walls. Cannot be null
    private int width;
    private int height;

    
    
    //Creates level from an array of walls
    public Level(Wall[][] walls) {
        try {
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

    public Level(){
        walls = new Wall[20][20];
        width = walls.length;
        height = walls[0].length;
        for (int x = 0; x < walls.length; x++) {
            for (int y = 0; y < walls[0].length; y++) {
                if (x == 0 || x== 19 || y == 0 || y == 19) {
                    walls[x][y] = Wall.Green;
                } else {
                    walls[x][y] = Wall.Air;
                }
            }
        }
    }

    public Wall getWall(int x, int y){
        return walls[x][y];
    }

    public Wall getWall(Vector pos){
        return walls[(int)Math.floor(pos.getX())][(int)Math.floor(pos.getY())];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
