package spill.game.util;

import spill.game.Wall;

public class RayHit {
    //Class for giving more information about a ray hit

    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;

    private Vector position;
    private Wall wall;
    private int face;
    RayHit(Vector position, Wall wall, int face){
        this.position = position;
        this.wall = wall;
        this.face = face;
    }

    public Vector getPosition() {
        return this.position;
    }

    public Wall getWall() {
        return this.wall;
    }

    public int getFace() {
        return this.face;
    }
}
