public class Spikes extends EnvironmentalHazard
{
    public Spikes(int xSize, int ySize) {
        super(xSize, ySize);
        this.damage = 1;
        this.imagePath = "player_damage_assets\\Spikes.png";
        setImage(formatImage(imagePath, xSize, ySize));
    }
    
    public void act()
    {
        
    }
}
