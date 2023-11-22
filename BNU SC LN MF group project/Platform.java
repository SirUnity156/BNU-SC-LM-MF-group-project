import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    public int getxYSize() {return this.xSize;}

    public int getYSize() {return this.ySize;}
}
