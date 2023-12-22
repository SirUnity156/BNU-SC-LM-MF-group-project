import greenfoot.GreenfootImage;

public class RemoveablePlatform extends Platform
{
    private int coinCost;
    private static GreenfootImage img = new GreenfootImage("bricks3.jpg");
    public RemoveablePlatform(int xSize, int ySize, int coinCost) {
        super(xSize, ySize, img);
        this.coinCost = coinCost;
    }
    
    public void act()
    {
        
    }

    public int getCoinCost() {return this.coinCost;}
}
