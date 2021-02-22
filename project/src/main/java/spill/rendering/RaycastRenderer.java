package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import spill.game.Game;
import spill.game.util.RayCaster;
import spill.game.util.RayHit;
import spill.game.util.Vector;

public class RaycastRenderer extends Renderer {
    private static final int RAY_COUNT = 200;
    private static final double FOV = Math.PI/6;
    
    public RaycastRenderer(GraphicsContext gc, Game gameContext, double canvasWidth, double canvasHeight) {
        super(gc, gameContext, canvasWidth, canvasHeight);
    }

    @Override
    public void render() {
        gc.clearRect(0,0,canvasWidth,canvasHeight);
       
        for (int i = 0; i < RAY_COUNT; i++) {
            RayHit hit = RayCaster.hitWall(game.getPlayer().getPos(), game.getPlayer().getDirection().copy().rotate(FOV * i/RAY_COUNT - FOV/2), game.getCurrentLevel());
            double rayDistance = Vector.distance(game.getPlayer().getPos(), hit.getPosition());
            double fisheyeCorrectedDistance = rayDistance * Math.cos(game.getPlayer().getDirection().getAngle() - hit.getAngle());
            double lineHeight = canvasHeight/fisheyeCorrectedDistance;
            gc.setFill(hit.getWall().getColor().deriveColor(0, 1, 2* hit.getAngle()/Math.PI, 1));
            gc.fillRect(canvasWidth * i/RAY_COUNT, canvasHeight/2 - lineHeight/2, canvasWidth/RAY_COUNT, lineHeight);
        }
    }
}
