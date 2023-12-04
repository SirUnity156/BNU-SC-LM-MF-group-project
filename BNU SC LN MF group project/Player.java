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
    private boolean isOverLappingPlatform(Platform platform) {
        boolean xOverlap = ( getX() + this.xSize >= platform.getX() ) && ( getX() <= platform.getX() + platform.getXSize() );
        boolean yOverlap = ( getY() + this.ySize >= platform.getY() ) && ( getY() <= platform.getY() + platform.getYSize() );
        return xOverlap && yOverlap;

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
        int collisionBufferSize = 10;
        int offset;

        for(Platform platform : intersects) {
            platform = intersects.get(0);

            if(isOverLappingPlatform(platform)) {
                while(true);
            }

            xDiff = this.getX() - platform.getX();
            yDiff = this.getY() - platform.getY();

            if(yDiff < xDiff) {
                offset = yDiff;
                offset += (yDiff>0) ? (ySize/2 + collisionBufferSize) : (-ySize/2 - collisionBufferSize);
                yVel = 0;
                setLocation(getX(), getY() + offset);
                grounded = yDiff < 0;
            }

        }
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