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

    private Game(int playerCount, int gameWidth, int gameHeight)
    {
        this.gameHeight = gameHeight;
        this.players = new ArrayList<>(playerCount);
        this.lines = new ArrayList<>(playerCount);
        players.add(new Player());
        for(int index = 0; index < playerCount - 1; index++)
        {
            players.add(new Player());
            lines.add(new Line(gameWidth, players.get(index), players.get(index + 1)));
        }
        lines.add(new Line(gameWidth, players.get(players.size()-1), players.get(0)));
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
        json.put(Util.JSON_KEY_Game_HashCode, hashCode());
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
        json.put(Util.JSON_KEY_Game_HashCode, hashCode());
        return json;
    }

    public void playerUpdate(int i, JSONObject jsonObject)
    {
        players.get(i).update(jsonObject);
    }
}
