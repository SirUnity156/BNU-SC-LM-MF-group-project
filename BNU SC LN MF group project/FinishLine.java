import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class FinishLine extends Actor {
    private int cost;
    private String imgString = "FinishLineFlag.png";
    private int xSize;
    private int ySize;

    public FinishLine(int xSize, int ySize, int cost) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.cost = cost;

        setImage(formatImage(imgString));
    }

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

    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path); // Gets image at specified path
        img.scale(xSize, ySize); // Set the size
        return img;
    }
}