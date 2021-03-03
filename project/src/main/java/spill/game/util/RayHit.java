package spill.game.util;

import spill.game.Wall;

public class RayHit {
    //Class for giving more information about a ray hit

    

    private Vector position;
    private Wall wall;
    private Vector face;
    private double wallX;
    RayHit(Vector position, Wall wall, Vector face, double wallX){
        this.position = position;
        this.wall = wall;
        this.face = face;
        this.wallX = wallX;
    }

    public Vector getPosition() {
        return this.position;
    }

    public Wall getWall() {
        return this.wall;
    }

    public Vector getFace() {
        return this.face;
    }

    public double getWallX() {
        return this.wallX;
    }
}
