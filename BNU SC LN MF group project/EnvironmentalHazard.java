import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class EnvironmentalHazard extends Actor
{
    protected int xSize, ySize;
    protected int damage;
    protected String imagePath;
    public EnvironmentalHazard(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        
    }

    public void act()
    {
        
    }

    /**Sets image size to player size*/
    protected GreenfootImage formatImage(String path, int xSize, int ySize) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); //Set the size
        return img;
    }

    /**Returns damage*/
    public int getDamage() {return this.damage;}
}
