package spill.game;

import spill.game.util.Vector;

public class Player {
    public static final double SPEED = 3; //Squares per second
    public static final double TURNSPEED = 3; //Radians per second

    private Vector pos;
    private Vector direction;
    private Level level;
    

    public Player(Level level){
        this.level = level;
        pos = new Vector(level.getWidth()/2, level.getHeight()/2);
        direction = new Vector(SPEED, 0);
    }

    public Player(Level level, double x, double y, double angle){
        //For when a player position is saved
        this.level = level;
        pos = new Vector(x, y);
        direction = Vector.getVectorFromAngleAndLength(angle, SPEED);
    }


    public Vector getPos() {
        return pos;
    }

    public Vector getDirection() {
        return direction;
    }

    public void forward(double deltaTime){
        if (!level.getWall(Vector.add(pos, direction.getXComponent().mult(5))).isSolid() || true) {
            pos.add(direction.getXComponent().mult(deltaTime));
        }
        if (!level.getWall(Vector.add(pos, direction.getYComponent().mult(5))).isSolid() || true) {
            pos.add(direction.getYComponent().mult(deltaTime));
        }
        
    }

    public void backward(double deltaTime){
        if (!level.getWall(Vector.sub(pos, direction.getXComponent().mult(5))).isSolid()) {
            pos.sub(direction.getXComponent().mult(deltaTime));
        }
        if (!level.getWall(Vector.sub(pos, direction.getYComponent().mult(5))).isSolid()) {
            pos.sub(direction.getYComponent().mult(deltaTime));
        }
    }

    public void turnLeft(double deltaTime){
        direction.rotate(-TURNSPEED * deltaTime);
    }

    public void turnRight(double deltaTime){
        direction.rotate(TURNSPEED * deltaTime);
    }


    
}
