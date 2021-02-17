package spill.game;

import spill.game.util.Vector;

public class Player {
    Vector pos;
    double angle;

    public Player(double x, double y){
        pos = new Vector(x, y);
        angle = 0;
    }


    public Vector getPos() {
        return this.pos;
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    
}
