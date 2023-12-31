import java.util.ArrayList;
import java.util.List;

// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/*TODO change remainingJump decrement
 * TODO fix bug where you can't counter strafe in air
*/

public class Player extends Actor {
    private int xSize, ySize; // Size of player

    // Buttons to control player
    private String leftKey = "a";
    private String rightKey = "d";
    private String upKey = "w";
    private String interactKey = "e";

    private boolean pressingUp = false, pressingLeft = false, pressingRight = false, pressingInteract = false; // Tracks the buttons being pressed

    private boolean grounded = false; // Tracks if player is touching the floor
    private boolean isTouchingLeftWall = false, isTouchingRightWall = isTouchingLeftWall;

    private boolean pressUpLastTick = false; // Tracks if up was pressed on the previous tick

    private boolean imageFlipped = false;

    private double groundFriction = 0.6, airFriction = 0.5; // Values for friction on ground and in air
    private double counterStrafingFrictionMultiplier = 5; // Friction is multiplied by this value when pressing direction opposite to current movement direction

    private double xVel = 0, yVel = 0; // Velocities
    private double maxXSpeed = 4; // Maximum allowed x velocity

    private double groundAcc = 0.6; // Acceleration on ground
    private double airAcc = 0.5; // Acceleration in air

    private int jumpSpeed = 15; // Speed increase when jumping
    private int jumps = 2, remainingJumps = jumps; // Sets number of jumps allowed between floor touches
    private int jumpCooldownTicks = 5; // Length of time after jumping that the player may not jump during
    private int ticksSinceLastJump = 0; // Counts ticks since the last jump
    private boolean hasJumpedOnThisUpInput = false; // Tracks if the player has jumped on the current jump input, for preventing multiple jumps on the same input

    private boolean jumpBufferActive = false; // If this is true, the player will automatically jump at the soonest opportunity
    private int jumpBufferDuration = 7; // How many ticks the jump buffer will remain active
    private int ticksSinceJumpBufferActive = 0; // Tracks how long the jump buffer has been active for

    private double worldGravity, effectiveGravity; // Stores the world gravity acceleration
    private double jumpApexGravityMultiplier = 0.3; // Gravity will be multiplied by this valuewhile at the jump apex
    private double jumpApexSpeedThreshold = 1.25; // A player that moves below this vertical speed will be considered to be at apex

    public int invincibilityDuration = 10, remainingInvincibilityFrames = invincibilityDuration; // Ticks of invincibility after being hit

    private int health = 10; // Amount of health points

    private String imagePath = "00_idle\\skeleton-00_idle_00.png"; //Image to represent player

    /** Players constructor applying size and world values */
    public Player(int xSize, int ySize, double worldGravity) {
        // Sets field values
        this.xSize = xSize;
        this.ySize = ySize;
        this.worldGravity = this.effectiveGravity = worldGravity;

        setImage(formatImage(imagePath)); // Sets the image to the correct size
    }

    /** Called every tick */
    public void act() {
        checkCollision(); // Ensures player is not intersecting any objects
        getInputs(); // Stores the current inputs
        applyInputs(); // Takes the user input and updates values accordingly
        applyMovement(); // Updates player position
        updateTimers(); // Updates timers by a tick
        checkGameOver(); // Checks if the game should end
    }

    /** Checks if the game should end and sets the world to the game over world */
    private void checkGameOver() {
        if (this.health <= 0)
            Greenfoot.setWorld(new GameOverWorld());
    }

    private void setSpriteDirection() {
        if(this.pressingLeft && !this.imageFlipped) {
            getImage().mirrorHorizontally();
            imageFlipped = true;
        }
        else if(this.pressingRight && this.imageFlipped) {
            getImage().mirrorHorizontally();
            imageFlipped = false;
        }
    }

    /** Updates the timers by a tick */
    private void updateTimers() {
        this.remainingInvincibilityFrames--; // Decrements remaining invincibilty ticks
    }

    public void setInvincibilityFrames(int duration) {
        this.remainingInvincibilityFrames = duration;
    }

    /**
     * Detects if the user is overlapping another platform
     * Used instead of isIntersecting due to minor changes in where values are
     * calculated from
     */
    private boolean isOverlappingPlatform(Platform platform, int buffer) {
        return isXOverlappingPlatform(platform, buffer) && isYOverlappingPlatform(platform, buffer);
    }

    /**
     * Detects if the x coords of the user overlaps withe the x coords of the
     * platform
     */
    private boolean isXOverlappingPlatform(Platform platform, int buffer) {
        // return getX() + this.xSize/2 >= platform.getX() - platform.getXSize()/2 &&
        // getX() - this.xSize/2 <= platform.getX() + platform.getXSize()/2;
        boolean rightOfLeftSide = this.getX() + this.xSize / 2 + buffer > platform.getX() - platform.getXSize() / 2; // Checks if the player is right of the left side of the platform
        boolean leftOfRightSide = this.getX() - this.xSize / 2 - buffer < platform.getX() + platform.getXSize() / 2; // Checks if the player is left of the right side of the platform
        return rightOfLeftSide && leftOfRightSide; // If both are true, the function returns true
    }

    /**
     * Detects if the y coords of the user overlaps withe the y coords of the
     * platform
     */
    private boolean isYOverlappingPlatform(Platform platform, int buffer) {
        boolean belowTop = this.getY() + this.ySize / 2 + buffer > platform.getY() - platform.getYSize() / 2; // Checks if the bottom of the player is below the top of the platform
        boolean aboveBottom = this.getY() - this.ySize / 2 - buffer < platform.getY() + platform.getYSize() / 2; // Checks if the top of the player is above the bottom of the platform
        return belowTop && aboveBottom; // If both are true, the function returns true
    }

    private List<Platform> getOverlappingPlatforms(int buffer) {
        List<Platform> platforms = getWorld().getObjects(Platform.class);
        List<Platform> overlaps = new ArrayList<>(0);
        for (Platform platform : platforms) {
            if (isOverlappingPlatform(platform, buffer))
                overlaps.add(platform);
        }
        return overlaps;
    }

    /** Sets image size to player size */
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path); // Gets image at specified path
        img.scale(xSize, ySize); // Set the size
        return img;
    }

    /** Takes user's inputs and saves them */
    private void getInputs() {
        pressUpLastTick = pressingUp; // Saves the state of up input last frame

        // Saves inputs
        pressingLeft = Greenfoot.isKeyDown(leftKey);
        pressingRight = Greenfoot.isKeyDown(rightKey);
        pressingUp = Greenfoot.isKeyDown(upKey);
        pressingInteract = Greenfoot.isKeyDown(interactKey);
    }

    /** Performs actions based on user inputs */
    private void applyInputs() {
        applyHorizontalInput(); // Takes value of pressing left/right and updates values accordingly
        applyJumpInput(); // Jumps if conditions are met
        applyGravity(); // Adds gravity to vertical velocity
        if (pressingInteract)
            interact();
    }

    /** Removes platforms is the player has collected enough coins */
    private void interact() {
        List<RemoveablePlatform> lockedDoors = getWorld().getObjects(RemoveablePlatform.class); // Gets all removeable platforms

        // Loops through all the doors and checks if the player has enough coins to
        // remove them
        for (RemoveablePlatform removeablePlatform : lockedDoors) {
            if (((MyWorld) getWorld()).getCoinCount() >= removeablePlatform.getCoinCost()) getWorld().removeObject(removeablePlatform);
        }
    }

    /**
     * Takes values of pressing left/right and updates values accordingly
     * If left/right are not pressed or both are pressed, friction is applied
     * If the opposite direction is pressed to the current movement, friction is
     * increased
     */
    private void applyHorizontalInput() {
        // checkIfTouchingLeftWall();
        // checkIfTouchingRightWall();

        // Checks if neither/both are pressed
        if (pressingLeft == pressingRight) {
            if(Math.abs(xVel) <= 2) {
                xVel = 0;
                return;
            }
            applyFriction(1);
            return;
        }

        // FIXME this prevents player moving in the air
        // Checks if pressing direction opposite to current movement
        
        if((pressingLeft && xVel > 0) || (pressingRight && xVel < 0)){
            applyFriction(counterStrafingFrictionMultiplier);
            return;
        }


        // Checks if speed exceeds maximum speed
        if (Math.abs(xVel) >= maxXSpeed) {
            xVel = maxXSpeed * (xVel / Math.abs(xVel));
            return;
        }

        // Applies acceleration
        // FIXME when the wall touching tests are used, player cannot strafe in the air
        if (grounded) {
            if (pressingLeft)
                xVel = (!this.isTouchingLeftWall) ? (xVel - groundAcc) : (0);
            if (pressingRight)
                xVel = (!this.isTouchingRightWall) ? (xVel + groundAcc) : (0);
            return;
        }

        // Applies acceleration unless player is colliding with a wall
        if (pressingLeft) {
            xVel = (!this.isTouchingLeftWall) ? (xVel - airAcc) : (0);
            return;
        }
        if (pressingRight) {
            xVel = (!this.isTouchingRightWall) ? (xVel + airAcc) : (0);
            return;
        }


        applyFriction(1);
    }

    /** Applies jump velocity if certain conditions are met */
    private void applyJumpInput() {
        // If the user has let go of the jump button, the yVel is reduced
        if (pressUpLastTick && !pressingUp && yVel < 0)
            yVel *= 0.3;
        ;

        // Updates jump buffer and checks if it has expired
        if (jumpBufferActive) {
            ticksSinceJumpBufferActive++;
            if (ticksSinceJumpBufferActive > jumpBufferDuration)
                jumpBufferActive = false; // Checks if jump buffer has expired
        }

        // Checks to activate the jump buffer
        if (pressingUp && ticksSinceLastJump > jumpCooldownTicks && !hasJumpedOnThisUpInput && !jumpBufferActive) {
            jumpBufferActive = true;
            ticksSinceJumpBufferActive = 0;
        }

        // Player jumps
        if ((pressingUp || jumpBufferActive) && remainingJumps > 0 && ticksSinceLastJump > jumpCooldownTicks
                && !hasJumpedOnThisUpInput) {
            // Jump from jump buffer
            if (jumpBufferActive && !pressingUp) {
                yVel = -(3 * jumpSpeed / 4);
            }

            // Normal jump
            else {
                yVel -= jumpSpeed;
                remainingJumps--;
                grounded = false;
                ticksSinceLastJump = 0;
                hasJumpedOnThisUpInput = true;
                jumpBufferActive = false;
            }
        }

        // No jump
        else {
            ticksSinceLastJump++;
            if (!pressingUp)
                hasJumpedOnThisUpInput = false;
        }
    }

    /** Updates player position */
    private void applyMovement() {
        setLocation(getX() + (int) Math.round(xVel), getY() + (int) Math.round(yVel));
        setSpriteDirection();
    }

    /** Checks for everything that the player could collide with */
    private void checkCollision() {
        checkPlatformCollision(); // Checks if player is colliding with any platforms
        checkHazardCollision(); // Checks if the player is touching any environmental hazards
        checkEnemyCollision(); // Checks if the player is colliding with any enemies
    }

    /**
     * Checks if the player is colliding with any enemies
     * If the player is touching any enemies, the damage of the most damaging enemy
     * is applied to the player
     */
    private void checkEnemyCollision() {
        // Checks if player is invincible
        if (this.remainingInvincibilityFrames > 0) return;

        // Gets all intersecting enemies
        List<Enemy> touchedEnemies = getIntersectingObjects(Enemy.class);

        // Returns if no enemies touched
        if (touchedEnemies.size() == 0) return;

        int highestDamage = 0; // Stores highest damage found

        // Loops through all enemies
        for (Enemy enemy : touchedEnemies) {
            // Checks if damage is higher and saves damage if higher
            if (enemy.getDamage() > highestDamage)
                highestDamage = enemy.getDamage();
        }
        this.setHealth(this.getHealth() - highestDamage); // Applies damage
        setInvincibilityFrames(this.invincibilityDuration); // Sets invincibility frames to I-frame duration
    }

    /**
     * Checks if the player intersects with a platform and moves them outside the
     * platform
     */
    private void checkPlatformCollision() {
        // Makes a new player and sets its location to the player's location next frame
        Player nextFramePlayer = this;
        nextFramePlayer.setLocation(getX() + (int) Math.round(xVel), getY() + (int) Math.round(yVel));

        List<Platform> intersects = getIntersectingObjects(Platform.class); // List of currently intersecting platforms
        int xDiff, yDiff; // To store differences in player-platform locations

        // Buffer between platform and the player
        int collisionBufferXSize = 1;
        int collisionBufferYSize = 1;

        // Distance for the player to be offset from the platform
        int offset;

        Platform platform;

        // Loops through intersecting platforms and moves the player out of them
        while (intersects.size() > 0) {
            platform = intersects.get(0);
            int distanceToTop = (nextFramePlayer.getY() + nextFramePlayer.ySize / 2)
                    - (platform.getY() - platform.getYSize() / 2); // Distance from bottom of player to top of platform
            int distanceToBottom = (nextFramePlayer.getY() - nextFramePlayer.ySize / 2)
                    - (platform.getY() + platform.getYSize() / 2); // Distance from top of player to bottom of platform
            boolean isAbove = Math.abs(distanceToTop) < Math.abs(distanceToBottom); // Flag to store if player is above platform

            yDiff = (isAbove) ? (distanceToTop) : (distanceToBottom); // If player is above platform, yDiff is distanceToTop, else is distanceToBottom

            int distanceToLeft = (nextFramePlayer.getX() + nextFramePlayer.xSize / 2)
                    - (platform.getX() - platform.getXSize() / 2); // Distance from right of player to left of platform
            int distanceToRight = (nextFramePlayer.getX() - nextFramePlayer.xSize / 2)
                    - (platform.getX() + platform.getXSize() / 2); // Distance from left of player to right of platform
            boolean isLeft = Math.abs(distanceToLeft) < Math.abs(distanceToRight);

            xDiff = (isLeft) ? (distanceToLeft) : (distanceToRight);

            /*
             * //Calculates differences in player-platform locations
             * xDiff = (nextFramePlayer.getX() + nextFramePlayer.xSize/2) - (platform.getX()
             * + platform.getXSize()/2);
             * yDiff = (nextFramePlayer.getY() + nextFramePlayer.ySize/2) - (platform.getY()
             * + platform.getYSize()/2);
             */

            // Checks to see if a vertical side is closer
            if (Math.abs(yDiff) <= Math.abs(xDiff)) {
                yVel = 0; // Player's y velocity is halted
                offset = (yDiff >= 0) ? (-platform.getYSize() / 2 - nextFramePlayer.ySize / 2 - collisionBufferYSize) : (platform.getYSize() / 2 + nextFramePlayer.ySize / 2 + collisionBufferYSize); // Offsets the player either above or below the platform depending on which is closer
                setLocation(this.getX(), platform.getY() + offset); // Update location
            }

            // Horizontal side is closer
            else {
                xVel = 0; // Player's x velocity is halted
                offset = (xDiff > 0) ? (-platform.getXSize() / 2 - nextFramePlayer.xSize / 2 - collisionBufferXSize) : (platform.getXSize() / 2 + nextFramePlayer.xSize / 2 + collisionBufferXSize); // Offsets the player either left or right the platform depending on which is closer
                setLocation(platform.getX() + offset, this.getY()); // Update location
            }

            intersects = getIntersectingObjects(Platform.class); // Gets all intersecting platforms
        }
        checkIfGrounded(); // Checks if the player is standing on a platform
        if (grounded)
            remainingJumps = jumps; // Reset number of jumps remaining
    }

    /** Checks if any platforms exist directly below the player */
    private void checkIfGrounded() {
        int groundedDistance = 5; // Distance that platforms must be within to ground the player
        List<Platform> platforms = getWorld().getObjects(Platform.class); // Gets all platforms

        // Loops through platforms and checks if below player
        for (Platform platform : platforms) {
            if (isXOverlappingPlatform(platform, 0) && (platform.getY() > this.getY()) && (this.getY() + this.ySize / 2 + groundedDistance > platform.getY() - platform.getYSize() / 2)) {
                this.grounded = true;
                return;
            }
        }
        this.grounded = false;
    }

    /**
     * Checks if player is touching any environmental hazards
     * If the player is intersecting with a hazard, the damage of the most damaging
     * hazard is applied
     */

    private void checkHazardCollision() {
        if(this.remainingInvincibilityFrames > 0) return;
        List <EnvironmentalHazard> hazards =  (List<EnvironmentalHazard>) getIntersectingObjects(EnvironmentalHazard.class);
        if(hazards.size() == 0) return;

        int highestDamage = 0;
        
        for (int i = 0; i < hazards.size(); i++) {
            int damage = hazards.get(i).getDamage();
            if (damage > highestDamage) {
                highestDamage = damage;

            }
        }
        this.setHealth(this.getHealth() - highestDamage);
        setInvincibilityFrames(this.invincibilityDuration);
    }



    /**Decreases player velocity*/
    private void applyFriction(double multiplier) {
        if (grounded) xVel *= (1 - (groundFriction * multiplier)); // Applies ground friction
        else xVel *= (1 - (airFriction * multiplier)); // Applies air friction
    }

    /** Applies gravity acceleration if not grounded */
    private void applyGravity() {
        if (!grounded) {
            if (-yVel < jumpApexSpeedThreshold) effectiveGravity = worldGravity * jumpApexGravityMultiplier; // Lowers gravity if player is at jump apex
            else effectiveGravity = worldGravity; // Gravity remains normal when not at apex

            yVel += effectiveGravity; // Applies gravity
        }
    }

    /** Returns xVel */
    public double getXVel() {
        return xVel;
    }

    /** Returns yVel */
    public double getYVel() {
        return yVel;
    }

    /** Returns health */
    public int getHealth() {
        return this.health;
    }

    /** Sets health to specified value */
    public void setHealth(int newHealth) {
        this.health = newHealth;
    }
}
