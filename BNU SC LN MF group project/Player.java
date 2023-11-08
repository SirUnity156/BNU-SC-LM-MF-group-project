import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    private String leftKey = "left";
    private String rightKey = "right";
    private String upKey = "up";
    private boolean pressingup;
    private boolean grounded = true;
    private double counterStrafingFrictionMultiplier = 5;
    private int xVel = 0;
    private int yVel = 0;
    private int groundAcc = 2;
    private double groundFriction = 0.2;
    private double airFriction = 0.1;
    public void act()
    {
        applyInputs();
        applyMovement();
    }
    /*private void getInputs() {
        pressingLeft = Greenfoot.isKeyDown(leftKey);
        pressingRight = Greenfoot.isKeyDown(rightKey);
    }*/
    private void applyInputs() {
        boolean pressingLeft = Greenfoot.isKeyDown(leftKey);
        boolean pressingRight = Greenfoot.isKeyDown(rightKey);
        
        
        if(pressingLeft == pressingRight) {
            applyFriction(1);
            return;
        }
        else if((pressingLeft && xVel > 0) || (pressingRight && xVel < 0)){
            applyFriction(counterStrafingFrictionMultiplier);
            return;
        }
        if(pressingLeft) xVel -= groundAcc;
        if(pressingRight) xVel += groundAcc;
    }
    private void applyMovement() {
        setLocation(getX() + xVel, getY() + yVel);
    }
    private void applyFriction(double multiplier) {
        if(grounded) xVel *= 1-(groundFriction*multiplier);
        else xVel *= 1-(airFriction*multiplier);
    }
}
