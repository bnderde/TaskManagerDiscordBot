package de.bnder.taskmanager.commands.settings;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsShowDoneTasks {

    public static void set(Member member, TextChannel textChannel) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("settings_title", langCode);
        final org.jsoup.Connection.Response res = Main.tmbAPI("user/settings/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("type", "show_done_tasks").execute();
        final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
        if (res.statusCode() == 200) {
            final boolean newValue = Boolean.parseBoolean(jsonObject.getString("newValue", "0"));
            if (newValue) {
                MessageSender.send(embedTitle, Localizations.getString("settings_show_done_tasks_enabled", langCode), textChannel, Color.green, langCode);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("settings_show_done_tasks_disabled", langCode), textChannel, Color.green, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {{
                add("SETTINGS-2.1-" + res.statusCode());
            }}), textChannel, Color.red, langCode);
        }
    }

}
