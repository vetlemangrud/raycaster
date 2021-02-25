package spill.game.util;

import spill.game.Level;
import spill.game.Wall;
public class RayCaster {
    //TODO: THIS CODE SMELLS BADLY, BUT WORKS, MAKE IT EASIER TO UNDERSTAND!!!
    private static final int DEPTH_OF_FIELD = 8; //How many blocks the ray can travel max 
    //Thanks to Lode for much inspiration and help https://lodev.org/cgtutor/raycasting.html
    //Also thanks to 3DSage https://www.youtube.com/watch?v=gYRrGTC7GtA&t=467s
    public static RayHit hitWall(Vector origin, Vector direction, Level level){
        
        RayHit horizontalHit; //Point where ray hits a wall (Just checking horizontal walls) (Global position)
        RayHit verticalHit; //Point where ray hits a wall (Just checking vertical walls) (Global position)

        //Check horizontal faces
        if (direction.getAngle() == 0 || direction.getAngle() == Math.PI) {
            //Wont see any horizontal face if looking perpendicular to the y axis
            horizontalHit = new RayHit(new Vector(0,Double.MAX_VALUE), Wall.AIR, Vector.NORTH);
        } else {
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
                if (level.getWall(Vector.add(nextPoint,new Vector(0,-0.1))).getColor() == null && level.getWall(Vector.add(nextPoint,new Vector(0,0.1))).getColor() == null) {
                    nextPoint.add(deltaHorizontal);
                }
                linesCrossed++;
            }
            if (direction.getAngle() < Math.PI) {
                //Looking down
                horizontalHit = new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0,0.1))), Vector.NORTH);
            } else {
                //Looking up
                horizontalHit = new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0,-0.1))), Vector.SOUTH);
            }
            
        }

        //Check vertical faces
        if (direction.getAngle() == Math.PI/4 || direction.getAngle() == Math.PI*6/4) {
            //Wont see any vertical face if looking perpendicular to the x axis
            verticalHit = new RayHit(new Vector(Double.MAX_VALUE,0), Wall.AIR, Vector.WEST);
        } else {
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
                if (level.getWall(Vector.add(nextPoint,new Vector(-0.1,0))).getColor() == null && level.getWall(Vector.add(nextPoint,new Vector(0.1,0))).getColor() == null) {
                    nextPoint.add(deltaVertical);
                }
                linesCrossed++;
            }

            if(direction.getAngle() < Math.PI/2 || direction.getAngle() > Math.PI * 3/2){
                //Looking rigth
                verticalHit = new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(0.1,0))), Vector.WEST);
            } else {
                //Looking left
                verticalHit = new RayHit(nextPoint, level.getWall(Vector.add(nextPoint,new Vector(-0.1,0))), Vector.EAST);
            }
        }

        if (Vector.squaredDistance(origin, verticalHit.getPosition()) < Vector.squaredDistance(origin, horizontalHit.getPosition())) {
            return verticalHit;
        } else {
            return horizontalHit;
        }
    }
}
