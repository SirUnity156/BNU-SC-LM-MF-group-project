    // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Greenfoot;
import greenfoot.World;

/**
 * Write a description of class MyWorld here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    public double gravityAcc = 2.5;
    public int floorHeight = getHeight() * 2/3;
    public int tickRate = 50;
    public MyWorld()
    {
        super(1600, 800, 1);
        Greenfoot.setSpeed(tickRate);
        Player player = new Player(20, 35, gravityAcc, floorHeight);
        
        addObject(new DevStatDisplay(), 20, 20);
        addObject(player, this.getWidth()/2 -200, 200);
        
        addObject(new Platform(this.getWidth()/2,50), this.getWidth()/2, 0);
        addObject(new Platform(this.getWidth()/2,50), this.getWidth()/2, 500);
        addObject(new Platform(50 ,this.getWidth()/2), 400, 100);
        addObject(new Platform(50 ,this.getWidth()/2), 1000, 0);
    }
}
