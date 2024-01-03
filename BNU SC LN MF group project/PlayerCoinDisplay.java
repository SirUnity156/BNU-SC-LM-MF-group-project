import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerCoinDisplay here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class PlayerCoinDisplay extends DevStatDisplay {
    protected String getText() {
        return "health: " + Integer.toString(((MyWorld) getWorld()).getCoinCount());
    }
}
