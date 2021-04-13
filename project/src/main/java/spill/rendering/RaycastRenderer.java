package spill.rendering;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import spill.game.Entity;
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
        drawEntities();
        gameImageView.setImage(currentFrame);

    }

    @Override
    public void clear(){
        gameImageView.setImage(new WritableImage((int) gameImageView.getFitWidth(), (int) gameImageView.getFitHeight()));
        gc.clearRect(0,0,canvasWidth,canvasHeight);
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

            //Wall
            for (int y = 0; y < lineHeight; y++) {
                double screenY = canvasHeight/2 - lineHeight/2 + y;
                Color pixelColor = hit.getWall().getColor(hit.getWallX(),(y+yOffset)/(lineHeight+ 2*yOffset));
                if (hit.getFace() == Vector.NORTH || hit.getFace() == Vector.SOUTH) {
                    pixelColor = hit.getWall().getDarkerColor(hit.getWallX(),(y+yOffset)/(lineHeight+ 2*yOffset));
                }
                currentPixelWriter.setColor((int)screenX, (int)screenY, pixelColor);
                
            }

            // //Floor (EPIC FAIL)
            // double minDistToViewableFloor = 0.5; //All floor closer than this is outside the viewing cone
            // double rayAngle = Vector.sub(hit.getPosition(), game.getPlayer().getPos()).getAngle();
            // double distToViewableFloor = minDistToViewableFloor / Math.cos(rayAngle - game.getPlayer().getDirection().getAngle());
            // Vector closestViewableFloor = Vector.add(game.getPlayer().getPos(), Vector.getVectorFromAngleAndLength(rayAngle, distToViewableFloor));
            // for (int y = 0; y < canvasHeight/2 - lineHeight/2; y++) {
            //     double screenY = canvasHeight - canvasHeight/2 + lineHeight/2 + y;
            //     Vector floorLocation = Vector.add(hit.getPosition(), Vector.sub(closestViewableFloor, hit.getPosition()).mult(y/(canvasHeight/2 - lineHeight/2)));
            //     Color pixelColor = Color.BLUE;
            //     if (Math.abs(floorLocation.getX() - Math.floor(floorLocation.getX())) < 0.05) {
            //         pixelColor = Color.GREEN;
            //     }
            //     if (Math.abs(floorLocation.getY() - Math.floor(floorLocation.getY())) < 0.05) {
            //         pixelColor = Color.RED;
            //     }
            //     currentPixelWriter.setColor((int)screenX, (int)screenY, pixelColor);
                
            // }
        }
    }

    private void drawEntities(){
        //The technique is based on https://lodev.org/cgtutor/raycasting3.html, but some steps are changed

        //Sorting entities by distance to player (Closest last)
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : game.getCurrentLevel().getEntities()) {
            entities.add(entity);
        }
        entities.sort((a, b) -> {
            double result = Vector.squaredDistance(b.getPos(), game.getPlayer().getPos()) - Vector.squaredDistance(a.getPos(), game.getPlayer().getPos());
            if (result > 0) {
                return 1;
            } else if (result < 0) {
                return -1;
            } else {
                return 0;
            }
        });


        //Draw entities
        for (Entity entity : entities) {
            drawEntity(entity);
        }
        
        
    }

    private void drawEntity(Entity entity){
        Vector relativePosition = Vector.sub(entity.getPos(),game.getPlayer().getPos()); //Position relative to player
            double centerScreenX = (Vector.angleSub(relativePosition,game.getPlayer().getDirection())/FOV+0.5)*canvasWidth; //Where the center of the sprite is on screen x
            
            //Settng the sprite size the same way we set the wall line height (Sprite should be the same height as a wall)
            double spriteSize = canvasHeight / relativePosition.getLength();
            double spriteScaling = spriteSize/entity.getSprite().getHeight();
            double scaledHeight = spriteScaling * entity.getSprite().getHeight();
            double scaledWidth = spriteScaling * entity.getSprite().getWidth();
            int leftScreenX = (int) (centerScreenX - entity.getSprite().getWidth() * spriteScaling/2); //X position of leftmost pixels for this sprite
            int topScreenY = (int) (canvasHeight / 2 - spriteSize / 2); // Y position of top pixels for this sprite

            //Draw sprite to screen
            //This code is smelling real bad 🤢
            //TODO:Make sprite not move around wierdly
            PixelReader spritePixelReader = entity.getSprite().getPixelReader();
            for (int x = 0; x < scaledWidth; x++) {
                int screenX = leftScreenX + x;
                if (screenX >= 0 && screenX < canvasWidth) {
                    if(Vector.distance(rayHits[(int) (screenX * RAY_COUNT/canvasWidth)].getPosition(), game.getPlayer().getPos()) > Vector.distance(entity.getPos(), game.getPlayer().getPos())){
                        for (int y = 0; y < scaledHeight; y++) {
                            int screenY = topScreenY + y;
                            if (screenY >= 0 && screenY < canvasHeight) {
                                Color pixelColor = spritePixelReader.getColor((int) (x/spriteScaling), (int) (y/spriteScaling));
                                if (pixelColor.isOpaque()) {
                                    currentPixelWriter.setColor(screenX, screenY, pixelColor);
                                }
                            }
                        }
                    }
                
                }
            }
    }
}
