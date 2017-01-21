import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Atraxi on 20/01/2017.
 */
public class Game implements Runnable
{
    private int gameHeight;
    private ArrayList<Integer> points;
    private ArrayList<Integer> playerPositions;
    private ArrayList<Boolean> hasPlayerRequestedPause;

    public Game(int playerCount, int gameWidth, int gameHeight)
    {
        this.gameHeight = gameHeight;
        this.playerPositions = new ArrayList<>(playerCount);
        this.hasPlayerRequestedPause = new ArrayList<>(playerCount);
        this.points = new ArrayList<>(gameWidth);
    }

    public Game(JSONObject jsonObject)
    {
        this(jsonObject.getInt(Util.JSON_KEY_PlayerCount),
             jsonObject.getInt(Util.JSON_KEY_GameWidth),
             jsonObject.getInt(Util.JSON_KEY_GameHeight));
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.append(Util.JSON_KEY_PlayerPosition, playerPositions);
        json.append(Util.JSON_KEY_LinePosition, points);

        return json;
    }

    public JSONObject playerUpdate()
    {



        return toJson();
    }

    @Override
    public void run()
    {
        boolean areAllPlayersReady = hasPlayerRequestedPause.stream().allMatch(x -> x);
        while(areAllPlayersReady)
        {
            ArrayList<Integer> newPoints = new ArrayList<>(points.size());

            for(int index = 1; index < points.size() - 2; index++)
            {

            }
            try
            {
                Thread.sleep(1000/60);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            areAllPlayersReady = hasPlayerRequestedPause.stream().allMatch(x -> x);
        }
    }

    public JSONObject jsonHash()
    {
        JSONObject json = new JSONObject();
        json.append(Util.JSON_KEY_GameHash, hashCode());
        return null;
    }
}
