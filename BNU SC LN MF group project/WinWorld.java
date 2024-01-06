// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;
import greenfoot.World;

public class WinWorld extends World
{
    public WinWorld()
    {
        super(1300, 700, 1);
        setBackground(formatImage("win_screen.jpg"));
    }
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(1300, 700); // Set the size
        return img;
    }
}
