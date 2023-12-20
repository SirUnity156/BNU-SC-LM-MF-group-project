// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;


public class DevStatDisplay extends Actor
{
    
    public void act()
    {
        setImage(new GreenfootImage(getText(), 50, Color.WHITE, Color.BLACK));
    }

    public String getText() {
        return "";
    }
}
