public class Enemy
{
    public int health = 100;
    public int maxHealth = 100;
    public int posX;
    public int posY;
    public int speed = 5;
    public Enemy(int tileSize)
    {
        posX = tileSize;
        posY = 420;
    }
}
