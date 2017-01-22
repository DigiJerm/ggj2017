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
    public int globalGameIndex;//for ordering games by time of creation

    private Game(int playerCount, int gameWidth, int gameHeight, int gameIndex)
    {
        this.globalGameIndex = gameIndex;
        this.gameHeight = gameHeight;

        this.players = new ArrayList<>(playerCount);
        for(int index = 0; index < playerCount; index++)
        {
            players.add(new Player());
        }

        int lineCount = playerCount;
        lineCount = lineCount == 2 ? 1 : lineCount;
        this.lines = new ArrayList<>(lineCount);

        for(int index = 0; index < lineCount - 1; index++)
        {
            lines.add(new Line(gameWidth, players.get(index), players.get(index + 1)));
        }
        lines.add(new Line(gameWidth, players.get(players.size()-1), players.get(0)));
    }

    public Game(JSONObject jsonObject, int gameCount)
    {
        this(jsonObject.getInt(Util.JSON_KEY_PlayerCount),
             jsonObject.getInt(Util.JSON_KEY_GameWidth),
             jsonObject.getInt(Util.JSON_KEY_GameHeight),
             gameCount);
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
        json.put(Util.JSON_KEY_Game_PlayerList, jsonArray);
        json.put(Util.JSON_KEY_Game_Lines, jsonArray1);

        return json;
    }

    @Override
    public void run()
    {
        //boolean areAllPlayersReady = players.stream().noneMatch(x -> x.hasRequestedPause);
        final boolean[] areAllPlayersAlive = {true};
        while(areAllPlayersAlive[0])
        {
            players.forEach((player) -> areAllPlayersAlive[0] &= player.update());
            lines.forEach(Line::update);

            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            //areAllPlayersReady = players.stream().allMatch(x -> x.hasRequestedPause);
        }
        lines.forEach(line -> line.setToRandom(gameHeight));
    }

    public JSONObject jsonHash()
    {
        JSONObject json = new JSONObject();
        json.put(Util.JSON_KEY_Game_HashCode, hashCode());
        for(Player player : players)
        {
            json.append(Util.JSON_KEY_Game_PlayerList, player.hashCode());
        }
        return json;
    }

    public void playerUpdate(int hashCode, JSONObject jsonObject)
    {
        getPlayerByHash(hashCode).setCharge(jsonObject, gameHeight);
    }

    public Line getLineByHash(int hashcode)
    {
        return lines.stream().filter(x -> x.hashCode() == hashcode).findFirst().get();
    }

    public Player getPlayerByHash(int hashcode)
    {
        return players.stream().filter(x -> x.hashCode() == hashcode).findFirst().get();
    }
}
