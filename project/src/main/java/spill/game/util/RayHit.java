package spill.game.util;

import spill.game.Wall;

public class RayHit {
    //Class for giving more information about a ray hit

    

    private Vector position;
    private Wall wall;
    private Vector face;
    RayHit(Vector position, Wall wall, Vector face){
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

    public Vector getFace() {
        return this.face;
    }
}
