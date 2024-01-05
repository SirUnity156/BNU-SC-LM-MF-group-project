import greenfoot.Actor;

public class Coin extends Actor {
    public void act() {
        checkCollision();
    }

    private void checkCollision() {
        Actor player = getOneIntersectingObject(Player.class);
        if (player != null) {
            // Player touched the coin
            updateCoinCounter();
            getWorld().removeObject(this);
            // Greenfoot.playSound("coin_sound.wav"); --> Add SFX?
        }
    }

    private void updateCoinCounter() {
        // Increment the coin counter in the world
        ((MyWorld) getWorld()).incrementCoinCounter();
        
    }
}
