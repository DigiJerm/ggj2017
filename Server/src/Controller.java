/**
 * Created by Atraxi on 20/01/2017.
 */

import spark.Spark;
import spark.route.RouteOverview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Controller
{
    private static HashMap<String, Game> games = new HashMap<>();
    private static final int PORT = 4321;

    private static String indexHTML;
    private static String createHTML;

    public static void main(String[] args) throws IOException
    {
        loadPageData();
        sparkInitialization();
    }

    private static void loadPageData() throws IOException
    {
        indexHTML = new String(Files.readAllBytes(Paths.get("Client\\index.html")));
        createHTML = new String(Files.readAllBytes(Paths.get("Client\\create.html")));
    }

    private static void sparkInitialization()
    {
        System.out.println("Initializing embedded SparkJava/Jetty servlet/server");

        Spark.port(PORT);

        Spark.staticFiles.externalLocation("Client");

        RouteOverview.enableRouteOverview();

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

        Spark.post("/create/submit", (request, response) ->
        {

            return "{\"data\":\"received\"}";
        });

        Spark.get("/create", (request, response) -> createHTML);

        Spark.get("/", (request, response) -> indexHTML);

        System.out.println("Server initialization complete. Listening on port " + PORT);
    }
}
