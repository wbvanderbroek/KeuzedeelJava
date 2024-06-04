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
    int playerX = 100;
    int playerY = 100;
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
        obstacles.add(new Rectangle(100, 200, tileSize, tileSize));
        obstacles.add(new Rectangle(200, 200, tileSize, tileSize));
        obstacles.add(new Rectangle(300, 200, tileSize, tileSize));
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

        Rectangle nextPlayerRect = new Rectangle(nextPlayerX, nextPlayerY, tileSize, tileSize);
        boolean collision = false;
        for (Rectangle obstacle : obstacles)
        {
            if (nextPlayerRect.intersects(obstacle))
            {
                collision = true;
                break;
            }
        }

        if (!collision) {
            playerX = nextPlayerX;
            playerY = nextPlayerY;
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