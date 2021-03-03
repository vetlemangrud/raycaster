package spill.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Wall {

    public static final Wall AIR = new Wall((Color) null);
    public static final Wall GREEN = new Wall(Color.GREEN);
    public static final Wall BLUE = new Wall(Color.BLUE);

    public static final Wall BRICK = new Wall("brick");
    public static final Wall VINES = new Wall("vines");

    Color color; //Invisible if null
    Image texture;
    PixelReader pixelReader;
    public Wall(Color color) {
        this.color = color;
    }

    public Wall(String textureName) {
        texture = new Image(getClass().getResourceAsStream("/textures/" + textureName + ".png"));
        pixelReader = texture.getPixelReader();
    }

    public boolean isSolid(){
        return color != null || texture != null;
    }

    public Color getColor(double x, double y){
        if (color == null) {
            return pixelReader.getColor((int) (x * texture.getWidth()), (int) (y*texture.getHeight()));
        } else {
            return color;
        }
        
        //return color.deriveColor(x * 255, 1, 1, 1);
    }

}

    