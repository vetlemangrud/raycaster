package spill.game.util;

import javafx.geometry.Point2D;

public class Vector{
    //Point2D works well, but i wanted something more like Vectors in Processing
    //Vector is basically just a wrapper around Point2D

    private Point2D point;
    public Vector(double x, double y) {
        point = new Point2D(x,y);
    }

    public Vector() {
        this(0,0);
    }

    public Vector(Point2D point) {
        this.point = point;
    }

    public static Vector getVectorFromAngleAndLength(double angle, double length){
        double x = length * Math.cos(angle);
        double y = length * Math.sin(angle);
        return new Vector(x,y);
    }

    //Making both static and non-static versions of basic functions
    public static Vector add(Vector a, Vector b) {
        return new Vector(a.point.add(b.point));
    }
    
    public Vector add(Vector b) {
        Vector c = Vector.add(this, b);
        point = c.point;
        return c;
    }

    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.point.subtract(b.point));
    }
    
    public Vector sub(Vector b) {
        Vector c = Vector.sub(this, b);
        point = c.point;
        return c;
    }

    public static Vector mult(Vector a, double b) {
        return new Vector(a.point.multiply(b));
    }

    public Vector mult(double b) {
        Vector c = Vector.mult(this, b);
        point = c.point;
        return c;
    }

    public static Vector rotate(Vector a, double angle) {
        return getVectorFromAngleAndLength(a.getAngle() + angle, a.getLength());
    }

    public Vector rotate(double angle){
        Vector c = Vector.rotate(this, angle);
        point = c.point;
        return c;
    }

    public static Vector normalize(Vector a){
        return getVectorFromAngleAndLength(a.getAngle(), 1);
    }

    public Vector normalize(){
        Vector c = Vector.normalize(this);
        point = c.point;
        return c;
    }

    public static double distance(Vector a, Vector b){
        return Math.abs(a.copy().sub(b).getLength());
    }

    public Vector copy(){
        return new Vector(getX(),getY());
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getLength(){
        return Point2D.ZERO.distance(point);
    }

    public double getAngle(){
        return (Math.atan2(point.getY(), point.getX()) + 2 * Math.PI) % (2 * Math.PI); //Modulo is so wierd sometimes
    }

    public Vector getXComponent(){
        return new Vector(point.getX(),0);
    }

    public Vector getYComponent(){
        return new Vector(0,point.getY());
    }

    public void setX(double x) {
        point = new Point2D(x, getY());
    }

    public void setY(double y) {
        point = new Point2D(getX(), y);
    }

}
