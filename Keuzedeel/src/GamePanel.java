import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;

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
    ArrayList<Rectangle> path;

    boolean gameStarted = false;
    int currentLevel = 3;
    Player player = new Player(tileSize);
    Enemy enemy = new Enemy(tileSize);

    Node [] [] node = new Node [maxScreenCol] [maxScreenRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean goalReached = false;
    int step = 0;
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        StartAStar();
    }
    private void StartAStar()
    {
        int col = 0;
        int row = 0;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxScreenRow, maxScreenCol));
        while (col < maxScreenCol && row < maxScreenRow)
        {
            node [col] [row] = new Node (col, row);
            this.add(node [col] [row]);
            col++;
            if (col == maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
        setStartNode(3,6);
        setGoalNode(11,3);

        setSolidNode(10, 2);
        setSolidNode(10, 3);
        setSolidNode(10, 4);
        setSolidNode(10, 5);
        setSolidNode(10, 6);
        setSolidNode(10, 7);
        setSolidNode(6, 2);
        setSolidNode(7, 2);
        setSolidNode(8, 2);
        setSolidNode(9, 2);
        setSolidNode(11, 7);
        setSolidNode(12, 7);

        setCosts();
        search();
    }
    private void setStartNode(int col, int row)
    {
        node [col] [row].setAsStart();
        startNode = node [col] [row];
        currentNode = startNode;
    }
    private void setGoalNode(int col, int row)
    {
        node [col] [row].setAsGoal();
        goalNode = node [col] [row];
    }
    private void setSolidNode(int col, int row)
    {
        node [col] [row].setAsSolid();
    }
    private void setCosts()
    {
        int col = 0;
        int row = 0;

        while (col < maxScreenCol && row <maxScreenRow)
        {
            getCost(node [col] [row]);
            col++;
            if (col == maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
    }
    private void getCost(Node node)
    {
        //g cost, distance from start node
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //h cost, distance from goal node
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //f cost, total cost
        node.fCost = node.gCost + node.hCost;

        //display cost on node
        if (node != startNode && node != goalNode)
        {
            node.setText("<html>F:" +node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }
    public void search()
    {
        step = 0;
        while (goalReached == false && step < 300)
        {
            int col = currentNode.col;
            int row = currentNode.row;
            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (row -1 >= 0)
            {
                openNode(node[col][row - 1]);
            }
            if (row + 1 < maxScreenRow)
            {
                openNode(node[col][row + 1]);
            }
            if (col -1 >= 0)
            {
                openNode(node[col - 1][row]);
            }
            if (col +1 < maxScreenCol)
            {
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int  i = 0; i < openList.size(); i++)
            {
                if (openList.get(i).fCost < bestNodefCost)
                {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                else if (openList.get(i).fCost == bestNodefCost)
                {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost)
                    {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode)
            {
                goalReached = true;
                trackPath();
            }
            step++;
        }
    }
    private void openNode(Node node)
    {
        if (node.open == false && node.checked == false && node.solid == false)
        {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackPath()
    {
        Node current = goalNode;

        while (current != startNode)
        {
            current = current.parent;

            if (current != startNode)
            {
                current.setAsPath();
            }
        }
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
        int nextPlayerX = player.posX;
        int nextPlayerY = player.posY;
        if (!player.playerFinished) {
            if (keyHandler.upPressed) {
                nextPlayerY -= player.playerSpeed;
            }
            if (keyHandler.downPressed) {
                nextPlayerY += player.playerSpeed;
            }
            if (keyHandler.leftPressed) {
                nextPlayerX -= player.playerSpeed;
            }
            if (keyHandler.rightPressed) {
                nextPlayerX += player.playerSpeed;
            }
        }

        boolean collisionX = false;
        if (player.posX != nextPlayerX)
        {
            for (int i = 1; i <= player.playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(player.posX + (nextPlayerX - player.posX > 0 ? i : -i), player.posY, tileSize, tileSize);
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
                    player.posX = player.posX + (nextPlayerX - player.posX > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }

        boolean collisionY = false;
        if (player.posY != nextPlayerY)
        {
            for (int i = 1; i <= player.playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(player.posX, player.posY + (nextPlayerY - player.posY > 0 ? i : -i), tileSize, tileSize);
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
                    player.posY = player.posY + (nextPlayerY - player.posY > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }

        if (!player.playerFinished)
        {
            Rectangle playerRect = new Rectangle(player.posX, player.posY, tileSize, tileSize);
            for (Rectangle object : finish)
            {
                if (playerRect.intersects(object))
                {
                    System.out.println("finished");
                    player.playerFinished = true;
                }
            }

            ArrayList<Rectangle> objectsToRemove = new ArrayList<>();
            for (Rectangle object : healthPickups)
            {
                if (playerRect.intersects(object))
                {
                    objectsToRemove.add(object);
                    System.out.println("pickedUpHealth");
                    player.health = player.health + 20;
                }
            }
            for (Rectangle object :objectsToRemove)
            {
                healthPickups.remove(object);
            }

        }

        if ((collisionX || collisionY) == true)
        {
            player.health--;
            System.out.println("collided");
        }
        if (player.health <= 0)
        {
            LevelLoader();
        }
        moveEnemy();
    }

    private void moveEnemy()
    {
        if (enemy.posX < player.posX)
        {
            enemy.posX += enemy.speed;
        }
        else if (enemy.posX > player.posX)
        {
            enemy.posX -= enemy.speed;
        }

        if (enemy.posY < player.posY)
        {
            enemy.posY += enemy.speed;
        }
        else if (enemy.posY > player.posY)
        {
            enemy.posY -= enemy.speed;
        }
    }

    public void paintComponent(Graphics _graphics)
    {

        if (!gameStarted)
        {
            LevelLoader();
            gameStarted = true;
        }
        super.paintComponent(_graphics);
        Graphics2D graphics = (Graphics2D)_graphics;


        //Display all the obstacles
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
        if (player.playerFinished)
        {
            FontMetrics fm = graphics.getFontMetrics();
            String text = "Finished game";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int x = (screenWidth - textWidth) / 2;
            int y = (screenHeight - textHeight) / 2;
            graphics.drawString(text, x, y);
            currentLevel++;
            LevelLoader();
        }
        FontMetrics fm = graphics.getFontMetrics();
        String text = "Finished game";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (screenWidth - textWidth) / 12;
        int y = (screenHeight - textHeight) / 12;
        graphics.drawString(String.valueOf(player.health), x, y);
        //display enemy
        graphics.setColor(Color.blue);
        graphics.fillRect(enemy.posX,enemy.posY,tileSize,tileSize);
        //Display players
        graphics.setColor(Color.orange);
        graphics.fillRect(player.posX,player.posY,tileSize,tileSize);


    }
    private void LevelLoader()
    {
        player.health = player.maxHealth;
        player.posY = tileSize;
        player.posX = tileSize;
        player.playerFinished = false;
        obstacles = new ArrayList<>();
        finish = new ArrayList<>();
        healthPickups = new ArrayList<>();
        path = new ArrayList<>();

        if (obstacles != null)
        {
            obstacles.clear();

        }
        if (finish != null)
        {
            finish.clear();
        }
        if (path != null)
        {
            path.clear();
        }
        //left and right bar
        obstacles.add(new Rectangle(0, 0, tileSize, tileSize * maxScreenRow));
        obstacles.add(new Rectangle(screenWidth - tileSize, 0, tileSize, tileSize * maxScreenRow));
        //top and bottom bar
        obstacles.add(new Rectangle(0, 0, tileSize * screenWidth, tileSize));
        obstacles.add(new Rectangle(0, screenHeight - tileSize, tileSize * screenWidth, tileSize));

        if (currentLevel == 1)
        {
            //actual obstacles
            obstacles.add(new Rectangle(tileSize * 3, tileSize, tileSize, tileSize * 8));
            obstacles.add(new Rectangle(tileSize * 6, tileSize * 3, tileSize, tileSize * 8));
            obstacles.add(new Rectangle(tileSize * 9, tileSize, tileSize, tileSize * 8));
            obstacles.add(new Rectangle(tileSize * 12, tileSize * 3, tileSize, tileSize * 8));

            //finish
            finish.add(new Rectangle(screenWidth - tileSize * 3, screenHeight - tileSize * 2, tileSize * 2, tileSize * 2));

            //health pickups
            healthPickups.add(new Rectangle(tileSize, tileSize * 4, tileSize, tileSize));

        }
        else if (currentLevel == 2)
        {
            //finish
            finish.add(new Rectangle(screenWidth - tileSize * 3, screenHeight - tileSize * 2, tileSize * 2, tileSize * 2));
        }
        for (int i = 0; i < maxScreenCol; i++)
        {
            for (int j = 0; j < maxScreenRow; j++)
            {
                Rectangle tempRect = new Rectangle(tileSize * i, tileSize * j, tileSize, tileSize);
                boolean intersects = false;
                for (Rectangle object : obstacles)
                {
                    if (tempRect.intersects(object))
                    {
                        intersects = true;
                        break;
                    }
                }
                if (!intersects)
                {
                    path.add(tempRect);
                }
            }
        }

    }
    private void CreateObstacle(Graphics _graphics, int posX, int posY, int sizeX, int sizeY, Color color)
    {
        Graphics2D obstacle  = (Graphics2D)_graphics;
        obstacle.setColor(color);
        obstacle.fillRect(posX,posY,sizeX,sizeY);
    }
}