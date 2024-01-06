public class RemoveablePlatform extends Platform
{
    private int coinCost;
    private static String imgPath = "bricks3.jpg";
    public RemoveablePlatform(int xSize, int ySize, int coinCost) {
        super(xSize, ySize, imgPath);
        this.coinCost = coinCost;
    }
    
    public void act()
    {
        
    }

    public int getCoinCost() {return this.coinCost;}
}
