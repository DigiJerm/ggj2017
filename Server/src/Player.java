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
        jsonObject.put(Util.JSON_KEY_Player_Health, health);
        jsonObject.put(Util.JSON_KEY_Player_HashCode, hashCode());
        return jsonObject;
    }

    public void addLine(Line line)
    {
        linkedLines.add(line);
    }

    public void setCharge(JSONObject jsonObject)
    {
        health = jsonObject.getInt(Util.JSON_KEY_Player_Value);
    }

    public void update()
    {
        //Moved to client
//        if(health > 0) health -= 1;
//        else if(health < 0) health += 1;

        for(Line line : linkedLines)
        {
            line.playerUpdate(health, this);
        }
    }
}
