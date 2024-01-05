import greenfoot.*;

public class FinishLine extends Actor {
    public void act() {
        checkPlayerCoins();
    }

    private void checkPlayerCoins() {
        MyWorld world = (MyWorld) getWorld(); // 
        int collectedCoins = world.getCoinCount();

        if (collectedCoins >= 5) {
            // End game and display "Winner" message
            Greenfoot.stop();
            world.showText("Winner", world.getWidth() / 2, world.getHeight() / 2);
        } else {
            // Display message to collect at least five coins
            world.showText("Collect at least five coins!", world.getWidth() / 2, world.getHeight() / 2);
        }
    }
}
