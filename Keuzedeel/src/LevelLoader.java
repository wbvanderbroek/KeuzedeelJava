import java.awt.*;
import java.util.ArrayList;

public class LevelLoader
{
    GamePanel gp;
    int tileSize;
    int screenWidth;
    int screenHeight;
    int maxScreenCol;
    int maxScreenRow;

    int currentLevel = 1;
    public LevelLoader(GamePanel gp)
    {
        this.gp = gp;
        tileSize = gp.tileSize;
        screenHeight = gp.screenHeight;
        screenWidth = gp.screenWidth;
        maxScreenCol = gp.maxScreenCol;
        maxScreenRow = gp.maxScreenRow;
    }
    public void LoadLevel()
    {
        gp.player.health = gp.player.maxHealth;
        gp.player.posY = tileSize;
        gp.player.posX = tileSize;
        gp.player.playerFinished = false;
        gp.obstacles = new ArrayList<>();
        gp.finish = new ArrayList<>();
        gp.healthPickups = new ArrayList<>();
        gp.allObstacles.clear();
        gp.path = new ArrayList<>();
        gp.pathFinder = new PathFinder(gp);
        if (gp.obstacles != null)
        {
            gp.obstacles.clear();
        }
        if (gp.finish != null)
        {
            gp.finish.clear();
        }
        if (gp.path != null)
        {
            gp.path.clear();
        }
        //left and right bar
        gp.obstacles.add(new Rectangle(0, 0, tileSize, tileSize * maxScreenRow));
        gp.obstacles.add(new Rectangle(screenWidth - tileSize, 0, tileSize, tileSize * maxScreenRow));
        //top and bottom bar
        gp.obstacles.add(new Rectangle(0, 0, tileSize * screenWidth, tileSize));
        gp.obstacles.add(new Rectangle(0, screenHeight - tileSize, tileSize * screenWidth, tileSize));

        if (currentLevel == 1)
        {
            //actual obstacles
            gp.obstacles.add(new Rectangle(tileSize * 3, tileSize, tileSize, tileSize * 8));
            gp.obstacles.add(new Rectangle(tileSize * 6, tileSize * 3, tileSize, tileSize * 8));
            gp.obstacles.add(new Rectangle(tileSize * 9, tileSize, tileSize, tileSize * 8));
            gp.obstacles.add(new Rectangle(tileSize * 12, tileSize * 3, tileSize, tileSize * 8));

            //finish
            gp.finish.add(new Rectangle(screenWidth - tileSize * 3, screenHeight - tileSize * 2, tileSize * 2, tileSize * 2));

            //health pickups
            gp.healthPickups.add(new Rectangle(tileSize, tileSize * 4, tileSize, tileSize));

        }
        else if (currentLevel == 2)
        {
            //finish
            gp.finish.add(new Rectangle(screenWidth - tileSize * 3, screenHeight - tileSize * 2, tileSize * 2, tileSize * 2));
        }
        for (int i = 0; i < maxScreenCol; i++)
        {
            for (int j = 0; j < maxScreenRow; j++)
            {
                Rectangle tempRect = new Rectangle(tileSize * i, tileSize * j, tileSize, tileSize);
                boolean intersects = false;
                for (Rectangle object : gp.obstacles)
                {
                    if (tempRect.intersects(object))
                    {
                        intersects = true;
                        break;
                    }
                }
                if (!intersects)
                {
                    gp.path.add(tempRect);
                }
            }
        }
        for (int i = 0; i < maxScreenCol; i++)
        {
            for (int j = 0; j < maxScreenRow; j++)
            {
                Rectangle tempRect = new Rectangle(tileSize * i, tileSize * j, tileSize, tileSize);
                boolean intersects = false;
                for (Rectangle object : gp.path)
                {
                    if (tempRect.intersects(object))
                    {
                        intersects = true;
                        break;
                    }
                }
                if (!intersects)
                {
                    gp.allObstacles.add(tempRect);
                }
            }
        }

    }
}
