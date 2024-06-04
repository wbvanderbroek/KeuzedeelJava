import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable
{
    final int originalTileSize = 16; //16x16 pixel size
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    int fps = 60;
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    int playerX = tileSize;
    int playerY = tileSize;
    int playerSpeed = 5;
    ArrayList<Rectangle> obstacles;
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        obstacles = new ArrayList<>();
        //left and right bar
        obstacles.add(new Rectangle(0, 0, tileSize, tileSize * maxScreenRow));
        obstacles.add(new Rectangle(screenWidth - tileSize, 0, tileSize, tileSize * maxScreenRow));
        //top and bottom bar
        obstacles.add(new Rectangle(0, 0, tileSize * screenWidth, tileSize));
        obstacles.add(new Rectangle(0, screenHeight - tileSize, tileSize * screenWidth, tileSize));

        //actual obstacles
        obstacles.add(new Rectangle(tileSize * 3, tileSize, tileSize, tileSize * 8));
        obstacles.add(new Rectangle(tileSize * 6, tileSize * 3, tileSize, tileSize * 8));
        obstacles.add(new Rectangle(tileSize * 9, tileSize, tileSize, tileSize * 8));
        obstacles.add(new Rectangle(tileSize * 12, tileSize * 3, tileSize, tileSize * 8));

    }
    public void StartGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run()
    {
        double drawInterval  = 1000000000 /fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        int timer = 0;
        int drawCount = 0;

        while (gameThread != null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1)
            {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000)
            {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update()
    {
        int nextPlayerX = playerX;
        int nextPlayerY = playerY;

        if (keyHandler.upPressed)
        {
            nextPlayerY -= playerSpeed;
        }
        if (keyHandler.downPressed)
        {
            nextPlayerY += playerSpeed;
        }
        if (keyHandler.leftPressed)
        {
            nextPlayerX -= playerSpeed;
        }
        if (keyHandler.rightPressed)
        {
            nextPlayerX += playerSpeed;
        }


        boolean collisionX = false;
        if (playerX != nextPlayerX)
        {
            for (int i = 1; i <= playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(playerX + (nextPlayerX - playerX > 0 ? i : -i), playerY, tileSize, tileSize);
                collisionX = false;
                for (Rectangle obstacle : obstacles)
                {
                    if (nextPlayerRectX.intersects(obstacle))
                    {
                        collisionX = true;
                        break;
                    }
                }
                if (!collisionX)
                {
                    playerX = playerX + (nextPlayerX - playerX > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }

        boolean collisionY = false;
        if (playerY != nextPlayerY)
        {
            for (int i = 1; i <= playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(playerX, playerY + (nextPlayerY - playerY > 0 ? i : -i), tileSize, tileSize);
                collisionY = false;
                for (Rectangle obstacle : obstacles)
                {
                    if (nextPlayerRectX.intersects(obstacle))
                    {
                        collisionY = true;
                        break;
                    }
                }
                if (!collisionY)
                {
                    playerY = playerY + (nextPlayerY - playerY > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }
    }
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        //Display player
        Graphics2D player  = (Graphics2D)graphics;
        player.setColor(Color.white);
        player.fillRect(playerX,playerY,tileSize,tileSize);
        //player.dispose();

        //Display all the obstacles
        for (Rectangle obstacle : obstacles)
        {
            CreateObstacle(graphics, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
    }
    private void CreateObstacle(Graphics graphics, int posX, int posY, int sizeX, int sizeY)
    {
        Graphics2D obstacle  = (Graphics2D)graphics;
        obstacle.setColor(Color.red);
        obstacle.fillRect(posX,posY,sizeX,sizeY);
    }
}