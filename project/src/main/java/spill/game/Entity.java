package spill.game;

import javafx.scene.image.Image;
import spill.game.util.Vector;

public class Entity{
    private Vector pos;
    private Image sprite;
    private Level level;

    public Entity(Level level, Vector startPosition, String spriteName){
        sprite = new Image(getClass().getResourceAsStream("/sprites/" + spriteName + ".png"));
        this.level = level;
        this.pos = startPosition;
    }

    public Vector getPos(){
        return pos;
    }

    public Image getSprite(){
        return sprite;
    }
}
