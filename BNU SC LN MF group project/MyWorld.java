import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
        addObject(player, this.getWidth()/3, this.getHeight() * 1/2);
        addObject(new Platform(this.getWidth(),30), 0, this.getHeight() * 2/3);
    }
}
