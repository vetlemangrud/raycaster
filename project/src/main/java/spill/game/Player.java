package spill.game;

import spill.game.util.Vector;

public class Player {
    public static final double SPEED = 0.05; //Squares per frame
    public static final double TURNSPEED = 3; //Degrees per frame

    private Vector pos;
    private double angle;
    private Level level;
    

    public Player(Level level){
        this.level = level;
        pos = new Vector(level.getWidth()/2, level.getHeight()/2);
        angle = 0;
    }


    public Vector getPos() {
        return this.pos;
    }

    public double getAngle() {
        return this.angle;
    }

    public void forward(){
        Vector velocity = Vector.getVectorFromAngleAndLength(angle, SPEED);
        if (level.getWall(Vector.add(pos, velocity)).color == null) {
            pos.add(velocity);
        }
        
    }

    public void backward(){
        Vector velocity = Vector.getVectorFromAngleAndLength(angle, SPEED);
        if (level.getWall(Vector.sub(pos, velocity)).color == null) {
            pos.sub(velocity);
        }
    }

    public void turnLeft(){
        angle -= TURNSPEED;
    }

    public void turnRight(){
        angle += TURNSPEED;
    }


    
}
