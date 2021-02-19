package spill.game;

import spill.game.util.Vector;

public class Player {
    public static final double SPEED = 0.05; //Squares per frame
    public static final double TURNSPEED = 3; //Degrees per frame

    private Vector pos;
    private Vector direction;
    private Level level;
    

    public Player(Level level){
        this.level = level;
        pos = new Vector(level.getWidth()/2, level.getHeight()/2);
        direction = new Vector(SPEED, 0);
    }


    public Vector getPos() {
        return this.pos;
    }

    public double getDirection() {
        return this.direction;
    }

    public void forward(){
        if (level.getWall(Vector.add(pos, velocity.getXComponent())).color == null) {
            pos.add(direction.getXComponent());
        }
        if (level.getWall(Vector.add(pos, velocity.getYComponent())).color == null) {
            pos.add(direction.getYComponent());
        }
        
    }

    public void backward(){
        if (level.getWall(Vector.sub(pos, direction.getXComponent())).color == null) {
            pos.sub(direction.getXComponent());
        }
        if (level.getWall(Vector.sub(pos, direction.getYComponent())).color == null) {
            pos.sub(direction.getYComponent());
        }
    }

    public void turnLeft(){
        direction.rotate(-TURNSPEED);
    }

    public void turnRight(){
        direction.rotate(TURNSPEED);
    }


    
}
