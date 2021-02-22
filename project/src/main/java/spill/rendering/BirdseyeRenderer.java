package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import spill.game.Game;
import spill.game.Level;
import spill.game.util.RayCaster;
import spill.game.util.Vector;

public class BirdseyeRenderer extends Renderer {

    public BirdseyeRenderer(GraphicsContext gc, Game gameContext, double canvasWidth, double canvasHeight) {
        super(gc, gameContext, canvasWidth, canvasHeight);
    }

    @Override
    public void render() {
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

        //Draw ray
        gc.setLineWidth(0.01);
        for (int i = 0; i < 100; i++) {
            Vector hit = RayCaster.hitWall(game.getPlayer().getPos(), game.getPlayer().getDirection().copy().rotate(Math.PI/2 * ((double)i)/100 - Math.PI/4), level).getPosition();
            gc.strokeLine(game.getPlayer().getPos().getX(), game.getPlayer().getPos().getY(), hit.getX(), hit.getY());
        }

        //Draw player
        gc.save();
        gc.translate(game.getPlayer().getPos().getX(), game.getPlayer().getPos().getY());
        gc.rotate(Math.toDegrees(game.getPlayer().getDirection().getAngle()));
        gc.setFill(Color.RED);
        gc.fillRoundRect(-0.2, -0.2, 0.4, 0.4, 0.4, 0.4);
        gc.setLineWidth(0.1);
        gc.strokeLine(0, 0, 0.2, 0);
        gc.restore();
        

        gc.restore();
    }

}
