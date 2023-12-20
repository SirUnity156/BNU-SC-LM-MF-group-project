import java.util.Timer;
import java.util.TimerTask;

// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

public class LoseTimer extends Actor
{
    private static int interval;
    static Timer timer;
    public LoseTimer() {
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = 60;

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                setInterval();

            }
        }, delay, period);
    }

    private static final int setInterval() {
        if (interval == 1) timer.cancel();
        return --interval;
    }

    public void act()
    {

        setImage(new GreenfootImage(Integer.toString(interval), 50, Color.WHITE, Color.BLACK));
    }
    public static int getRemainingTime() {
        return interval;
    }
}
