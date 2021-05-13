package de.bnder.taskmanager.commands.settings;

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
import java.util.List;

public class SettingsNotifyChannel {

    public static void set(Member member, TextChannel textChannel, String[] args, List<TextChannel> mentionedChannels) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("settings_title", langCode);
        final String arg0 = args[0].replaceAll("-", "").replaceAll("_", "");
        if (arg0.equalsIgnoreCase("notifychannel")) {
            if (mentionedChannels != null && mentionedChannels.size() == 1) {
                final TextChannel channel = mentionedChannels.get(0);
<<<<<<< Updated upstream
                final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/settings/value/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).data("type", "notify_channel").data("value", channel.getId()).postDataCharset("UTF-8").ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                final org.jsoup.Connection.Response res = Main.tmbAPI("user/settings/value/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("type", "notify_channel").data("value", channel.getId()).execute();
>>>>>>> Stashed changes
                if (res.statusCode() == 200) {
                    if (!de.bnder.taskmanager.utils.Settings.getUserSettings(member).getString("direct_message", "1").equals("0")) {
                        MessageSender.send(embedTitle, Localizations.getString("notify_channel_set_but_dms_are_enabled", langCode, new ArrayList<String>() {{
                            add(channel.getAsMention());
                        }}), textChannel, Color.green, langCode);
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("notify_channel_set", langCode, new ArrayList<String>() {{
                            add(channel.getAsMention());
                        }}), textChannel, Color.green, langCode);
                    }
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {{
                        add("SETTINGS-3-" + res.statusCode());
                    }}), textChannel, Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("notify_mention_one_channel", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("settings_invalid_arg", langCode), textChannel, Color.red, langCode);
        }
    }

}
