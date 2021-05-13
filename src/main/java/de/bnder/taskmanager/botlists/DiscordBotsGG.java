package de.bnder.taskmanager.botlists;

import de.bnder.taskmanager.main.Main;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class DiscordBotsGG {

    private static final String baseURL = "https://discord.bots.gg/api/v1";
    private static final String apiKey = Main.dotenv.get("DISCORD.BOTS.GG_API_KEY") != null ? Main.dotenv.get("DISCORD.BOTS.GG_API_KEY") : System.getenv("DISCORD.BOTS.GG_API_KEY");

    public static void sendServerCount(int serverCount, String botID) throws IOException {
        System.out.println("Updating Servers on " + baseURL);
        final Connection.Response response = Jsoup.connect(baseURL + "/bots/" + botID + "/stats")
                .header("Authorization", apiKey)
                .data("guildCount", String.valueOf(serverCount))
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
