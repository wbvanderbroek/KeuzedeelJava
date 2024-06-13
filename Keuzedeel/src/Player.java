import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player
{
    public int health = 100;
    public int maxHealth = 100;
    public int posX;
    public int posY;
    public int playerSpeed = 5;
    boolean playerFinished = false;
    GamePanel gp;
    public BufferedImage playerSprite;

    public Player(GamePanel gp)
    {
        this.gp = gp;
        posX = gp.tileSize;
        posY = gp.tileSize;
    }
    public void update()
    {
        int nextPlayerX = posX;
        int nextPlayerY = posY;
        if (!playerFinished) {
            if (gp.keyHandler.upPressed) {
                nextPlayerY -= playerSpeed;
            }
            if (gp.keyHandler.downPressed) {
                nextPlayerY += playerSpeed;
            }
            if (gp.keyHandler.leftPressed) {
                nextPlayerX -= playerSpeed;
            }
            if (gp.keyHandler.rightPressed) {
                nextPlayerX += playerSpeed;
            }
        }

        boolean collisionX = false;
        if (posX != nextPlayerX)
        {
            for (int i = 1; i <= playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(posX + (nextPlayerX - posX > 0 ? i : -i), posY, gp.tileSize, gp.tileSize);
                collisionX = false;
                for (Rectangle obstacle : gp.obstacles)
                {
                    if (nextPlayerRectX.intersects(obstacle))
                    {
                        collisionX = true;
                        break;
                    }
                }
                if (!collisionX)
                {
                    posX = posX + (nextPlayerX - posX > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }

        boolean collisionY = false;
        if (posY != nextPlayerY)
        {
            for (int i = 1; i <= playerSpeed; i++)
            {
                Rectangle nextPlayerRectX = new Rectangle(posX, posY + (nextPlayerY - posY > 0 ? i : -i), gp.tileSize, gp.tileSize);
                collisionY = false;
                for (Rectangle obstacle : gp.obstacles)
                {
                    if (nextPlayerRectX.intersects(obstacle))
                    {
                        collisionY = true;
                        break;
                    }
                }
                if (!collisionY)
                {
                    posY = posY + (nextPlayerY - posY > 0 ? 1 : -1);
                }
                else
                {
                    break;
                }
            }
        }

        if (!playerFinished)
        {
            Rectangle playerRect = new Rectangle(posX, posY, gp.tileSize, gp.tileSize);
            for (Rectangle object : gp.finish)
            {
                if (playerRect.intersects(object))
                {
                    System.out.println("finished");
                    gp.player.playerFinished = true;
                }
            }

            ArrayList<Rectangle> objectsToRemove = new ArrayList<>();
            for (Rectangle object : gp.healthPickups)
            {
                if (playerRect.intersects(object))
                {
                    objectsToRemove.add(object);
                    System.out.println("pickedUpHealth");
                    health = gp.player.health + 20;
                }
            }
            for (Rectangle object :objectsToRemove)
            {
                gp.healthPickups.remove(object);
            }

        }

        if ((collisionX || collisionY) == true)
        {
            health--;
        }
        if (health <= 0)
        {
            gp.levelLoader.LoadLevel();
        }
    }
    public void draw(Graphics graphics)
    {
        getPlayerImage();
        BufferedImage image = playerSprite;
        graphics.drawImage(playerSprite, posX, posY, gp.tileSize, gp.tileSize, null);
    }
    public void getPlayerImage()
    {
        try
        {
            playerSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/roblox-smirk.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}