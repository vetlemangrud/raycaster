package spill.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Wall {

    public static final Wall AIR = new Wall((Color) null);
    public static final Wall GREEN = new Wall(Color.GREEN);
    public static final Wall BLUE = new Wall(Color.BLUE);

    public static final Wall BRICK = new Wall("brick");
    public static final Wall VINES = new Wall("vines");

    Color color; //Invisible if null
    Color darkerColor; //Performance seemed to drop significantly when modifying colors on the fly
    Image texture;
    PixelReader pixelReader;
    WritableImage darkerTexture;
    PixelReader darkerReader;
    public Wall(Color color) {
        this.color = color;
        if (color != null) {
            this.darkerColor = color.darker();
        }
    }

    public Wall(String textureName) {
        texture = new Image(getClass().getResourceAsStream("/textures/" + textureName + ".png"));
        pixelReader = texture.getPixelReader();

        darkerTexture = new WritableImage((int)texture.getWidth(),(int)texture.getHeight());
        darkerReader = darkerTexture.getPixelReader();

        PixelWriter darkerWriter = darkerTexture.getPixelWriter();
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                darkerWriter.setColor(x, y, pixelReader.getColor(x, y).deriveColor(0, 1, 0.5, 1));
            }
        }
    }

    public boolean isSolid(){
        return color != null || texture != null;
    }

    public Color getColor(double x, double y) {
        if (color == null) {
            return pixelReader.getColor((int) (x * texture.getWidth()), (int) (y*texture.getHeight()));
        } else {
            return color;
        }
    }

    public Color getDarkerColor(double x, double y) {
        if (darkerColor == null) {
            return darkerReader.getColor((int) (x * darkerTexture.getWidth()), (int) (y*darkerTexture.getHeight()));
        } else {
            return darkerColor;
        }
    }

}

    