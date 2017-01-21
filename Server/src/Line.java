import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Atraxi on 21/01/2017.
 */
public class Line
{
    private ArrayList<Integer> pointSize;
    private ArrayList<Boolean> pointDirectionIsLeft;
    private Player player0;
    private Player player1;


    public Line(int gameWidth, Player player0, Player player1)
    {
        this.pointSize = new ArrayList<>(gameWidth);
        this.pointDirectionIsLeft = new ArrayList<>(gameWidth);
        this.player0 = player0;
        this.player1 = player1;
    }

    public void update()
    {
        ArrayList<Integer> newPointSize = new ArrayList<>(pointSize.size());
        ArrayList<Boolean> newPointDirectionIsLeft = new ArrayList<>(pointDirectionIsLeft.size());

        for(int index = 1; index < pointSize.size() - 1; index++)
        {
            if(pointDirectionIsLeft.get(index))
            {
                //newPointDirectionIsLeft =
            }
            else
            {

            }
        }
        synchronized(this)
        {

        }
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put(Util.JSON_KEY_Line_size, pointSize);
        json.put(Util.JSON_KEY_Line_direction, pointDirectionIsLeft);
        json.put(Util.JSON_KEY_Line_player0, player0.hashCode());
        json.put(Util.JSON_KEY_Line_player1, player1.hashCode());
        return json;
    }

    public void playerAction(int data, Player source)
    {
        synchronized(this)
        {
            if(source == player0)
            {
                pointDirectionIsLeft.set(0, !(Math.abs(data) >= Math.abs(pointSize.get(0))));
                pointSize.set(0, pointSize.get(0) + data);
            }
            else if(source == player1)
            {
                pointDirectionIsLeft.set(pointDirectionIsLeft.size(), Math.abs(data) >= Math.abs(pointSize.get(pointSize.size())));
                pointSize.set(pointSize.size(), pointSize.get(pointSize.size()) + data);
            }
        }
    }
}
