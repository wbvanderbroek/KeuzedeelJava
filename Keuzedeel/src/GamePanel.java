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

    public PathFinder pathFinder;
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

        //Display all the objects
        for (Rectangle obstacle : obstacles)
        {
            CreateObstacle(_graphics, obstacle.x, obstacle.y, obstacle.width, obstacle.height, Color.red);
        }
        for (Rectangle object : path)
        {
            CreateObstacle(_graphics, object.x, object.y, object.width, object.height, Color.white);
        }
        for (Rectangle object : healthPickups)
        {
            CreateObstacle(_graphics, object.x, object.y, object.width, object.height, Color.green);
        }
        for (Rectangle object : finish)
        {
            CreateObstacle(_graphics, object.x, object.y, object.width, object.height, Color.DARK_GRAY);
        }
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial", Font.BOLD, 40));

        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(String.valueOf(player.health));
        int textHeight = fm.getHeight();
        int x = (screenWidth - textWidth) / 12;
        int y = (screenHeight - textHeight) / 12;
        graphics.drawString(String.valueOf(player.health), x, y);
        //Display enemy
        graphics.setColor(Color.blue);
        graphics.fillRect(enemy.posX,enemy.posY,tileSize,tileSize);
        //Display players
        graphics.setColor(Color.orange);
        graphics.fillRect(player.posX,player.posY,tileSize,tileSize);
    }

    private void CreateObstacle(Graphics _graphics, int posX, int posY, int sizeX, int sizeY, Color color)
    {
        Graphics2D obstacle  = (Graphics2D)_graphics;
        obstacle.setColor(color);
        obstacle.fillRect(posX,posY,sizeX,sizeY);
    }
}