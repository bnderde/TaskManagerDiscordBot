package de.bnder.taskmanager.botlists;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class UpdateLists {

    public static void updateBotLists(int servers, String botID) {
        try {
            final int ownAPIStatusCode = updateOwnStats(servers);
            if (ownAPIStatusCode == 200) {
                final int serverCount = getTotalServers();
                TopGG.sendServerCount(serverCount, botID);
                DiscordbotlistCOM.sendServerCount(serverCount, botID);
                DiscordBotsGG.sendServerCount(serverCount, botID);
                TopcordXYZ.sendServerCount(serverCount, botID);
                BlistXYZ.sendServerCount(serverCount, botID);
                //BotlistsCOM.sendServerCount(serverCount);
                DiscordBOATS.sendServerCount(serverCount, botID);
                VoidbotsNET.sendServerCount(serverCount, botID);
                InfinitybotlistCOM.sendServerCount(serverCount, botID);
                SpacebotlistORG.sendServerCount(serverCount, botID);
                System.out.println("Updating lists finished");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int updateOwnStats(final int servers) throws IOException {
<<<<<<< Updated upstream:src/main/java/de/bnder/taskmanager/lists/UpdateLists.java
        final Connection.Response response = Jsoup.connect(Main.requestURL + "/stats/shard/" + Main.shard + "/servers")
                .method(Connection.Method.POST)
                .header("authorization", "TMB " + Main.authorizationToken)
                .header("user_id", "---")
                .data("servers", String.valueOf(servers))
                .timeout(de.bnder.taskmanager.utils.Connection.timeout)
                .userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final Connection.Response response = Main.tmbAPI("stats/shard/" + Main.shard + "/servers", null, Connection.Method.POST, null).data("servers", String.valueOf(servers)).execute();
>>>>>>> Stashed changes:src/main/java/de/bnder/taskmanager/botlists/UpdateLists.java
        return response.statusCode();
    }

    public static int getTotalServers() throws IOException {
<<<<<<< Updated upstream:src/main/java/de/bnder/taskmanager/lists/UpdateLists.java
        final Connection.Response response = Jsoup.connect(Main.requestURL + "/stats")
                .method(Connection.Method.GET)
                .header("authorization", "TMB " + Main.authorizationToken)
                .header("user_id", "---")
                .timeout(de.bnder.taskmanager.utils.Connection.timeout)
                .userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final Connection.Response response = Main.tmbAPI("stats", null, Connection.Method.GET, null).execute();
>>>>>>> Stashed changes:src/main/java/de/bnder/taskmanager/botlists/UpdateLists.java
        if (response.statusCode() == 200) {
            final JsonObject jsonObject = Json.parse(response.body()).asObject();
            final int shard0Servers = jsonObject.getInt("servers_shard_0", 0);
            final int shard1Servers = jsonObject.getInt("servers_shard_1", 0);
            return shard0Servers + shard1Servers;
        }
        return 0;
    }

}
