// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Enemy extends Actor
{
    private int xSize, ySize;

    private double movementSpeed = 5;

    private int health;

    private int leftBoundary, rightBoundary;

    private int damage;

    private boolean movingLeft = true;

    private String imagePath = "ppl3.png";

    public Enemy(int xSize, int ySize, int health, int leftBoundary, int rightBoundary, int damage) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.health = health;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.damage = damage;
        setImage(formatImage(imagePath));
    }
    
    public void act()
    {
        double xVel = (movingLeft) ? (-movementSpeed) : (movementSpeed);
        int newX = this.getX() + (int)xVel;
        if(newX < leftBoundary || newX > rightBoundary) {
            newX = (movingLeft) ? (leftBoundary) : (rightBoundary);
            movingLeft ^= true; //Bit flip
        }
        setLocation(newX, getY());
    }

    /**Sets image size to player size*/
    private GreenfootImage formatImage(String path) {
        GreenfootImage img = new GreenfootImage(path);
        img.scale(xSize, ySize); //Set the size
        return img;
    }

    public int getDamage() {
        return this.damage;
    }
}
