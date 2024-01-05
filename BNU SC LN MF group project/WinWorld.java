// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;
import greenfoot.World;

public class WinWorld extends World
{
    public WinWorld()
    {
        super(600, 400, 1);
        setBackground(formatImage("win_screen.jpg"));
    }
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(getWidth(), getHeight()); // Set the size
        return img;
    }
}
