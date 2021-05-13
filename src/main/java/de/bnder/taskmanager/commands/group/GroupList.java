package de.bnder.taskmanager.commands.group;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GroupList {

    public static void getGroupList(Member member, TextChannel textChannel) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("group_title", langCode);
<<<<<<< Updated upstream
        final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/list/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final org.jsoup.Connection.Response res = Main.tmbAPI("group/list/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
        final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
        final int statusCode = res.statusCode();
        if (statusCode == 200) {
            final JsonArray servers = jsonObject.get("groups").asArray();
            if (servers.size() > 0) {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < servers.size(); i++) {
                    final String serverName = servers.get(i).asString();
                    builder.append(serverName).append("\n");
                }
                MessageSender.send(embedTitle, builder.toString(), textChannel, Color.green, langCode);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("keine_gruppen_auf_server", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {
                {
                    add(String.valueOf(statusCode));
                }
            }), textChannel, Color.red, langCode);
        }
    }

}
