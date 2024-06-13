import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;

    ArrayList<Rectangle> obstacles;
    ArrayList<Rectangle> finish;
    ArrayList<Rectangle> healthPickups;
    public ArrayList<Rectangle> path;
    public ArrayList<Rectangle> allObstacles = new ArrayList<>();

    boolean gameStarted = false;

    public Player player = new Player(this);
    Enemy enemy = new Enemy(this);
    LevelLoader levelLoader = new LevelLoader(this);
    UIDisplay uiDisplay = new UIDisplay(this);
    public PathFinder pathFinder;
    BufferedImage finishSprite;

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
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update()
    {
        player.update();
        enemy.update();
        if (player.playerFinished)
        {
            levelLoader.currentLevel++;
            levelLoader.LoadLevel();
        }
    }
    public void paintComponent(Graphics _graphics)
    {
        if (!gameStarted)
        {
            levelLoader.LoadLevel();
            gameStarted = true;
        }
        super.paintComponent(_graphics);
        Graphics2D graphics = (Graphics2D)_graphics;
        getImage();
        //Display all the objects
        for (Rectangle obstacle : obstacles)
        {
            CreateObstacle(_graphics, obstacle.x, obstacle.y, obstacle.width, obstacle.height, Color.decode("#006836"));
        }
        for (Rectangle object : path)
        {
            CreateObstacle(_graphics, object.x, object.y, object.width, object.height, Color.decode("#9b6836"));
        }
        for (Rectangle object : healthPickups)
        {
            CreateObstacle(_graphics, object.x, object.y, object.width, object.height, Color.green);
        }
        BufferedImage image = finishSprite;

        for (Rectangle object : finish)
        {
            graphics.drawImage(finishSprite, object.x, object.y, object.width, object.height, null);

        }

        uiDisplay.draw(graphics);
        player.draw(graphics);
        enemy.draw(graphics);

        graphics.dispose();
    }
    public void getImage()
    {
        try
        {
            finishSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/exit-icon.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void CreateObstacle(Graphics _graphics, int posX, int posY, int sizeX, int sizeY, Color color)
    {
        Graphics2D obstacle  = (Graphics2D)_graphics;
        obstacle.setColor(color);
        obstacle.fillRect(posX,posY,sizeX,sizeY);
    }
}