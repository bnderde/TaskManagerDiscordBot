package de.bnder.taskmanager.commands.settings;

import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsSetNotifications {

    public static void set(Member member, TextChannel textChannel, List<TextChannel> mentionedChannels, List<Member> mentionedMembers) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("settings_title", langCode);
        if (PermissionSystem.hasPermission(member, GroupPermission.DEFINE_NOTIFY_CHANNEL)) {
            if (mentionedMembers != null && mentionedMembers.size() > 0) {
                if (mentionedChannels != null && mentionedChannels.size() > 0) {
                    final User user = mentionedMembers.get(0).getUser();
<<<<<<< Updated upstream
                    final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/notify-channel/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.PUT).header("authorization", "TMB " + Main.authorizationToken).header("user_id", user.getId()).data("notify_channel", mentionedChannels.get(0).getId()).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final org.jsoup.Connection.Response res = Main.tmbAPI("user/notify-channel/" + member.getGuild().getId(), user.getId(), org.jsoup.Connection.Method.PUT, member.getGuild().getId()).data("notify_channel", mentionedChannels.get(0).getId()).execute();
>>>>>>> Stashed changes
                    if (res.statusCode() == 200) {
                        MessageSender.send(embedTitle, Localizations.getString("user_connect_channel_connected", langCode, new ArrayList<String>() {{
                            add(user.getAsTag());
                            add(mentionedChannels.get(0).getAsMention());
                        }}), textChannel, Color.green, langCode);
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {
                            {
                                add(String.valueOf(res.statusCode()));
                            }
                        }), textChannel, Color.red, langCode);
                    }
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("notify_mention_one_channel", langCode), textChannel, Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("user_needs_to_be_mentioned", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

}
