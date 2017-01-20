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
        Spark.port(4321);

        Spark.get("/load/:gameHash", (request, response) ->
        {
            System.out.println("/load/"+request.params(":gamehash"));
            return "{\"data\":\"test\"}";//games.get(request.params(":gameHash")).toJson().toString();
        });

        Spark.put("/submit/:gameHash/:playerIndex", (request, response) ->
        {
            System.out.println("/submit/"+request.params(":gamehash") +"/"+ request.params(":playerIndex"));
            request.params().forEach((x, y) -> System.out.println("\t"+ x + "||" + y));
            System.out.println("\t"+request.body());
            for(String string : request.splat())
            {
                System.out.println(string);
            }
            return "{\"data\":\"received\"}";//games.get(request.params(":gameHash")).playerUpdate().toString();
        });

        Spark.post("/submit/:gameHash/:playerIndex", (request, response) ->
        {
            System.out.println("/submit/"+request.params(":gamehash") +"/"+ request.params(":playerIndex"));
            request.params().forEach((x, y) -> System.out.println("\t"+ x + "||" + y));
            System.out.println("\t"+request.body());
            for(String string : request.splat())
            {
                System.out.println(string);
            }
            return "{\"data\":\"received\"}";//games.get(request.params(":gameHash")).playerUpdate().toString();
        });

        Spark.put("/create/submit", (request, response) ->
        {

            return "{\"data\":\"received\"}";
        });
        Spark.get("/create", (request, response) ->
        {

            return "testCreate";
        });

        Spark.get("/", (request, response) ->
        {

            return "testIndex";
        });
    }
}
