package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import spill.game.Game;
import spill.game.util.RayCaster;
import spill.game.util.RayHit;
import spill.game.util.Vector;

public class RaycastRenderer extends Renderer {
    private static final int RAY_COUNT = 800;
    private static final double FOV = Math.PI/2.5;

    private ImageView gameImageView;
    private PixelWriter currentPixelWriter;
    
    
    public RaycastRenderer(GraphicsContext gc, Game gameContext, ImageView gameImageView, double canvasWidth, double canvasHeight) {
        super(gc, gameContext, canvasWidth, canvasHeight);
        this.gameImageView = gameImageView;
    }

    @Override
    public void render() {
        WritableImage currentFrame = new WritableImage((int) gameImageView.getFitWidth(), (int) gameImageView.getFitHeight());
        currentPixelWriter = currentFrame.getPixelWriter();

        gc.clearRect(0,0,canvasWidth,canvasHeight);
        
        drawFloorAndRoof();

        drawWalls();
        gameImageView.setImage(currentFrame);
    }

    private void drawFloorAndRoof(){
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight/2);
        gc.setFill(Color.BROWN);
        gc.fillRect(0, canvasHeight/2, canvasWidth, canvasHeight/2);
        for (int x = 0; x < canvasWidth; x++) {
            for (int y = 0; y < canvasHeight; y++) {
                if ((x + y) % 5 == 0) {
                    currentPixelWriter.setColor(x, y, Color.BLACK);
                }
                
            }
        }
        
    }

    private void drawWalls(){
        for (int i = 0; i < RAY_COUNT; i++) {
            RayHit hit = RayCaster.hitWall(game.getPlayer().getPos(), game.getPlayer().getDirection().copy().rotate(FOV * i/RAY_COUNT - FOV/2), game.getCurrentLevel());
            double rayDistance = Vector.distance(game.getPlayer().getPos(), hit.getPosition());
            double fisheyeCorrectedDistance = rayDistance * Math.cos(game.getPlayer().getDirection().getAngle() - Vector.sub(hit.getPosition(), game.getPlayer().getPos()).getAngle());
            double lineHeight = canvasHeight/fisheyeCorrectedDistance;

            if (hit.getFace() == Vector.EAST || hit.getFace() == Vector.WEST) {
                gc.setFill(hit.getWall().getColor());
            } else {
                gc.setFill(hit.getWall().getColor().deriveColor(0, 1, 0.5, 1));
            }
            gc.fillRect(canvasWidth * i/RAY_COUNT, canvasHeight/2 - lineHeight/2 , canvasWidth/RAY_COUNT, lineHeight);
            
        }
    }
}
