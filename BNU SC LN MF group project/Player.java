import java.util.ArrayList;
import java.util.List;

// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**--Planned Development--
 * Coyote time
 * Slide
 */

public class Player extends Actor
{
    private int xSize, ySize;
    
    private String leftKey = "left";
    private String rightKey = "right";
    private String upKey = "up";
    
    private boolean pressingUp = false, pressingLeft = false, pressingRight = false;
    
    private boolean grounded = false;
    
    private boolean pressUpLastTick = false;
    
    private double groundFriction = 0.2, airFriction = 0.1;
    private double counterStrafingFrictionMultiplier = 5;
    
    private int xVel = 0, yVel = 0;
    private int maxXSpeed = 15;
    
    private double groundAcc = 2;
    private double airAcc = 1;
    
    private int jumpSpeed = 35;
    private int jumps = 1, remainingJumps = jumps;
    private int jumpCooldownTicks = 5;
    private int ticksSinceLastJump = 0;
    private boolean hasJumpedOnThisUpInput = false;
    
    private boolean jumpBufferActive = false;
    private int jumpBufferDuration = 7;
    private int ticksSinceJumpBufferActive = 0;
    
    private double worldGravity, effectiveGravity;
    private double jumpApexGravityMultiplier = 0.5;
    private int jumpApexSpeedThreshold = 10;

    public Player(int xSize, int ySize, double worldGravity, int worldFloorHeight) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.worldGravity = this.effectiveGravity = worldGravity;

        formatImage("images\\man01.png");
    }
    
    public void act()
    {
        checkCollision();
        getInputs();
        applyInputs();
        applyMovement();

    }
    private boolean isOverlappingPlatform(Platform platform) {
        return isXOverlappingPlatform(platform) && isYOverlappingPlatform(platform);
    }
    private boolean isXOverlappingPlatform(Platform platform) {
        return getX() + this.xSize/2 >= platform.getX() - platform.getXSize()/2 && getX() - this.xSize/2 <= platform.getX() + platform.getXSize()/2;
    }
    private boolean isYOverlappingPlatform(Platform platform) {
        return getY() + this.ySize/2 >= platform.getY() - platform.getYSize()/2 && getY() <= platform.getY() + platform.getYSize();
    }
    private void formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize);
        setImage(img);
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
        if(pressUpLastTick && !pressingUp && yVel < 0) yVel = (3*yVel)/10;
        if(jumpBufferActive) {
            ticksSinceJumpBufferActive++;
            if(ticksSinceJumpBufferActive > jumpBufferDuration) jumpBufferActive = false;
        }
        if(pressingUp && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput && !jumpBufferActive) {
            jumpBufferActive = true;
            ticksSinceJumpBufferActive = 0;
        }
        if((pressingUp || jumpBufferActive) && remainingJumps > 0 && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput) {
            
            if(jumpBufferActive && !pressingUp) {
                yVel = -(3*jumpSpeed/4);
            }
            else {
                yVel -= jumpSpeed;
                remainingJumps--;
                grounded = false;
                ticksSinceLastJump = 0;
                hasJumpedOnThisUpInput = true;
                jumpBufferActive = false;
            }
        }
        else {
            ticksSinceLastJump++;
            if(!pressingUp) hasJumpedOnThisUpInput = false;
        }
    }
    private void applyMovement() {
        setLocation(getX() + xVel, getY() + yVel);
    }
    private void checkCollision() {

        List<Platform> intersects = getIntersectingObjects(Platform.class);
        int xDiff, yDiff;
        int collisionBufferSize = 5;
        int offset;
        


        for(Platform platform : intersects) {
            xDiff = this.getX() + this.xSize/2 - platform.getX() - platform.getXSize()/2;
            yDiff = this.getY() + this.ySize - platform.getY() - platform.getYSize();

            if(yDiff >= xDiff) {
                yVel = 0;
                offset = (yDiff < 0) ? (-this.ySize - collisionBufferSize) : (platform.getYSize() + collisionBufferSize);
                setLocation(this.getX(), platform.getY() + offset);
            }
            /*else {
                xVel = 0;
                offset = (xDiff < 0) ? (-this.xSize - collisionBufferSize) : (platform.getXSize()/2 + collisionBufferSize);
                setLocation(platform.getX() + offset, this.getY());
            }*/
        }
        checkIfGrounded();
        if(grounded) remainingJumps = jumps;
    }
    private void checkIfGrounded() {
        int groundedDistance = 10;
        List<Platform> platforms = getWorld().getObjects(Platform.class);
        
        for (Platform platform : platforms) {
            if(isXOverlappingPlatform(platform) && (platform.getY() > this.getY()) && (platform.getY() - this.getY() <= groundedDistance + platform.getYSize())) {
                this.grounded = true;
                return;
            }
        }
        this.grounded = false;
    }
    private List<Platform> getOverlappingPlatforms() {
        List<Platform> platforms = getWorld().getObjects(Platform.class);
        List<Platform> overlaps = new ArrayList<>(0);
        for (Platform platform : platforms) {
            if(isOverlappingPlatform(platform)) overlaps.add(platform);
        }
        return overlaps;
    }
    private void applyFriction(double multiplier) {
        if(grounded) xVel *= 1-(groundFriction*multiplier);
        else xVel *= 1-(airFriction*multiplier);
    }
    private void applyGravity() {
        if(!grounded) {
            if(-yVel < jumpApexSpeedThreshold) effectiveGravity = worldGravity * jumpApexGravityMultiplier;
            else effectiveGravity = worldGravity;
            yVel += effectiveGravity;
        }
    }
    public int getXVel() {
        return xVel;
    }
    public int getYVel() {
        return yVel;
    }
}