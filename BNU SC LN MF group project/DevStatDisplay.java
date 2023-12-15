// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;


public class DevStatDisplay extends Actor
{
    
    public void act()
    {
        Player player = getWorld().getObjects(Player.class).get(0);
        setImage(new GreenfootImage(Integer.toString(player.getHealth()), 50, Color.WHITE, Color.BLACK));
    }
}
