// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.GreenfootImage;

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
    public int getXSize() {return this.xSize;}

    public int getYSize() {return this.ySize;}
}
