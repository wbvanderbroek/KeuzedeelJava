import java.awt.*;
import java.sql.Struct;
import java.util.ArrayList;

public class PathFinder
{
    GamePanel gp;
    Node [] [] node;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp)
    {
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes()
    {
        node = new Node[gp.maxScreenCol][gp.maxScreenRow];
        int col = 0;
        int row = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            node [col] [row] = new Node (col, row);

            col++;
            if (col == gp.maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
    }
    public void resetNodes()
    {
        int col = 0;
        int row = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            node [col] [row].open = false;
            node [col] [row].checked = false;
            node [col] [row].solid = false;

            col++;
            if (col == gp.maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow)
    {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        for (Rectangle object: gp.allObstacles)
        {
            node [object.x / gp.tileSize][object.y / gp.tileSize].solid = true;
            getCost(node [object.x / gp.tileSize][object.y / gp.tileSize]);
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
    }
    public boolean search()
    {
        step = 0;
        while (goalReached == false && step < 500)
        {
            int col = currentNode.col;
            int row = currentNode.row;
            currentNode.checked = true;

            openList.remove(currentNode);

            if (row -1 >= 0)
            {
                openNode(node[col][row - 1]);
            }
            if (row + 1 < gp.maxScreenRow)
            {
                openNode(node[col][row + 1]);
            }
            if (col -1 >= 0)
            {
                openNode(node[col - 1][row]);
            }
            if (col +1 < gp.maxScreenCol)
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
            if (openList.size() == 0)
            {
                break;
            }
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode)
            {
                goalReached = true;
                trackPath();
            }
            step++;
        }
        return goalReached;
    }
    private void openNode(Node node)
    {
        if (node.open == false && node.checked == false && node.solid == false)
        {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    private void trackPath()
    {
        Node current = goalNode;
        while (current != startNode)
        {
            pathList.add(0, current);
            current = current.parent;
        }
    }

}
