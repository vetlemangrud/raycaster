package spill.game.util;

import javafx.geometry.Point2D;

public class Vector{
    //Point2D works well, but i wanted something more like Vectors in Processing
    //Vector is basically just a wrapper around Point2D

    private Point2D point;
    public Vector(double x, double y) {
        point = new Point2D(x,y);
    }

    public Vector(Point2D point) {
        this.point = point;
    }

    public static Vector getVectorFromAngleAndLength(double angle, double length){
        double x = length * Math.cos(Math.toRadians(angle));
        double y = length * Math.sin(Math.toRadians(angle));
        return new Vector(x,y);
    }

    //Making both static and non-static versions of basic functions
    static Vector add(Vector a, Vector b) {
        return new Vector(a.point.add(b.point));
    }
    
    public Vector add(Vector b) {
        Vector c = Vector.add(this, b);
        this.point = c.point;
        return c;
    }

    static Vector mult(Vector a, double b) {
        return new Vector(a.point.multiply(b));
    }

    public Vector add(double b) {
        Vector c = Vector.mult(this, b);
        this.point = c.point;
        return c;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public void setX(double x) {
        point = new Point2D(x, getY());
    }

    public void setY(double y) {
        point = new Point2D(getX(), y);
    }

}
