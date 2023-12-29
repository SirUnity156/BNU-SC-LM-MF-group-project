import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Platform extends Actor
{
    protected int xSize;
    protected int ySize;
    protected GreenfootImage img = new GreenfootImage("Wall.jpg");
    public Platform(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        img.scale(xSize, ySize);
        setImage(img);
    }

    public Platform(int xSize, int ySize, GreenfootImage img) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.img = img;
        img.scale(xSize, ySize);
        setImage(img);
    }

    public int getXSize() {return this.xSize;}

    public int getYSize() {return this.ySize;}
}
