import javax.swing.*;

public class Node extends JButton
{
    Node parent;

    int col;
    int row;

    int gCost;
    int hCost;
    int fCost;

    boolean solid;
    boolean open;
    boolean checked;

    public Node (int col, int row)
    {
        this.col = col;
        this.row = row;
    }
}
