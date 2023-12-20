// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Write a description of class GameOverWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameOverWorld extends World
{
    
    private int xSize = getWidth();
    private int ySize = getHeight();
    public GameOverWorld()
    {
        super(1300, 700, 1);
        setBackground(formatImage("game_over.png"));
    }
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); //Set the size
        return img;
    }
}
