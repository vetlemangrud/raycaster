package spill.game;

import spill.game.util.Vector;

public class Player {
    public static final double SPEED = 0.05; //Squares per frame
    public static final double TURNSPEED = 3; //Degrees per frame

    private Vector pos;
    private double angle;
    

    public Player(Level level){
        pos = new Vector(level.getWidth()/2, level.getHeight()/2);
        angle = 0;
    }


    public Vector getPos() {
        return this.pos;
    }

    // public void setPos(Vector pos) {
    //     this.pos = pos;
    // }

    public double getAngle() {
        return this.angle;
    }

    // public void setAngle(double angle) {
    //     this.angle = angle;
    // }

    public void forward(){
        pos.add(Vector.getVectorFromAngleAndLength(angle, SPEED));
    }

    public void backward(){
        pos.sub(Vector.getVectorFromAngleAndLength(angle, SPEED));
    }

    public void turnLeft(){
        angle -= TURNSPEED;
    }

    public void turnRight(){
        angle += TURNSPEED;
    }


    
}
