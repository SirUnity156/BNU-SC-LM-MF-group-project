// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class MyWorld extends World
{
    public double gravityAcc = 1.5;
    public int floorHeight = getHeight() * 2/3;
    private int tickRate = 50;
    private int xSize = getWidth();
    private int ySize = getHeight();
    
    public MyWorld()
    {
        super(1300, 700, 1);
        setBackground(formatImage("game_background\\background_idea_2.png"));
        Greenfoot.setSpeed(tickRate);
        Player player = new Player(20, 35, gravityAcc, floorHeight);
        
        addObject(player, this.getWidth()/2 - 200, 400);
        
        addObject(new Platform(this.getWidth()/2, 100), this.getWidth()/2, 500);
        addObject(new Platform(2*this.getWidth()/3, 100), this.getWidth()/2, 500);
        addObject(new Platform(100 , 200), 500, 300);
        addObject(new Platform(50 , 200), 1000, 400);
        
        addObject(new Platform(400, 50), 500, 300);
        
        addObject(new Enemy(20, 35, 100, 200, 400, 2), 400, 400);
        
        addObject(new LoseTimer(), this.getWidth()/2, 50);
        
        addCoins(10); // Adjust the number of coins as needed
    }
    
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); //Set the size
        return img;
    }

    // Coin Counter implementation (Store + Increment)
    private int coinCounter = 0;

    public void addCoins(int numCoins) {
        for (int i = 0; i < numCoins; i++) {
            addObject(new Coin(), Greenfoot.getRandomNumber(getWidth()), Greenfoot.getRandomNumber(getHeight()));
        }
    }

    public void incrementCoinCounter() {
        coinCounter++;
        updateCoinDisplay();
    }

    private void updateCoinDisplay() {
        showText("Coins: " + coinCounter, getWidth() - 100, 20);
    }
}
