package spill.game.util;

import spill.game.Wall;

public class RayHit {
    //Class for giving more information about a ray hit
    private Vector position;
    private Wall wall;
    private double angle;
    RayHit(Vector position, Wall wall, double angle){
        this.position = position;
        this.wall = wall;
        this.angle = angle;
    }

    public Vector getPosition() {
        return this.position;
    }

    public Wall getWall() {
        return this.wall;
    }

    public double getAngle() {
        return this.angle;
    }
}
