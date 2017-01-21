import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Atraxi on 21/01/2017.
 */
public class Line
{
    private int[] pointSize;
    private boolean[] pointDirectionIsLeft;
    private Player player0;
    private Player player1;


    public Line(int gameWidth, Player player0, Player player1)
    {
        this.pointSize = new int[gameWidth];
        Random random = new Random();
        for(int index = 0; index < gameWidth; index++)
        {
            pointSize[index] = random.nextInt(900) - 450;
        }
        this.pointDirectionIsLeft = new boolean[gameWidth];
        this.player0 = player0;
        player0.addLine(this);
        this.player1 = player1;
        player1.addLine(this);
    }

    public void update()
    {
        int[] newPointSize = new int[pointSize.length];
        boolean[] newPointDirectionIsLeft = new boolean[pointDirectionIsLeft.length];
//        Random random = new Random();
//        for(int index = 0; index < pointSize.length; index++)
//        {
//            pointSize[index] = random.nextInt(900) - 450;
//        }
        for(int index = 0; index < pointSize.length; index++)
        {
            if(pointDirectionIsLeft[index])
            {
                if(index == 0)
                {
                    System.err.println("Player -> Wins");
                }
                else
                {
                    newPointSize[index-1] = pointSize[index];
                    newPointDirectionIsLeft[index-1] = true;
                }
            }
            else
            {
                if(index == pointSize.length-1)
                {
                    System.err.println("Player <- Wins");
                }
                else
                {
                    newPointSize[index+1] = pointSize[index];
                    newPointDirectionIsLeft[index+1] = false;
                }
            }
        }

        synchronized(this)
        {
            pointSize = newPointSize;
            pointDirectionIsLeft = newPointDirectionIsLeft;
        }
    }

    //private int getSumOfN

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put(Util.JSON_KEY_Line_Size, pointSize);
        json.put(Util.JSON_KEY_Line_Direction, pointDirectionIsLeft);
        json.put(Util.JSON_KEY_Line_Player0, player0.hashCode());
        json.put(Util.JSON_KEY_Line_Player1, player1.hashCode());
        json.put(Util.JSON_KEY_Line_HashCode, hashCode());
        return json;
    }

    public void playerAction(int data, Player source)
    {
        synchronized(this)
        {
            if(source == player0)
            {
                pointDirectionIsLeft[0] = pointDirectionIsLeft[0] &
                                          Math.abs(data) < Math.abs(pointSize[0]);
                pointSize[0] = pointSize[0] + data;
            }
            else if(source == player1)
            {
                pointDirectionIsLeft[pointDirectionIsLeft.length - 1] = pointDirectionIsLeft[pointDirectionIsLeft.length - 1] |
                                                                      Math.abs(data) >= Math.abs(pointSize[pointSize.length - 1]);
                pointSize[pointSize.length - 1] = pointSize[pointSize.length - 1] + data;
            }
        }
    }
}
