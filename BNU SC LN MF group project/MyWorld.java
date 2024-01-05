
// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;


public class MyWorld extends World {
    public double gravityAcc = 1.5;
    public int floorHeight = getHeight() * 2 / 3;
    private int tickRate = 50;
    private int xSize = getWidth();
    private int ySize = getHeight();

    public MyWorld() {
        super(1300, 700, 1);
        setBackground(formatImage("game_background\\background_idea_2.png"));
        Greenfoot.setSpeed(tickRate);

        Player player = new Player(20, 35, gravityAcc);
        
        addObject(player, 100, 625);

        //Section 1
        //Platforms
        addObject(new Platform(300, 50), 150, 675);
        addObject(new Platform(200, 50), 200, 550);
        addObject(new Platform(200, 50), 100, 425);
        addObject(new Platform(250, 50), 225, 200);

        addObject(new Platform(50, 500), 325, 450);

        //Doors
        addObject(new RemoveablePlatform(50, 180, 3), 325, 85);

        //Enemies
        Enemy enemy11 = new Enemy(20, 40, 10, 100, 300, 2);
        addObject(enemy11, 300, 160);

        //Timer
        addObject(new LoseTimer(), this.getWidth() / 2, 50);

        //Displays
        addObject(new PlayerHealthDisplay(), 90, 20);
        addObject(new PlayerCoinDisplay(), 90, 80);

        //Coins
        addObject(new Coin(), 275, 610);
        addObject(new Coin(), 275, 50);
        addObject(new Coin(), 275, 250);

        //Section 2
        //Platforms
        addObject(new Platform(100, 50), 400, 200);
        addObject(new Platform(150, 50), 550, 100);
        addObject(new Platform(200, 50), 450, 400);
        addObject(new Platform(200, 50), 550, 500);
        addObject(new Platform(350, 50), 500, 675);

        addObject(new Platform(50, 550), 650, 275);

        addObject(new Platform(50, 50), 475, 300);
        addObject(new Platform(50, 50), 575, 300);
        addObject(new Platform(50, 50), 600, 200);

        //Doors
        addObject(new RemoveablePlatform(50, 200, 6), 650, 600);

        //Coins
        Enemy enemy21 = new Enemy(10, 20, 10, 475, 625, 2);
        addObject(enemy21, 550, 70);

        //Coins
        addObject(new Coin(), 600, 25);
        addObject(new Coin(), 600, 150);
        addObject(new Coin(), 375, 250);

        //Section 3
        //Platforms
        addObject(new Platform(635, 50), 990, this.getHeight()-25);
        addObject(new Platform(300, 50), this.getWidth()-150, 500);
        addObject(new Platform(300, 50), this.getWidth()-150, 300);
        addObject(new Platform(200, 50), 775, 150);

        addObject(new Platform(50, 50), 800, 550);
        addObject(new Platform(50, 50), 700, 450);
        addObject(new Platform(50, 50), 850, 350);
        addObject(new Platform(50, 50), 1200, 150);
        addObject(new Platform(50, 50), 1000, 150);
        

        //Enemies
        Enemy enemy31 = new Enemy(20, 40, 10, 690, 1300, 2);
        addObject(enemy31, 1200, this.getHeight()-65);
        Enemy enemy32 = new Enemy(20, 40, 10, 690, 1300, 2);
        addObject(enemy32, 690, this.getHeight()-65);

        //Coins
        addObject(new Coin(), 1275, this.getHeight()-100);
        addObject(new Coin(), 1275, 400);
        addObject(new Coin(), 700, 400);
    }

    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); // Set the size
        return img;
    }

    // Coin Counter implementation (Store + Increment)
    private int coinCounter = 0;

    public void addCoins(Point[] positions) {
        for (int i = 0; i < positions.length; i++) {
            addObject(new Coin(), (int)positions[i].getX(), (int)positions[i].getY());
        }
    }

    public void incrementCoinCounter() {
        coinCounter++;
        updateCoinDisplay();
    }

    private void updateCoinDisplay() {
        
    }

    public int getCoinCount() {
        return coinCounter;
    }
}
