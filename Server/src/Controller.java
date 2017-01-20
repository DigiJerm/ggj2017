/**
 * Created by Atraxi on 20/01/2017.
 */

import spark.Spark;

import java.util.HashMap;

public class Controller
{
    private static HashMap<String, Game> games = new HashMap<>();

    public static void main(String[] args)
    {
        Spark.get("/load/:gameHash", (request, response) ->
        {

            return games.get(request.params(":gameHash")).toJson().toString();
        });

        Spark.put("/submit/:gameHash/:playerIndex", (request, responese) ->
        {

            return games.get(request.params(":gameHash")).playerUpdate().toString();
        });

        Spark.put("/create/submit", (request, response) ->
        {

            return "";
        });
        Spark.get("/create", (request, response) ->
        {

            return "";
        });

        Spark.get("/", (request, response) ->
        {

            return "";
        });
    }
}
