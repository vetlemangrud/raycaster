package spill.game;

import spill.game.util.RayCaster;
import spill.game.util.RayHit;
import spill.game.util.Vector;

public class Player {
    public static final double SPEED = 3; //Squares per second
    public static final double TURNSPEED = 3; //Radians per second
    public static final double MIN_WALL_DIST = 0.1; //It it possible to get "infinitly" close to a wall, but not possible to clip a wall. MIN_WALL_DIST is distance from a wall and a player walking right on it ( o -> |)

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
        RayHit nextXHit = RayCaster.hitWall(pos, direction.getXComponent(), level);
        if (Vector.distance(nextXHit.getPosition(), pos) > Math.abs(direction.getX()) * deltaTime + MIN_WALL_DIST) {
            pos.add(direction.getXComponent().mult(deltaTime));
        }

        RayHit nextYHit = RayCaster.hitWall(pos, direction.getYComponent(), level);
        if (Vector.distance(nextYHit.getPosition(), pos) > Math.abs(direction.getY()) * deltaTime + MIN_WALL_DIST) {
            pos.add(direction.getYComponent().mult(deltaTime));
        }
        
    }

    public void backward(double deltaTime){
        RayHit nextXHit = RayCaster.hitWall(pos, direction.getXComponent().mult(-1), level);
        if (Vector.distance(nextXHit.getPosition(), pos) > Math.abs(direction.getX()) * deltaTime + MIN_WALL_DIST) {
            pos.add(direction.getXComponent().mult(deltaTime).mult(-1));
        }

        RayHit nextYHit = RayCaster.hitWall(pos, direction.getYComponent().mult(-1), level);
        if (Vector.distance(nextYHit.getPosition(), pos) > Math.abs(direction.getY()) * deltaTime + MIN_WALL_DIST) {
            pos.add(direction.getYComponent().mult(deltaTime).mult(-1));
        }
    }

    public void turnLeft(double deltaTime){
        direction.rotate(-TURNSPEED * deltaTime);
    }

    public void turnRight(double deltaTime){
        direction.rotate(TURNSPEED * deltaTime);
    }


    
}
