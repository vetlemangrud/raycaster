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
    private RayHit[] rayHits;
    
    
    public RaycastRenderer(GraphicsContext gc, Game gameContext, ImageView gameImageView, double canvasWidth, double canvasHeight) {
        super(gc, gameContext, canvasWidth, canvasHeight);
        this.gameImageView = gameImageView;
        rayHits = new RayHit[RAY_COUNT];
    }

    @Override
    public void render() {
        WritableImage currentFrame = new WritableImage((int) gameImageView.getFitWidth(), (int) gameImageView.getFitHeight());
        currentPixelWriter = currentFrame.getPixelWriter();
        castRays();

        gc.clearRect(0,0,canvasWidth,canvasHeight);
        
        drawFloorAndRoof();

        drawWalls();
        gameImageView.setImage(currentFrame);
    }

    private void castRays(){
        for (int i = 0; i < RAY_COUNT; i++) {
            rayHits[i] = RayCaster.hitWall(game.getPlayer().getPos(), game.getPlayer().getDirection().copy().rotate(FOV * i/RAY_COUNT - FOV/2), game.getCurrentLevel());
        }
    }

    private void drawFloorAndRoof(){
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight/2);
        gc.setFill(Color.BROWN);
        gc.fillRect(0, canvasHeight/2, canvasWidth, canvasHeight/2);
        
    }

    private void drawWalls(){
        for (int screenX = 0; screenX < canvasWidth; screenX++) {
            RayHit hit = rayHits[(int) ((screenX/canvasWidth) * ((double)RAY_COUNT))];
            double rayDistance = Vector.distance(game.getPlayer().getPos(), hit.getPosition());
            double fisheyeCorrectedDistance = rayDistance * Math.cos(game.getPlayer().getDirection().getAngle() - Vector.sub(hit.getPosition(), game.getPlayer().getPos()).getAngle());
            double lineHeight = canvasHeight/fisheyeCorrectedDistance;
            
            double yOffset = 0;
            if (lineHeight > canvasHeight) {
                yOffset = lineHeight/2 - canvasHeight/2;
                lineHeight = canvasHeight;
            }

            for (int y = 0; y < lineHeight; y++) {
                double screenY = canvasHeight/2 - lineHeight/2 + y;
                Color pixelColor = hit.getWall().getColor(hit.getWallX(),(y+yOffset)/(lineHeight+ 2*yOffset));
                if (hit.getFace() == Vector.NORTH || hit.getFace() == Vector.SOUTH) {
                    pixelColor = hit.getWall().getColor(hit.getWallX(),(y+yOffset)/(lineHeight+ 2*yOffset));
                }
                currentPixelWriter.setColor((int)screenX, (int)screenY, pixelColor);
                
            }
        }
    }
}
