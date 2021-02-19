package spill.game.util;

import spill.game.Level;
public class RayCaster {
    private static final int DEPTH_OF_FIELD = 8;
    //Thanks to Lode for much inspiration and help https://lodev.org/cgtutor/raycasting.html
    //Also thanks to 3DSage https://www.youtube.com/watch?v=gYRrGTC7GtA&t=467s
    public static Vector hitWall(Vector origin, Vector direction, Level level){
        System.out.println(direction.getAngle());
        Vector closestHorizontal; //First point it can hit
        Vector deltaHorizontal; //Vector between horizontal lines

        //Check horizontal faces
        if (direction.getAngle() == 0 || Math.abs(direction.getAngle()) == 180) {
            //Wont see any horizontal face if looking perpendicular to the y axis
            deltaHorizontal = new Vector(0,0);
            closestHorizontal = new Vector(0,0);
        } else {
            double closestY;
            double deltaY;
            if(direction.getAngle() > 0){
                //Looking down
                closestY = Math.ceil(origin.getY()); 
                deltaY = 1;
            } else {
                //Looking up
                closestY = Math.floor(origin.getY()); 
                deltaY = -1;
            }
            deltaHorizontal = new Vector(deltaY * 1/Math.tan(Math.toRadians(direction.getAngle())), deltaY);
            closestHorizontal = new Vector(closestY * 1/Math.tan(Math.toRadians(direction.getAngle())) + origin.getX(), closestY);
        }
        return closestHorizontal;

    }
}
