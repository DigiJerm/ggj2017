/**
 * Created by Atraxi on 20/01/2017.
 */

import org.json.JSONObject;
import spark.Spark;
import spark.route.RouteOverview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;

public class Controller
{
    private static HashMap<Integer, Game> games = new HashMap<>();
    private static final int PORT = 4321;

    private static String indexHTML;

    private static int gameCount = 0;

    public static void main(String[] args) throws IOException
    {
        loadPageData();
        sparkInitialization();
    }

    private static void loadPageData() throws IOException
    {
        indexHTML = new String(Files.readAllBytes(Paths.get("Client\\index.html")));
    }

    private static void sparkInitialization()
    {
        System.out.println("Initializing embedded SparkJava/Jetty servlet/server");

        Spark.port(PORT);

        Spark.staticFiles.externalLocation("Client");

        RouteOverview.enableRouteOverview();

        Spark.get("/load/:gameHash/player/:playerHash", (request, response) ->
        {
//            System.out.println("/load/"+request.params(":gameHash")+"/player/"+request.params(":playerHash"));
            Game game = games.get(Integer.parseInt(request.params(":gameHash")));
            if(game != null)
            {
                Player player = game.getPlayerByHash(Integer.parseInt(request.params(":playerHash")));
                if(player != null)
                {
                    String output = player.toJson().toString();
//                    System.out.println("\t"+output);
                    return output;
                }
                else
                {
                    response.status(418);
                    return "{\"data\":\"player_not_found\"}";
                }
            }
            else
            {
                response.status(418);
                return "{\"data\":\"game_not_found\"}";
            }
        });

        Spark.get("/load/:gameHash/line/:lineHash", (request, response) ->
        {
//            System.out.println("/load/"+request.params(":gameHash")+"/line/"+request.params(":lineHash"));
            Game game = games.get(Integer.parseInt(request.params(":gameHash")));
            if(game != null)
            {
                Line line = game.getLineByHash(Integer.parseInt(request.params(":lineHash")));
                if(line != null)
                {
                    String output = line.toJson().toString();
//                    System.out.println("\t"+output);
                    return output;
                }
                else
                {
                    response.status(418);
                    return "{\"data\":\"line_not_found\"}";
                }
            }
            else
            {
                response.status(418);
                return "{\"data\":\"game_not_found\"}";
            }
        });

        Spark.get("/load/:gameHash", (request, response) ->
        {
//            System.out.println("/load/"+request.params(":gameHash"));
            Game game = games.get(Integer.parseInt(request.params(":gameHash")));
            if(game != null)
            {
                String output = game.toJson().toString();
//                System.out.println("\t"+output);
                return output;
            }
            else
            {
                response.status(418);
                return "{\"data\":\"game_not_found\"}";
            }
        });

        Spark.put("/game/submit/:gameHash/:playerHash", (request, response) ->
        {
            System.out.println("/game/submit/"+request.params(":gameHash") +"/"+ request.params(":playerHash"));
            System.out.println("\t"+request.body());
            JSONObject jsonObject = new JSONObject(request.body());
            if(jsonObject.get(Util.JSON_KEY_Controller_Action).equals(Util.JSON_VALUE_Controller_Action_Pulse) ||
               jsonObject.get(Util.JSON_KEY_Controller_Action).equals(Util.JSON_VALUE_Controller_Action_Charge))
            {
                Game game = games.get(Integer.parseInt(request.params(":gameHash")));
                if(game != null)
                {
                    game.playerUpdate(Integer.parseInt(request.params(":playerHash")), jsonObject);
                    return "{\"data\":\"received\"}";
                }
                else
                {
                    response.status(418);
                    return "{\"data\":\"game_not_found\"}";
                }
            }
            else
            {
                response.status(418);
                return "\"data\":\"unknown_type\"}";
            }
        });

        Spark.post("/create/submit", (request, response) ->
        {
            System.out.println("/create/submit");
            System.out.println("\t"+request.body());
            Game game = new Game(new JSONObject(request.body()), gameCount);
            gameCount++;
            games.put(game.hashCode(), game);
            JSONObject gameData = game.jsonHash();
            System.out.println("\t"+gameData);
            new Thread(game).start();
            return gameData;
        });

        Spark.get("/gameList", (request, response) ->
        {
            System.out.println("/gameList");
            JSONObject json = new JSONObject();
            games.values().stream()
                 .sorted(Comparator.comparingInt(game -> game.globalGameIndex))
                 .forEach((gameConsumer) -> json.append(Util.JSON_KEY_GameData, gameConsumer.jsonHash()));
            System.out.println("\t"+json.toString());
            return json.toString();
        });

        Spark.get("/", (request, response) ->
        {
            System.out.println("/");
            return indexHTML;
        });

        Spark.exception(Exception.class, (exception, request, response) ->
        {
            System.err.println("URL:"+request.url());
            System.err.println("Body:"+request.body());
            exception.printStackTrace();
        });

        System.out.println("Server initialization complete. Listening on port " + PORT);
    }
}
