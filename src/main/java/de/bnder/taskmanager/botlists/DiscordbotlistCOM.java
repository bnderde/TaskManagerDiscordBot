package de.bnder.taskmanager.botlists;

import de.bnder.taskmanager.main.Main;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class DiscordbotlistCOM {

    private static final String baseURL = "https://discordbotlist.com/api/v1";
    private static final String apiKey = Main.dotenv.get("DISCORDBOTLIST.COM_API_KEY") != null ? Main.dotenv.get("DISCORDBOTLIST.COM_API_KEY") : System.getenv("DISCORDBOTLIST.COM_API_KEY");

    public static void sendServerCount(int serverCount, String botID) throws IOException {
        System.out.println("Updating Servers on " + baseURL);
        final Connection.Response response = Jsoup.connect(baseURL + "/bots/"+ botID + "/stats")
                .header("Authorization", apiKey)
                .data("guilds", String.valueOf(serverCount))
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute();
        if (response.statusCode() == 200){
            System.out.println("Success!");
        } else {
            System.out.println("Failed! (" + response.statusCode() + ")");
        }
    }

}
