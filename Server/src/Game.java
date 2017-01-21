import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Atraxi on 20/01/2017.
 */
public class Game implements Runnable
{
    private int gameHeight;
    private ArrayList<Line> lines;
    private ArrayList<Player> players;

    public Game(int playerCount, int gameWidth, int gameHeight)
    {
        this.gameHeight = gameHeight;
        this.players = new ArrayList<>(playerCount);
        this.lines = new ArrayList<>(playerCount);
        for(int index = 0; index < lines.size() - 1; index++)
        {
            lines.set(index, new Line(gameWidth, players.get(index), players.get(index + 1)));
        }
        lines.set(lines.size(), new Line(gameWidth, players.get(lines.size()), players.get(0)));
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
        JSONArray jsonArray = new JSONArray();
        for(Player player : players)
        {
            jsonArray.put(player.toJson());
        }
        JSONArray jsonArray1 = new JSONArray();
        for(Line line : lines)
        {
            jsonArray1.put(line.toJson());
        }
        json.put(Util.JSON_KEY_Players, jsonArray);
        json.put(Util.JSON_KEY_LinePosition, jsonArray1);

        return json;
    }

    @Override
    public void run()
    {
        boolean areAllPlayersReady = players.stream().noneMatch(x -> x.hasRequestedPause);
        while(areAllPlayersReady)
        {
            lines.forEach(Line::update);

            try{
                Thread.sleep(1000/60);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            areAllPlayersReady = players.stream().allMatch(x -> x.hasRequestedPause);
        }
    }

    public JSONObject jsonHash()
    {
        JSONObject json = new JSONObject();
        json.put(Util.JSON_KEY_GameHash, hashCode());
        return json;
    }

    public void playerUpdate(int i, JSONObject jsonObject)
    {
        players.get(i).update(jsonObject);
    }
}
