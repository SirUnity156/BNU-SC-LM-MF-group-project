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
        addObject(new DevStatDisplay(), 20, 20);
        addObject(new Player(gravityAcc, floorHeight), getWidth()/2, floorHeight);
    }
}
