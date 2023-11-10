import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**--Planned Development--
 * No double jump from holding up
 * Variable jump height
 * Coyote time & jump buffer
 * Slide
 * Decrease gravity at jump apex
 * Max speed (decrease accel proportionally to speed relative to max speed)
 */

public class Player extends Actor
{
    private String leftKey = "left";
    private String rightKey = "right";
    private String upKey = "up";
    private boolean pressingUp = false;
    private boolean pressingLeft = false;
    private boolean pressingRight = false;
    private boolean grounded = false;
    private double counterStrafingFrictionMultiplier = 5;
    private int xVel = 0;
    private int yVel = 0;
    private double groundAcc = 2;
    private double airAcc = 1;
    private double groundFriction = 0.2;
    private double airFriction = 0.1;
    private int jumpSpeed = 25;
    private int jumps = 1;
    private int remainingJumps = jumps;
    private int jumpCooldownTicks = 5;
    private int ticksSinceLastJump = 0;
    private int maxXSpeed = 15;

    public void act()
    {
        grounded = isGrounded();
        getInputs();
        applyInputs();
        applyMovement();
    }
    private void getInputs() {
        pressingLeft = Greenfoot.isKeyDown(leftKey);
        pressingRight = Greenfoot.isKeyDown(rightKey);
        pressingUp = Greenfoot.isKeyDown(upKey);
    }
    private void applyInputs() {
        applyHorizontalInput();
        applyJumpInput();
        applyGravity();

    }
    private void applyHorizontalInput() {
        if(pressingLeft == pressingRight) {
            applyFriction(1);
            return;
        }
        if((pressingLeft && xVel > 0) || (pressingRight && xVel < 0)){
            applyFriction(counterStrafingFrictionMultiplier);
            return;
        }
        
        if(Math.abs(xVel) >= maxXSpeed) {
            xVel = maxXSpeed * (xVel/Math.abs(xVel));
            return;
        }
        
        
        if(grounded) {
            if(pressingLeft) xVel -= groundAcc;
            if(pressingRight) xVel += groundAcc;
            return;
        }
        if(pressingLeft) xVel -= airAcc;
        if(pressingRight) xVel += airAcc;
    }
    private void applyJumpInput() {
        if(grounded) {
            remainingJumps = jumps;
            yVel = 0;
        }
        if(pressingUp && remainingJumps > 0 && ticksSinceLastJump >= jumpCooldownTicks) {
            yVel -= jumpSpeed;
            remainingJumps--;
            grounded = false;
            ticksSinceLastJump = 0;
        }
        else ticksSinceLastJump++;
    }
    private void applyMovement() {
        setLocation(getX() + xVel, getY() + yVel);
        checkCollision();
    }
    private void checkCollision() {
        if(grounded) setLocation(getX(), new MyWorld().floorHeight);
    }
    private void applyFriction(double multiplier) {
        if(grounded) xVel *= 1-(groundFriction*multiplier);
        else xVel *= 1-(airFriction*multiplier);
    }
    private boolean isGrounded() {
        if(ticksSinceLastJump < 5) return false;
        return getY() >= new MyWorld().floorHeight;
    }
    private void applyGravity() {
        if(!grounded) yVel += new MyWorld().gravityAcc;
    }
}