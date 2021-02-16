package spill.rendering;

import spill.game.Level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import spill.game.GameController;

public class BirdseyeRenderer implements Renderer {
    GraphicsContext gc;
    GameController game;
    public BirdseyeRenderer(GraphicsContext gc, GameController game) {
        this.gc = gc;
        this.game = game;
    }

    public void render(){
        gc.save();
        gc.clearRect(0,0,game.getCanvasWidth(),game.getCanvasHeight());

        Level level = game.getCurrentLevel();
        double scaleFactor = Math.min(game.getCanvasWidth()/level.getWidth(), game.getCanvasHeight()/level.getHeight());
        
        //Transform so that drawing between 0 and 1 in the x and y axis fits to canvas
        gc.translate((game.getCanvasWidth() - level.getWidth() * scaleFactor)/2, (game.getCanvasHeight() - level.getHeight() * scaleFactor)/2);
        gc.scale(scaleFactor, scaleFactor);

        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                gc.setFill(Color.BLUE);
                gc.setFill(level.getWall(x, y).getColor());
                gc.fillRect(x, y, 1, 1);
            }
        }
        gc.restore();
    }

}
