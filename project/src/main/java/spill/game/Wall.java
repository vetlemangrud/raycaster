package spill.game;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wall {

    public static final Wall AIR = new Wall(null);
    public static final Wall GREEN = new Wall(Color.GREEN);
    public static final Wall BLUE = new Wall(Color.BLUE);

    Color color; //Invisible if null
    Image texture;
    public Wall(Color color) {
        this.color = color;
        texture = new Image(getClass().getResourceAsStream("/textures/benk.png"));
    }

    public boolean isSolid(){
        return this.color != null;
    }

    public Color getColor(double x, double y){
        return color;
    }

}

    