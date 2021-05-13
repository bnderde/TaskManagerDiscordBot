package de.bnder.taskmanager.commands.settings;

import com.eclipsesource.json.Json;
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

public class SettingsDirectmessage {

    public static void set(Member member, TextChannel textChannel) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("settings_title", langCode);
<<<<<<< Updated upstream
        final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/settings/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).data("type", "direct_message").postDataCharset("UTF-8").ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final org.jsoup.Connection.Response res = Main.tmbAPI("user/settings/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("type", "direct_message").execute();
>>>>>>> Stashed changes
        final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
        if (res.statusCode() == 200) {
            final boolean newValue = jsonObject.getBoolean("newValue", false);
            if (newValue) {
                MessageSender.send(embedTitle, Localizations.getString("settings_dm_enabled", langCode), textChannel, Color.green, langCode);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("settings_dm_disabled", langCode), textChannel, Color.green, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {{
                add("SETTINGS-1.1-" + res.statusCode());
            }}), textChannel, Color.red, langCode);
        }
    }

}
