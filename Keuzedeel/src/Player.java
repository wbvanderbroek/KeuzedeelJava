public class Player
{
    public int health = 100;
    public int maxHealth = 100;
    public int posX;
    public int posY;
    public int playerSpeed = 5;
    boolean playerFinished = false;
    public Player(int tileSize)
    {
        posX = tileSize;
        posY = tileSize;
    }
}