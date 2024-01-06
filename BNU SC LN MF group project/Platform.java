import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Platform extends Actor
{
    protected int xSize;
    protected int ySize;
    protected String imgPath = "Wall.jpg";
    public Platform(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        
        setImage(formatImage(imgPath));
    }

    public Platform(int xSize, int ySize, String imgPath) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.imgPath = imgPath;
        setImage(formatImage(imgPath));
    }

    protected GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path); // Gets image at specified path
        img.scale(xSize, ySize); // Set the size
        return img;
    }

    public int getXSize() {return this.xSize;}

    public int getYSize() {return this.ySize;}
}
