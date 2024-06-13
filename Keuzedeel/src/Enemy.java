public class Enemy
{
    public int health = 100;
    public int maxHealth = 100;
    public int posX;
    public int posY;
    public int speed = 3;
    boolean onPath = false;
    GamePanel gp;
    public Enemy(GamePanel gp)
    {
        this.gp = gp;
        posX = gp.tileSize;
        posY = 420;
        onPath = true;
    }
    public void searchPath(int goalCol, int goalRow)
    {
        int startCol = posX / gp.tileSize;
        int startRow = posY / gp.tileSize;
        try
        {
            gp.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

            if (gp.pathFinder.search() == true)
            {
                int nextX = gp.pathFinder.pathList.get(0).col * gp.tileSize;
                int nextY = gp.pathFinder.pathList.get(0).row * gp.tileSize;

                if (posX < nextX)
                {
                    posX += speed;
                }
                else if (posX > nextX)
                {
                    posX -= speed;
                }

                if (posY < nextY)
                {
                    posY += speed;
                }
                else if (posY > nextY)
                {
                    posY -= speed;
                }
            }
            int nextCol = gp.pathFinder.pathList.get(0).col * gp.tileSize;
            int nextRow = gp.pathFinder.pathList.get(0).row * gp.tileSize;
            if (nextCol == goalCol && nextRow == goalRow)
            {
                onPath = false;
            }
        }
        catch (Exception e)
        {
        }
    }
    public void moveEnemy() {
        if (onPath == true)
        {
            int goalCol = gp.player.posX / gp.tileSize;
            int goalRow = gp.player.posY / gp.tileSize;
            searchPath(goalCol, goalRow);
        }
    }
}
