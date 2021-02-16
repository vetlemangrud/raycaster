package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import spill.game.GameController;

public class BirdseyeRenderer{
    GraphicsContext gc;
    GameController game;
    public BirdseyeRenderer(GraphicsContext gc, GameController game) {
        this.gc = gc;
        this.game = game;
    }

    public void render(){
        gc.fillRect(100, 100, 100, 200);
    }

}
