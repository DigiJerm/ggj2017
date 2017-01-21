/**
 * Created by Atraxi on 20/01/2017.
 */

import org.json.JSONObject;
import spark.Spark;
import spark.route.RouteOverview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Controller
{
    private static HashMap<Integer, Game> games = new HashMap<>();
    private static final int PORT = 4321;

    private static String indexHTML;

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

        Spark.get("/load/:gameHash", (request, response) ->
        {
            System.out.println("/load/"+request.params(":gameHash"));
            String tempOutput = games.get(Integer.parseInt(request.params(":gameHash")))
                                     .toJson().toString();
            System.out.println(tempOutput);
            return tempOutput;
        });

        Spark.put("/game/submit/:gameHash/:playerHash", (request, response) ->
        {
            System.out.println("/submit/"+request.params(":gameHash") +"/"+ request.params(":playerHash"));
            System.out.println("\t"+request.body());
            JSONObject jsonObject = new JSONObject(request.body());
            games.get(Integer.parseInt(request.params(":gameHash")))
                 .playerUpdate(Integer.parseInt(request.params(":playerHash")), jsonObject);
            return "{\"data\":\"received\"}";
        });

        Spark.post("/create/submit", (request, response) ->
        {
            System.out.println("/create/submit");
            System.out.println(request.body());
            Game game = new Game(new JSONObject(request.body()));
            games.put(game.hashCode(), game);
            System.out.println(game.jsonHash());
            return game.jsonHash();
        });

        Spark.get("/gameList", (request, response) ->
        {
            System.out.println("/gameList");
            JSONObject json = new JSONObject();
            games.forEach((key, value) ->
                          {
                              json.append(Util.JSON_KEY_GameHash, key)
                                      .append(Util.JSON_KEY_GameData, value.toJson());
                          });

            System.out.println(json.toString());
            return json.toString();
        });

        Spark.get("/", (request, response) ->
        {
            System.out.println("/");
            return indexHTML;
        });

        System.out.println("Server initialization complete. Listening on port " + PORT);
    }
}
