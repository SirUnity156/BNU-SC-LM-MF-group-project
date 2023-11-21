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
        Player player = new Player(50, 100, gravityAcc, floorHeight);
        
        addObject(new DevStatDisplay(), 20, 20);
        addObject(player, getWidth()/2, floorHeight);
        addObject(new Platform(this.getWidth(),20), 0, floorHeight + 20);
    }
}
