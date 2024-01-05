import greenfoot.Actor;
import greenfoot.Greenfoot;

public class FinishLine extends Actor {
    private int cost = 10;
    public void act() {
        Player player = getWorld().getObjects(Player.class).get(0);
        int coins = checkPlayerCoins();
        if(intersects(player) && coins >= this.cost) {
            Greenfoot.setWorld(new WinWorld());
        }
    }

    private int checkPlayerCoins() {
        MyWorld world = (MyWorld) getWorld();
        return world.getCoinCount();
    }

    public int getCost() {
        return this.cost;
    }
}