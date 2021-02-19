package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import spill.game.Game;
import spill.game.Level;
import spill.game.util.RayCaster;
import spill.game.util.Vector;

public class BirdseyeRenderer implements Renderer {
    GraphicsContext gc;
    Game game;
    double canvasWidth;
    double canvasHeight;

    public BirdseyeRenderer(GraphicsContext gc, Game gameContext, double canvasWidth, double canvasHeight) {
        this.gc = gc;
        this.game = gameContext;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void render(){
        gc.save();
        gc.clearRect(0,0,canvasWidth,canvasHeight);

        Level level = game.getCurrentLevel();
        double scaleFactor = Math.min(canvasWidth/level.getWidth(), canvasHeight/level.getHeight());
        
        //Transform so that drawing between 0 and width/height fo level in the x and y axis fits to canvas
        gc.translate((canvasWidth - level.getWidth() * scaleFactor)/2, (canvasHeight - level.getHeight() * scaleFactor)/2);
        gc.scale(scaleFactor, scaleFactor);

        //Draw map
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                gc.setFill(Color.WHITE);
                gc.setFill(level.getWall(x, y).getColor());
                gc.fillRect(x, y, 1, 1);
            }
        }

        //Draw player
        gc.save();
        gc.translate(game.getPlayer().getPos().getX(), game.getPlayer().getPos().getY());
        gc.rotate(game.getPlayer().getDirection().getAngle());
        gc.setFill(Color.RED);
        gc.fillRoundRect(-0.2, -0.2, 0.4, 0.4, 0.4, 0.4);
        gc.setLineWidth(0.1);
        gc.strokeLine(0, 0, 0.2, 0);
        gc.restore();

        //Draw ray
        gc.setLineWidth(0.1);
        Vector closestHorizontal = RayCaster.hitWall(game.getPlayer().getPos(), game.getPlayer().getDirection(), level);
        gc.strokeLine(game.getPlayer().getPos().getX(), game.getPlayer().getPos().getY(), closestHorizontal.getX(), closestHorizontal.getY());

        gc.restore();
    }

}
