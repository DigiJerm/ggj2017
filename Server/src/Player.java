import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Atraxi on 21/01/2017.
 */
public class Player
{
    private int health;
    public boolean hasRequestedPause = false;
    private ArrayList<Line> linkedLines;

    public Player()
    {
        this.health = 0;
       this.linkedLines = new ArrayList<>(2);
    }

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append(Util.JSON_KEY_Player_Health, health);
        return jsonObject;
    }

    public void update(JSONObject jsonObject)
    {
        for(Line line : linkedLines)
        {
            line.playerAction(jsonObject.getInt("data"), this);
        }
    }
}
