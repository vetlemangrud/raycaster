package spill.game;

import spill.game.util.Vector;

public class Exit {
    private Vector pos;
    private double width;
    private double height;
    private int targetLevel;
    private Vector targetPos;
    public Exit(Vector pos, double width, double height, int targetLevel, Vector targetPos){
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.targetLevel = targetLevel;
        this.targetPos = targetPos;
    }

    public int getTargetLevel(){
        return targetLevel;
    }

    public Vector getTargetPos(){
        return targetPos.copy();
    }

    public boolean isInside(Vector pos){
        return 
        pos.getX() >= this.pos.getX() &&
        pos.getX() < this.pos.getX() + this.width &&
        pos.getY() >= this.pos.getY() &&
        pos.getY() < this.pos.getY() + this.height;
    }

}
