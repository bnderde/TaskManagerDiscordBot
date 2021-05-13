package de.bnder.taskmanager.commands.group;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupNotifications {

    public static void setGroupNotifications(Member member, TextChannel textChannel, String[] args, List<TextChannel> mentionedChannels) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("group_title", langCode);
        if (PermissionSystem.hasPermission(member, GroupPermission.DEFINE_NOTIFY_CHANNEL)) {
            if (mentionedChannels != null && mentionedChannels.size() > 0) {
                String groupName = args[1];
                if (serverHasGroup(groupName, textChannel.getGuild())) {
<<<<<<< Updated upstream
                    final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/notify-channel/" + textChannel.getGuild().getId()).method(org.jsoup.Connection.Method.PUT).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).data("group_name", groupName).data("notify_channel", mentionedChannels.get(0).getId()).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final org.jsoup.Connection.Response res = Main.tmbAPI("group/notify-channel/" + textChannel.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.PUT, member.getGuild().getId()).data("group_name", groupName).data("notify_channel", mentionedChannels.get(0).getId()).execute();
>>>>>>> Stashed changes
                    if (res.statusCode() == 200) {
                        MessageSender.send(embedTitle, Localizations.getString("group_connect_channel_connected", langCode, new ArrayList<String>() {{
                            add(groupName);
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
                    MessageSender.send(embedTitle, Localizations.getString("group_with_name_doesnt_exist", langCode, new ArrayList<String>() {
                        {
                            add(groupName);
                        }
                    }), textChannel, Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("notify_mention_one_channel", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

    public static boolean serverHasGroup(String group, Guild guild) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/list/" + guild.getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("group/list/" + guild.getId(), null, org.jsoup.Connection.Method.GET, guild.getId()).execute();
>>>>>>> Stashed changes
            final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
            final int statusCode = res.statusCode();
            if (statusCode == 200) {
                final JsonArray groups = jsonObject.get("groups").asArray();
                if (groups.size() > 0) {
                    for (int i = 0; i < groups.size(); i++) {
                        final String groupName = groups.get(i).asString();
                        if (group.equalsIgnoreCase(groupName)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
