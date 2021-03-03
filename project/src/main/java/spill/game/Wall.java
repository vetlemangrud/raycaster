package spill.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Wall {

    public static final Wall AIR = new Wall(null);
    public static final Wall GREEN = new Wall(Color.GREEN);
    public static final Wall BLUE = new Wall(Color.BLUE);

    Color color; //Invisible if null
    Image texture;
    PixelReader pixelReader;
    public Wall(Color color) {
        this.color = color;
        texture = new Image(getClass().getResourceAsStream("/textures/benk.png"));
        pixelReader = texture.getPixelReader();
    }

    public boolean isSolid(){
        return this.color != null;
    }

    public Color getColor(double x, double y){
        return pixelReader.getColor((int) (x * texture.getWidth()), (int) (y*texture.getHeight()));
        //return color.deriveColor(x * 255, 1, 1, 1);
    }

}

    