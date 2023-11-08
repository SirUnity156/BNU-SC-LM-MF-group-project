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
    public int gravityAcc = 10;
    public int floorHeight = getHeight() * 2/3;
    public MyWorld()
    {    
        super(600, 400, 1);
        Greenfoot.setSpeed(50);
    }
}
