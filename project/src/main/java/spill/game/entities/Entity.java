package spill.game.entities;

import javafx.scene.image.Image;
import spill.game.util.Vector;

public class Entity{
    private Vector pos;
    private Image sprite;

    public Entity(Vector startPosition, String spriteName){
        sprite = new Image(getClass().getResourceAsStream("/sprites/" + spriteName + ".png"));
        this.pos = startPosition;
    }

    public Vector getPos(){
        return pos.copy();
    }

    public Image getSprite(){
        return sprite;
    }
}
