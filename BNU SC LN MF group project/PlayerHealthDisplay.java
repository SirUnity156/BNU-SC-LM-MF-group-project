import greenfoot.*;

public class PlayerHealthDisplay extends DevStatDisplay {

    protected String getText() {
        return "health: " + Integer.toString(getWorld().getObjects(Player.class).get(0).getHealth());
    }
}
