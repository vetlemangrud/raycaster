package spill.game;

import spill.game.util.Vector;

public class Exit {
    private Vector pos;
    private int targetLevel;
    private Vector targetPos;
    public Exit(Vector pos, int targetLevel, Vector targetPos){
        this.pos = pos;
        this.targetLevel = targetLevel;
        this.targetPos = targetPos;
    }
    public Vector getPos(){
        return pos.copy();
    }

    public int getTargetLevel(){
        return targetLevel;
    }

    public Vector getTargetPos(){
        return targetPos.copy();
    }

}
