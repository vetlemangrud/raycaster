package spill.game;

import javafx.scene.paint.Color;

public class Wall {

    public static final Wall Air = new Wall(null);
    public static final Wall Green = new Wall(Color.GREEN);

    Color color; //Invisible if null
    public Wall(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return color;
    } 

}

    