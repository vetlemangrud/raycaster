package spill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spill.game.util.Vector;

public class VectorTest {
    Vector v;
    Vector u;

    private boolean equalsWithMargin(double target, double actual, double margin){
        return target > actual - margin && target < actual + margin;
    }

    private boolean checkVector(Vector vector, double expectedX, double expectedY){
        return equalsWithMargin(expectedX, vector.getX(), 0.00001) && equalsWithMargin(expectedY, vector.getY(), 0.00001);
    }

    @BeforeEach
    public void setUp() {
        v = new Vector(2,0);
        u = new Vector(-1,-4);
    }

    @Test
    public void testAddition() {
        v.add(u);
        assertEquals(1.0, v.getX());
        assertEquals(-4.0, v.getY());
    }

    @Test
    public void testSubtraction() {
        v.sub(u);
        assertEquals(3.0, v.getX());
        assertEquals(4.0, v.getY());
    }

    @Test
    public void testMultiplication() {
        u.mult(2);
        assertEquals(-2.0, u.getX());
        assertEquals(-8.0, u.getY());
    }

    @Test
    public void angleTest() {
        assertEquals(v.getAngle(), 0);
        assertEquals(Math.PI/2, new Vector(0,1).getAngle());
        assertEquals(Math.PI, new Vector(-1,0).getAngle());
        assertEquals(3*Math.PI/2, new Vector(0,-1).getAngle());
        assertEquals(Math.PI/4, new Vector(20,20).getAngle());
    }

    @Test
    public void getVectorFromAngleAndLengthTest() {
        Vector w = Vector.getVectorFromAngleAndLength(0, 2);
        assertTrue(checkVector(w, 2, 0));

        w = Vector.getVectorFromAngleAndLength(Math.PI/2, 2);
        assertTrue(checkVector(w, 0, 2));

        w = Vector.getVectorFromAngleAndLength(Math.PI, 2);
        assertTrue(checkVector(w, -2, 0));

        w = Vector.getVectorFromAngleAndLength(-Math.PI/2, 2);
        assertTrue(checkVector(w, 0, -2));
    }

    @Test
    public void rotateTest() {
        v.rotate(Math.PI/2);
        assertTrue(checkVector(v, 0, 2));

        v.rotate(Math.PI/2);
        assertTrue(checkVector(v, -2, 0));

        v.rotate(Math.PI/2);
        assertTrue(checkVector(v, 0, -2));

        v.rotate(Math.PI/2);
        assertTrue(checkVector(v, 2, 0));

        v.rotate(Math.PI);
        assertTrue(checkVector(v, -2, 0));

        v.rotate(2 * Math.PI);
        assertTrue(checkVector(v, -2, 0));
    }

    @Test
    public void normalizeTest(){
        v.normalize();
        assertTrue(checkVector(v, 1, 0));

        Vector w = new Vector(2, 2);
        w.normalize();
        assertTrue(checkVector(w, Math.cos(Math.PI/4), Math.sin(Math.PI/4)));

        w = new Vector(-4, 0);
        w.normalize();
        assertTrue(checkVector(w, -1, 0));
        w = new Vector(-4, 4);
        w.normalize();
        assertTrue(checkVector(w, -Math.cos(Math.PI/4), Math.sin(Math.PI/4)));
    }

    @Test
    public void distanceTest(){
        Vector w = new Vector(2,0);
        assertEquals(0, Vector.distance(v, w));

        w = new Vector(4,0);
        assertEquals(2, Vector.distance(v, w));

        w = new Vector(3,1);
        assertEquals(Math.sqrt(2), Vector.distance(v, w));

        w = new Vector(2,2);
        assertEquals(2, Vector.distance(v, w));
    }

    @Test
    public void sqaredDistanceTest(){
        Vector w = new Vector(2,0);
        assertEquals(0, Vector.squaredDistance(v, w));

        w = new Vector(4,0);
        assertEquals(4, Vector.squaredDistance(v, w));

        w = new Vector(3,1);
        assertEquals(2, Vector.squaredDistance(v, w));

        w = new Vector(2,2);
        assertEquals(4, Vector.squaredDistance(v, w));
    }

    @Test
    public void angleSubTest(){
        Vector w = new Vector(2,0);
        assertEquals(0, Vector.angleSub(v, w));

        w.mult(2);
        assertEquals(0, Vector.angleSub(v, w));

        v.rotate(Math.PI/2);
        assertEquals(Math.PI/2, Vector.angleSub(v, w));

        w.rotate(Math.PI*3/2);
        assertEquals(-Math.PI, Vector.angleSub(v, w));
    }

    @Test
    public void copyTest(){
        Vector w = v.copy();
        v.rotate(Math.PI/2);
        assertTrue(checkVector(w, 2, 0));

        w = v.copy();
        v.rotate(Math.PI/2);
        v.mult(2);
        assertTrue(checkVector(w, 0, 2));
    }

    @Test
    public void getComponentTest(){
        assertTrue(checkVector(v.getXComponent(), 2, 0));
        assertTrue(checkVector(v.getYComponent(), 0, 0));
        assertTrue(checkVector(u.getXComponent(), -1, 0));
        assertTrue(checkVector(u.getYComponent(), 0, -4));
    }
    
}
