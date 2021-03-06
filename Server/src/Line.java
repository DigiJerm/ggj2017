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
    private Random random = new Random();

    public Line(int gameWidth, Player player0, Player player1)
    {
        this.pointSize = new int[gameWidth];
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

        synchronized(this)
        {
            for(int index = 0; index < pointSize.length; index++)
            {
                if(pointSize[index] != 0)
                {
                    if(pointDirectionIsLeft[index])
                    {
                        if(index == 0 && pointSize[index] != 0)
                        {
                            System.err.println("Player -> Wins");
                            player0.setIsDead();
                        }
                        else
                        {
                            //Determine direction
                            if(newPointSize[index - 1] == 0 || Math.abs(pointSize[index]) > Math.abs(newPointSize[index - 1]))
                            {//Moving left
                                newPointDirectionIsLeft[index - 1] = true;
                            }
                            else if(Math.abs(newPointSize[index - 1]) > Math.abs(pointSize[index]) && !newPointDirectionIsLeft[index - 1])
                            {//Moving right
                                newPointDirectionIsLeft[index - 1] = false;
                            }
                            else if(Math.abs(newPointSize[index - 1]) == Math.abs(pointSize[index]))
                            {
                                newPointDirectionIsLeft[index - 1] = random.nextBoolean();
                            }
                            else
                            {
                                System.err.println("direction unknown!!!!!!!!!!!!!!");

                            }

                            //Determine size
                            newPointSize[index - 1] += pointSize[index];
                        }
                    }
                    else
                    {
                        if(index == pointSize.length - 1 && pointSize[index] != 0)
                        {
                            System.err.println("Player <- Wins");
                            player1.setIsDead();
                        }
                        else
                        {
                            //Determine direction
                            if(newPointSize[index + 1] == 0 || Math.abs(pointSize[index]) > Math.abs(newPointSize[index + 1]))
                            {//Moving right
                                newPointDirectionIsLeft[index + 1] = false;
                            }
                            else if(Math.abs(newPointSize[index + 1]) > Math.abs(pointSize[index]) && newPointDirectionIsLeft[index + 1])
                            {//Moving left
                                newPointDirectionIsLeft[index + 1] = false;
                            }
                            else if(Math.abs(newPointSize[index + 1]) == Math.abs(pointSize[index]))
                            {
                                newPointDirectionIsLeft[index + 1] = random.nextBoolean();
                            }
                            else
                            {
                                System.err.println("direction unknown!!!!!!!!!!!!!!");

                            }

                            //Determine size
                            newPointSize[index + 1] += pointSize[index];
                        }
                    }
                }
            }

            pointSize = newPointSize;
            pointDirectionIsLeft = newPointDirectionIsLeft;
        }
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put(Util.JSON_KEY_Line_Size, pointSize);
        //json.put(Util.JSON_KEY_Line_Direction, pointDirectionIsLeft);
        json.put(Util.JSON_KEY_Line_Player0, player0.hashCode());
        json.put(Util.JSON_KEY_Line_Player1, player1.hashCode());
        json.put(Util.JSON_KEY_Line_HashCode, hashCode());
        return json;
    }

    public void playerUpdate(int data, Player source)
    {
        synchronized(this)
        {
            if(source == player0)
            {
                pointDirectionIsLeft[0] = pointDirectionIsLeft[0] &&
                                          Math.abs(data) < Math.abs(pointSize[0]);
                pointSize[0] = pointSize[0] + data;
            }
            else if(source == player1)
            {
                pointDirectionIsLeft[pointDirectionIsLeft.length - 1] = pointDirectionIsLeft[pointDirectionIsLeft.length - 1] ||
                                                                      Math.abs(data) >= Math.abs(pointSize[pointSize.length - 1]);
                pointSize[pointSize.length - 1] = pointSize[pointSize.length - 1] + data;
            }
            else
            {
                System.err.println("Unknown player:"+source.toJson());
            }
        }
    }

    public void setToRandom(int gameHeight)
    {
        for(int index = 0; index < pointSize.length; index++)
        {
            pointSize[index] = random.nextInt(gameHeight) - gameHeight/2;
        }
    }
}
