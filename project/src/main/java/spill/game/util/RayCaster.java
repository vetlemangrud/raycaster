package spill.game.util;

import spill.game.Level;
import spill.game.Wall;
public class RayCaster {
    //Thanks to Lode for much inspiration and help https://lodev.org/cgtutor/raycasting.html
    //Also thanks to 3DSage https://www.youtube.com/watch?v=gYRrGTC7GtA&t=467s

    private static final int DEPTH_OF_FIELD = 8; //How many blocks the ray can travel max 
    
    public static RayHit hitWall(Vector origin, Vector direction, Level level){
        RayHit horizontalHit = hitHorizontalWall(origin, direction, level); //Point where ray hits a wall (Just checking horizontal walls) (Global position)
        RayHit verticalHit = hitVerticalWall(origin, direction, level); //Point where ray hits a wall (Just checking vertical walls) (Global position)
        
        //Return the closest hit
        if (Vector.squaredDistance(origin, verticalHit.getPosition()) < Vector.squaredDistance(origin, horizontalHit.getPosition())) {
            return verticalHit;
        } else {
            return horizontalHit;
        }
    }

    private static RayHit hitHorizontalWall(Vector origin, Vector direction, Level level) {
        //Check horizontal faces
        if (direction.getAngle() == 0 || direction.getAngle() == Math.PI) {
            //Wont see any horizontal face if looking perpendicular to the y axis
            return new RayHit(new Vector(0,Double.MAX_VALUE), Wall.AIR, Vector.NORTH);
        }
        double closestY;
        double deltaY;
        if(direction.getAngle() < Math.PI){
            //Looking down
            closestY = Math.ceil(origin.getY()); 
            deltaY = 1;
        } else {
            //Looking up
            closestY = Math.floor(origin.getY()); 
            deltaY = -1;
        }

        Vector nextPoint = Vector.mult(direction, (closestY-origin.getY())/direction.getY()).add(origin); //First point it can hit (Global position)
        Vector deltaHorizontal = Vector.mult(direction, deltaY/direction.getY()); //Vector between horizontal lines (Relative position)

        double linesCrossed = 0;
        while (linesCrossed < DEPTH_OF_FIELD) {
            if (level.getWall(Vector.add(nextPoint,new Vector(0,-0.1))).getColor(0,0) == null && level.getWall(Vector.add(nextPoint,new Vector(0,0.1))).getColor(0,0) == null) {
                nextPoint.add(deltaHorizontal);
            }
            linesCrossed++;
        }
        if (direction.getAngle() < Math.PI) {
            //Looking down
            return new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0,0.1))), Vector.NORTH);
        } else {
            //Looking up
            return new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0,-0.1))), Vector.SOUTH);
        }
            
        
    }

    private static RayHit hitVerticalWall(Vector origin, Vector direction, Level level) { 
        //Check vertical faces
        if (direction.getAngle() == Math.PI/4 || direction.getAngle() == Math.PI*6/4) {
            //Wont see any vertical face if looking perpendicular to the x axis
            return new RayHit(new Vector(Double.MAX_VALUE,0), Wall.AIR, Vector.WEST);
        }

        double closestX;
        double deltaX;
        if(direction.getAngle() < Math.PI/2 || direction.getAngle() > Math.PI * 3/2){
            //Looking rigth
            closestX = Math.ceil(origin.getX()); 
            deltaX = 1;
        } else {
            //Looking left
            closestX = Math.floor(origin.getX()); 
            deltaX = -1;
        }

        Vector nextPoint = Vector.mult(direction, (closestX-origin.getX())/direction.getX()).add(origin); //First point it can hit (Global position)
        Vector deltaVertical = Vector.mult(direction, deltaX/direction.getX()); //Vector between vertical lines (Relative position)

        double linesCrossed = 0;
        while (linesCrossed < DEPTH_OF_FIELD) {
            if (level.getWall(Vector.add(nextPoint,new Vector(-0.1,0))).getColor(0,0) == null && level.getWall(Vector.add(nextPoint,new Vector(0.1,0))).getColor(0,0) == null) {
                nextPoint.add(deltaVertical);
            }
            linesCrossed++;
        }

        if(direction.getAngle() < Math.PI/2 || direction.getAngle() > Math.PI * 3/2){
            //Looking rigth
            return new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0.1,0))), Vector.WEST);
        } else {
            //Looking left
            return new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(-0.1,0))), Vector.EAST);
        }
        
    }
}
