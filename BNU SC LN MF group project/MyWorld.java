import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public int gravityAcc = 2;
    public int floorHeight = getHeight() * 2/3;
    public int tickRate = 50;
    public MyWorld()
    {    
        super(1000, 400, 1);
        Greenfoot.setSpeed(tickRate);
        addObject(new Player(), getWidth()/2, floorHeight);
    }
}
