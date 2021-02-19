package spill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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

    @BeforeEach
    public void setUp() {
        v = new Vector(2,0);
        u = new Vector(-1,-4);
    }

    @Test
    void testAddition() {
        v.add(u);
        assertEquals(1.0, v.getX());
        assertEquals(-4.0, v.getY());
    }

    @Test
    void testSubtraction() {
        v.sub(u);
        assertEquals(3.0, v.getX());
        assertEquals(4.0, v.getY());
    }

    @Test
    void testMultiplication() {
        u.mult(2);
        assertEquals(-2.0, u.getX());
        assertEquals(-8.0, u.getY());
    }

    @Test
    void angleTest() {
        assertEquals(v.getAngle(), 0);
        assertEquals(Math.PI/2, new Vector(0,1).getAngle());
        assertEquals(Math.PI, new Vector(-1,0).getAngle());
        assertEquals(3*Math.PI/2, new Vector(0,-1).getAngle());
        assertEquals(Math.PI/4, new Vector(20,20).getAngle());
    }

    @Test
    void getVectorFromAngleAndLengthTest() {
        Vector w = Vector.getVectorFromAngleAndLength(0, 2);
        assertEquals(2, w.getX());
        assertEquals(0, w.getY());

        w = Vector.getVectorFromAngleAndLength(Math.PI/2, 2);
        assertTrue(equalsWithMargin(0, w.getX(), 0.01));
        assertTrue(equalsWithMargin(2, w.getY(), 0.01));

        w = Vector.getVectorFromAngleAndLength(Math.PI, 2);
        assertTrue(equalsWithMargin(-2, w.getX(), 0.00001));
        assertTrue(equalsWithMargin(0, w.getY(), 0.00001));

        w = Vector.getVectorFromAngleAndLength(-Math.PI/2, 2);
        assertTrue(equalsWithMargin(0, w.getX(), 0.00001));
        assertTrue(equalsWithMargin(-2, w.getY(), 0.00001));
    }

    @Test
    void rotateTest() {
        v.rotate(Math.PI/2);
        assertTrue(equalsWithMargin(0, v.getX(), 0.00001));
        assertTrue(equalsWithMargin(2, v.getY(), 0.00001));

        v.rotate(Math.PI/2);
        assertTrue(equalsWithMargin(-2, v.getX(), 0.00001));
        assertTrue(equalsWithMargin(0, v.getY(), 0.00001));

        v.rotate(Math.PI/2);
        assertTrue(equalsWithMargin(0, v.getX(), 0.00001));
        assertTrue(equalsWithMargin(-2, v.getY(), 0.00001));

        v.rotate(Math.PI/2);
        assertTrue(equalsWithMargin(2, v.getX(), 0.00001));
        assertTrue(equalsWithMargin(0, v.getY(), 0.00001));
    }

}
