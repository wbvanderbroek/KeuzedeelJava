import java.awt.*;

public class UIDisplay
{
    GamePanel gp;
    public UIDisplay(GamePanel gp)
    {
        this.gp = gp;
    }

    public void draw(Graphics graphics)
    {
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial", Font.BOLD, 40));

        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(String.valueOf(gp.player.health));
        int textHeight = fm.getHeight();
        int x = (gp.screenWidth - textWidth) / 12;
        int y = (gp.screenHeight - textHeight) / 12;
        graphics.drawString(String.valueOf(gp.player.health), x, y);
    }
}
