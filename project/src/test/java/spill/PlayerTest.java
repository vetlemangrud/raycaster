package spill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spill.game.Exit;
import spill.game.Level;
import spill.game.Player;
import spill.game.Wall;
import spill.game.entities.Entity;
import spill.game.util.Vector;

public class PlayerTest {
    Player testPlayer;
    Level testLevel;

    @BeforeEach
    public void setUp(){
        //Setting up a 20x20 level with brick walls (Except top corner)
        Wall[][] walls = new Wall[20][20];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if ((x == 0 || y == 0 || x == 19 || y == 19) && (x != 0 || y != 0)) {
                    walls[x][y] = Wall.BRICK;
                } else {
                    walls[x][y] = Wall.AIR;
                }
            }
        }
        testLevel = new Level(0, walls, new ArrayList<Entity>(), new ArrayList<Exit>(), new Vector(10,10), "jazz");
        testPlayer = new Player(testLevel, 10, 10, 0);
    }

    private boolean matchPlayerPos(Player player, double expectedX, double expectedY){
        return Math.abs(player.getPos().getX() - expectedX) < 0.00001 && Math.abs(player.getPos().getY() - expectedY) < 0.00001; //Wiggle room when moving in weird angles
    }

    @Test
    public void walkForward(){
        testPlayer.forward(1);
        assertTrue(matchPlayerPos(testPlayer, 10 + Player.SPEED, 10));
        testPlayer.forward(0.5);
        assertTrue(matchPlayerPos(testPlayer, 10 + 1.5 * Player.SPEED, 10));
    }

    @Test 
    public void walkBackward(){
        testPlayer.backward(1);
        assertTrue(matchPlayerPos(testPlayer, 10 - Player.SPEED, 10));
        testPlayer.backward(0.5);
        assertTrue(matchPlayerPos(testPlayer, 10 - 1.5 * Player.SPEED, 10));
    }

    @Test
    public void turn(){
        testPlayer.turnRight(1);
        assertEquals(Player.TURNSPEED, testPlayer.getDirection().getAngle());
        testPlayer.turnRight(0.5);
        assertEquals(1.5 * Player.TURNSPEED, testPlayer.getDirection().getAngle());

        testPlayer.turnLeft(1);
        assertEquals(0.5 * Player.TURNSPEED, testPlayer.getDirection().getAngle());
        testPlayer.turnLeft(2);
        assertEquals(2*Math.PI - 1.5 * Player.TURNSPEED, testPlayer.getDirection().getAngle());
    }

    @Test
    public void turningAndMoving(){
        //Moving the player in a triangle
        
        testPlayer.turnRight(0.5);
        testPlayer.forward(0.4);
        assertTrue(matchPlayerPos(testPlayer, 10 + 0.4 * Player.SPEED * Math.cos(0.5 * Player.TURNSPEED), 10 + 0.4 * Player.SPEED * Math.sin(0.5 * Player.TURNSPEED)));
        testPlayer.turnLeft(1 + Math.PI/Player.SPEED);
        testPlayer.backward(0.4);
        assertTrue(matchPlayerPos(testPlayer, 10 + 2 * 0.4 * Player.SPEED * Math.cos(0.5 * Player.TURNSPEED), 10));
    
    }

    @Test
    public void straightCollisionTest(){
        //Simulate walking 999 times with random time intervals (Lag should not break collision)
        for (int i = 0; i < 999; i++) {
            testPlayer.forward(Math.random() * 10);
        }
        assertTrue(testPlayer.getPos().getX() < 19);
    }

    @Test
    public void cheekyCollisionTest(){
        //Trying to walk into the empty corner
        testPlayer.turnLeft(0.75 * Math.PI/Player.TURNSPEED);
        //Simulate walking 999 times with random time intervals (Lag should not break collision)
        for (int i = 0; i < 999; i++) {
            testPlayer.forward(Math.random() * 10);
        }
        assertTrue(testPlayer.getPos().getX() > 1);
        assertTrue(testPlayer.getPos().getY() > 1);
    }

    @Test
    public void runningAroundCollisionTest(){
        //Moving around randomly (Checking if randomness escapes)
        for (int i = 0; i < 999; i++) {
            testPlayer.turnRight(Math.random());
            testPlayer.turnLeft(Math.random());
            testPlayer.forward(Math.random());
            testPlayer.backward(Math.random());

            assertTrue(testPlayer.getPos().getX() > 1);
            assertTrue(testPlayer.getPos().getY() > 1);
            assertTrue(testPlayer.getPos().getX() < 19);
            assertTrue(testPlayer.getPos().getY() < 19);
        }
    }

}
