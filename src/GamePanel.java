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

    Thread gameThread;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }
    public void StartGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run()
    {
        while (gameThread != null)
        {
            System.out.println("adadald");
        }
    }
}