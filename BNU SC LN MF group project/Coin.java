import greenfoot.*;

public class Coin extends Actor {
    public void act() {
        checkCollision();
    }

    private void checkCollision() {
        Actor player = getOneIntersectingObject(Player.class); 
        if (player != null) {
            // Player touched the coin
            getWorld().removeObject(this);
            // Greenfoot.playSound("coin_sound.wav"); --> Add SFX?
            updateCoinCounter();
        }
    }

    private void updateCoinCounter() {
        // Increment the coin counter in the world
        World world = getWorld();
        if (world instanceof MyWorld) { 
            ((MyWorld) world).incrementCoinCounter();
        }
    }
}
