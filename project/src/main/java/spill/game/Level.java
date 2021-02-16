package spill.game;

import javafx.scene.paint.Color;

public class Level {

    private Wall[][] walls; //[width][height] array of walls. Cannot be null
    private int width;
    private int height;

    private static final Wall Air = new Wall(null);
    private static final Wall GreenWall = new Wall(Color.GREEN);
    
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
                if (x == 2 || y == 3) {
                    walls[x][y] = GreenWall;
                } else {
                    walls[x][y] = Air;
                }
            }
        }
    }

    public Wall getWall(int x, int y){
        return walls[x][y];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
