import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Platform here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Platform extends Actor
{
    private int xSize;
    private int ySize;
    public Platform(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        GreenfootImage img = new GreenfootImage("images\\Wall.jpg");
        img.scale(xSize, ySize);
        setImage(img);
    }
}
