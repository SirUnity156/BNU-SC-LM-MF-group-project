import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**--Planned Development--
 * Coyote time
 * Slide
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
    
    private boolean pressUpLastTick = false;
    
    private double groundFriction = 0.2;
    private double airFriction = 0.1;
    private double counterStrafingFrictionMultiplier = 5;
    
    private int xVel = 0;
    private int yVel = 0;
    private int maxXSpeed = 25;
    
    private double groundAcc = 2;
    private double airAcc = 1;
    
    private int jumpSpeed = 30;
    private int jumps = 1;
    private int remainingJumps = jumps;
    private int jumpCooldownTicks = 5;
    private int ticksSinceLastJump = 0;
    private boolean hasJumpedOnThisUpInput = false;
    
    private boolean jumpBufferActive = false;
    private int jumpBufferDuration = 7;
    private int ticksSinceJumpBufferActive = 0;
    
    private double worldGravity;
    private double effectiveGravity;
    private double jumpApexGravityMultiplier = 0.1;
    private int jumpApexSpeedThreshold = 10;
    private int worldFloorHeight;

    public Player(double worldGravity, int worldFloorHeight) {
        this.worldGravity = worldGravity;
        this.effectiveGravity = worldGravity;
        this.worldFloorHeight = worldFloorHeight;
    }
    
    public void act()
    {
        grounded = isGrounded();
        
        getInputs();
        applyInputs();
        applyMovement();
    }
    private void getInputs() {
        pressUpLastTick = pressingUp;
        
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
        if(pressUpLastTick && !pressingUp && yVel < 0) yVel /= 2;
        if(jumpBufferActive) {
            ticksSinceJumpBufferActive++;
            if(ticksSinceJumpBufferActive > jumpBufferDuration) jumpBufferActive = false;
        }
        if(grounded) {
            remainingJumps = jumps;
            yVel = 0;
        }
        else if(pressingUp && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput && !jumpBufferActive) {
            jumpBufferActive = true;
            ticksSinceJumpBufferActive = 0;
        }
        if((pressingUp || jumpBufferActive) && remainingJumps > 0 && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput) {
            
            if(jumpBufferActive && !pressingUp) {
                yVel = -(3*jumpSpeed/4);
            }
            else yVel -= jumpSpeed;
            remainingJumps--;
            grounded = false;
            ticksSinceLastJump = 0;
            hasJumpedOnThisUpInput = true;
            jumpBufferActive = false;
        }
        else {
            ticksSinceLastJump++;
            if(!pressingUp) hasJumpedOnThisUpInput = false;
        }
    }
    private void applyMovement() {
        setLocation(getX() + xVel, getY() + yVel);
        checkCollision();
    }
    private void checkCollision() {
        if(grounded) setLocation(getX(), worldFloorHeight);
    }
    private void applyFriction(double multiplier) {
        if(grounded) xVel *= 1-(groundFriction*multiplier);
        else xVel *= 1-(airFriction*multiplier);
    }
    private boolean isGrounded() {
        //if(ticksSinceLastJump < 5) return false;
        return getY() >= worldFloorHeight;
    }
    private void applyGravity() {
        if(!grounded) {
            if(-yVel < jumpApexSpeedThreshold) effectiveGravity = worldGravity * jumpApexGravityMultiplier;
            else effectiveGravity = worldGravity;
            yVel += worldGravity;
        }
    }
    public int getXVel() {
        return xVel;
    }
    public int getYVel() {
        return yVel;
    }
}