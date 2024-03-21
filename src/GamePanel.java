import javax.swing.*;
import java.awt.*;

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
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
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
        double nextDrawTime = System.nanoTime() + drawInterval;


        while (gameThread != null)
        {
            update();
            repaint();
            try
            {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }
                Thread.sleep((long)(remainingTime));
                nextDrawTime += drawInterval;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void update()
    {
        if (keyHandler.upPressed == true)
        {
            playerY -= playerSpeed;
        }
        if (keyHandler.downPressed == true)
        {
            playerY += playerSpeed;

        }
        if (keyHandler.leftPressed == true)
        {
            playerX -= playerSpeed;
        }
        if (keyHandler.rightPressed == true)
        {
            playerX += playerSpeed;
        }

    }
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D graphics2D  = (Graphics2D)graphics;

        graphics2D.setColor(Color.white);
        graphics2D.fillRect(playerX,playerY,tileSize,tileSize);
        graphics2D.dispose();
    }
}