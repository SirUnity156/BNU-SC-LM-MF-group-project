// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;


public class DevStatDisplay extends Actor
{
    private String output;
    
    public DevStatDisplay(String output) {
        this.output = output;
    }
    
    public void act()
    {
        Player player = getWorld().getObjects(Player.class).get(0);
        setImage(new GreenfootImage(Integer.toString((int)player.getHealth()), 50, Color.WHITE, Color.BLACK));
    }
}
