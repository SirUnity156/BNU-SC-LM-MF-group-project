import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DevStatDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DevStatDisplay extends Actor
{
    
    public void act()
    {
        Player player = getWorld().getObjects(Player.class).get(0);
        setImage(new GreenfootImage(Integer.toString(player.getYVel()), 50, Color.WHITE, Color.BLACK));
    }
}
