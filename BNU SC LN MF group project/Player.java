import java.util.ArrayList;
import java.util.List;

// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/*TODO change remainingJump decrement*/

public class Player extends Actor
{
    private int xSize, ySize; //Size of player

    //Buttons to control player
    private String leftKey = "left";
    private String rightKey = "right";
    private String upKey = "up";
    
    private boolean pressingUp = false, pressingLeft = false, pressingRight = false; //Tracks the buttons being pressed
    
    private boolean grounded = false; //Tracks if player is touching the floor
    private boolean isTouchingLeftWall = false, isTouchingRightWall = isTouchingLeftWall;

    private boolean pressUpLastTick = false; //Tracks if up was pressed on the previous tick
    
    private double groundFriction = 0.2, airFriction = 0.1; //Values for friction on ground and in air
    private double counterStrafingFrictionMultiplier = 5; //Friction is multiplied by this value when pressing direction opposite to current movement direction
    
    private double xVel = 0, yVel = 0; //Velocities
    private double maxXSpeed = 4; //Maximum allowed x velocity
    
    private double groundAcc = 0.6; //Acceleration on ground
    private double airAcc = 0.5; //Acceleration in air
    
    private int jumpSpeed = 15; //Speed increase when jumping
    private int jumps = 2, remainingJumps = jumps; //Sets number of jumps allowed between floor touches
    private int jumpCooldownTicks = 5; //Length of time after jumping that the player may not jump during
    private int ticksSinceLastJump = 0; //Counts ticks since the last jump
    private boolean hasJumpedOnThisUpInput = false; //Tracks if the player has jumped on the current jump input, for preventing multiple jumps on the same input
    
    private boolean jumpBufferActive = false; //If this is true, the player will automatically jump at the soonest opportunity
    private int jumpBufferDuration = 7; //How many ticks the jump buffer will remain active
    private int ticksSinceJumpBufferActive = 0; //Tracks how long the jump buffer has been active for
    
    private double worldGravity, effectiveGravity; //Stores the world gravity acceleration
    private double jumpApexGravityMultiplier = 0.5; //Gravity will be multiplied by this valuewhile at the jump apex
    private int jumpApexSpeedThreshold = 5; //A player that moves below this vertical speed will be considered to be at apex

    private int health = 1;

    public Player(int xSize, int ySize, double worldGravity, int worldFloorHeight) {
        //Sets field values
        this.xSize = xSize;
        this.ySize = ySize;
        this.worldGravity = this.effectiveGravity = worldGravity;

        setImage(formatImage("images\\man01.png")); //Sets the image to the correct size
    }
    
    /**Called every tick*/
    public void act()
    {
        checkCollision(); //Ensures playe ris not intersecting any platforms
        getInputs(); //Stores the current inputs
        applyInputs(); //Takes the user input and updates values accordingly
        applyMovement(); //Updates player position

    }
    
    /**Detects if the user is overlapping another platform
     * Used instead of isIntersecting due to minor changes in where values are calculated from
    */
    private boolean isOverlappingPlatform(Platform platform, int buffer) {
        return isXOverlappingPlatform(platform, buffer) && isYOverlappingPlatform(platform, buffer);
    }

    /**Detects if the x coords of the user overlaps withe the x coords of the platform*/
    private boolean isXOverlappingPlatform(Platform platform, int buffer) {
        //return getX() + this.xSize/2 >= platform.getX() - platform.getXSize()/2 && getX() - this.xSize/2 <= platform.getX() + platform.getXSize()/2;
        boolean leftOfLeftSide = this.getX() + this.xSize/2 + buffer > platform.getX() - platform.getXSize()/2;
        boolean rightOfRightSide = this.getX() - this.xSize/2 - buffer < platform.getX() + platform.getXSize()/2;
        return leftOfLeftSide && rightOfRightSide;
    }

    /**Detects if the y coords of the user overlaps withe the y coords of the platform*/
    private boolean isYOverlappingPlatform(Platform platform, int buffer) {
        boolean belowTop = this.getY() + this.ySize/2 + buffer > platform.getY() - platform.getYSize()/2;
        boolean aboveBottom = this.getY() - this.ySize/2 - buffer < platform.getY() + platform.getYSize()/2;
        return belowTop && aboveBottom;
    }

    private List<Platform> getOverlappingPlatforms(int buffer) {
        List<Platform> platforms = getWorld().getObjects(Platform.class);
        List<Platform> overlaps = new ArrayList<>(0);
        for(Platform platform : platforms) {
            if(isOverlappingPlatform(platform, buffer)) overlaps.add(platform);
        }
        return overlaps;
    }

    /**Sets image size to player size*/
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); //Set the size
        return img;
    }

    /**Takes user's inputs and saves them*/
    private void getInputs() {
        pressUpLastTick = pressingUp; //Saves the state of up input last frame
        
        //Saves inputs
        pressingLeft = Greenfoot.isKeyDown(leftKey);
        pressingRight = Greenfoot.isKeyDown(rightKey);
        pressingUp = Greenfoot.isKeyDown(upKey);
    }

    /**Performs actions based on user inputs*/
    private void applyInputs() {
        applyHorizontalInput(); //Takes value of pressing left/right and updates values accordingly
        applyJumpInput(); //Jumps if conditions are met
        applyGravity(); //Adds gravity to vertical velocity
    }

    /**Takes values of pressing left/right and updates values accordingly
     * If left/right are not pressed or both are pressed, friction is applied
     * If the opposite direction is pressed to the current movement, friction is increased
    */
    private void applyHorizontalInput() {
        //checkIfTouchingLeftWall();
        //checkIfTouchingRightWall();

        //Checks if neither/both are pressed
        if(pressingLeft == pressingRight) {
            applyFriction(1);
            return;
        }

        //FIXME this prevents player moving in the air
        //Checks if pressing direction opposite to current movement
        /*if((pressingLeft && xVel > 0) || (pressingRight && xVel < 0)){
            applyFriction(counterStrafingFrictionMultiplier);
            return;
        }*/
        
        //Checks if speed exceeds maximum speed
        if(Math.abs(xVel) >= maxXSpeed) {
            xVel = maxXSpeed * (xVel/Math.abs(xVel));
            return;
        }
        
        //Applies acceleration
        //FIXME when the wall touching tests are used, player cannot strafe in the air
        if(grounded) {
            if(pressingLeft) xVel = (!this.isTouchingLeftWall) ? (xVel - groundAcc) : (0);
            if(pressingRight) xVel = (!this.isTouchingRightWall) ? (xVel + groundAcc) : (0);
            return;
        }
        if(pressingLeft) xVel = (!this.isTouchingLeftWall) ? (xVel - airAcc) : (0);
        if(pressingRight) xVel = (!this.isTouchingRightWall) ? (xVel + airAcc) : (0);
    }

    /**Applies jump velocity if certain conditions are met*/
    private void applyJumpInput() {
        //If the user has let go of the jump button, the yVel is reduced
        if(pressUpLastTick && !pressingUp && yVel < 0) yVel *= 0.3;;

        //Updates jump buffer and checks if it has expired
        if(jumpBufferActive) {
            ticksSinceJumpBufferActive++;
            if(ticksSinceJumpBufferActive > jumpBufferDuration) jumpBufferActive = false;
        }

        //Checks to activate the jump buffer
        if(pressingUp && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput && !jumpBufferActive) {
            jumpBufferActive = true;
            ticksSinceJumpBufferActive = 0;
        }

        //Player jumps
        if((pressingUp || jumpBufferActive) && remainingJumps > 0 && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput) {
            //Jump from jump buffer
            if(jumpBufferActive && !pressingUp) {
                yVel = -(3*jumpSpeed/4);
            }

            //Normal jump
            else {
                yVel -= jumpSpeed;
                remainingJumps--;
                grounded = false;
                ticksSinceLastJump = 0;
                hasJumpedOnThisUpInput = true;
                jumpBufferActive = false;
            }
        }

        //No jump
        else {
            ticksSinceLastJump++;
            if(!pressingUp) hasJumpedOnThisUpInput = false;
        }
    }

    /**Updates player position*/
    private void applyMovement() {
        setLocation(getX() + (int)Math.round(xVel), getY() + (int)Math.round(yVel));
    }

    private void checkCollision() {
        checkPlatformCollision();
        checkHazardCollision();
        checkEnemyCollision();
    }

    private void checkEnemyCollision() {
        List<Enemy> touchedEnemies = getIntersectingObjects(Enemy.class);
        if(touchedEnemies.size() == 0) return;
        int highestDamage = 0;
        for (Enemy enemy : touchedEnemies) {
            if(enemy.getDamage() > highestDamage) highestDamage = enemy.getDamage();
        }
        this.setHealth(this.getHealth() - highestDamage);
    }

    /**Checks if the player intersects with a platform and moves them outside the platform*/
    private void checkPlatformCollision() {
        Player nextFramePlayer = this;
        nextFramePlayer.setLocation(getX() + (int)Math.round(xVel)  , getY() + (int)Math.round(yVel));

        List<Platform> intersects = getIntersectingObjects(Platform.class); //List of currently intersecting platforms
        int xDiff, yDiff; //To store differences in player-platform locations

        //Buffer between platform and the player
        int collisionBufferXSize = 1;
        int collisionBufferYSize = 1;

        //Distance for the player to be offset from the platform
        int offset;
        
        Platform platform;

        //Loops through intersecting platforms and moves the player out of them
        while(intersects.size() > 0) {
            platform = intersects.get(0);
            int distanceToTop = (nextFramePlayer.getY() + nextFramePlayer.ySize/2) - (platform.getY() - platform.getYSize()/2);
            int distanceToBottom = (nextFramePlayer.getY() - nextFramePlayer.ySize/2) - (platform.getY() + platform.getYSize()/2);
            boolean isAbove = Math.abs(distanceToTop) < Math.abs(distanceToBottom);

            yDiff = (isAbove) ? (distanceToTop) : (distanceToBottom);

            int distanceToLeft = (nextFramePlayer.getX() + nextFramePlayer.xSize/2) - (platform.getX() - platform.getXSize()/2);
            int distanceToRight = (nextFramePlayer.getX() - nextFramePlayer.xSize/2) - (platform.getX() + platform.getXSize()/2);
            boolean isLeft = Math.abs(distanceToLeft) < Math.abs(distanceToRight);
            
            xDiff = (isLeft) ? (distanceToLeft) : (distanceToRight);

            /*//Calculates differences in player-platform locations
            xDiff = (nextFramePlayer.getX() + nextFramePlayer.xSize/2) - (platform.getX() + platform.getXSize()/2);
            yDiff = (nextFramePlayer.getY() + nextFramePlayer.ySize/2) - (platform.getY() + platform.getYSize()/2);*/

            //Checks to see if a vertical side is closer
            if(Math.abs(yDiff) <= Math.abs(xDiff)) {
                yVel = 0; //Player's y velocity is halted
                offset = (yDiff >= 0) ? (-platform.getYSize()/2 - nextFramePlayer.ySize/2 - collisionBufferYSize) : (platform.getYSize()/2 + nextFramePlayer.ySize/2 + collisionBufferYSize); //Offsets the player either above or below the platform depending on which is closer
                setLocation(this.getX(), platform.getY() + offset);
            }
            
            //Horizontal side is closer
            else {
                xVel = 0; //Player's x velocity is halted
                offset = (xDiff > 0) ? (-platform.getXSize()/2 - nextFramePlayer.xSize/2 - collisionBufferXSize) : (platform.getXSize()/2 + nextFramePlayer.xSize/2 + collisionBufferXSize); //Offsets the player either left or right the platform depending on which is closer
                setLocation(platform.getX() + offset, this.getY());
            }

            intersects = getIntersectingObjects(Platform.class);
        }
        checkIfGrounded(); //Checks if the player is standing on a platform
        if(grounded) remainingJumps = jumps; //Reset number of jumps remaining
    }

    /**Checks if any platforms exist directly below the player */
    private void checkIfGrounded() {
        int groundedDistance = 5; //Distance that platforms must be within to ground the player
        List<Platform> platforms = getWorld().getObjects(Platform.class); //Gets all platforms
        
        //Loops through platforms and checks if below player
        for (Platform platform : platforms) {
            if(isXOverlappingPlatform(platform, 0) && (platform.getY() > this.getY()) && (this.getY() + this.ySize/2 + groundedDistance > platform.getY() - platform.getYSize()/2)) {
                this.grounded = true;
                return;
            }
        }
        this.grounded = false;
    }

    private void checkIfTouchingLeftWall() {
        int leftTouchDistance = 5; //Distance that platforms must be within to ground the player
        int verticalBuffer = 0;
        List<Platform> platforms = getWorld().getObjects(Platform.class); //Gets all platforms
        
        for (Platform platform : platforms) {
            boolean isLeftOfPlayer = platform.getX() < this.getX();
            boolean isCloseEnough = platform.getX() + platform.getXSize()/2 < this.getX() - leftTouchDistance - this.xSize/2;
            if(isYOverlappingPlatform(platform, verticalBuffer) && isLeftOfPlayer && isCloseEnough) {
                this.isTouchingLeftWall = true;
                return;
            }
        }
        this.isTouchingLeftWall = false;
    }

    private void checkIfTouchingRightWall() {
        int rightTouchDistance = 5; //Distance that platforms must be within to ground the player
        int verticalBuffer = 0;
        List<Platform> platforms = getWorld().getObjects(Platform.class); //Gets all platforms
        
        for (Platform platform : platforms) {
            boolean isRightOfPlayer = platform.getX() > this.getX();
            boolean isCloseEnough = platform.getX() - platform.getXSize()/2 < this.getX() + rightTouchDistance + this.xSize/2;
            if(isYOverlappingPlatform(platform, verticalBuffer) && isRightOfPlayer && isCloseEnough) {
                this.isTouchingRightWall = true;
                return;
            }
        }
        this.isTouchingRightWall = false;
    }


    private void checkHazardCollision() {

    }

    /**Decreases player velocity*/
    private void applyFriction(double multiplier) {
        if(grounded) xVel *= 1-(groundFriction*multiplier); //Applies ground friction
        else xVel *= 1-(airFriction*multiplier); //Applies air friction
    }

    /**Applies gravity acceleration if not grounded*/
    private void applyGravity() {
        if(!grounded) {
            if(-yVel < jumpApexSpeedThreshold) effectiveGravity = worldGravity * jumpApexGravityMultiplier;
            else effectiveGravity = worldGravity;
            yVel += effectiveGravity; //Applies gravity
        }
    }

    /**Returns xVel*/
    public double getXVel() {
        return xVel;
    }

    /**Returns yVel*/
    public double getYVel() {
        return yVel;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int newHealth) {
        this.health = newHealth;
    }
}