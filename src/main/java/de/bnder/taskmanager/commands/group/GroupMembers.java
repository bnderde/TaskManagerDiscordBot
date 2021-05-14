package de.bnder.taskmanager.commands.group;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GroupMembers {

    public static void getGroupMembers(Member member, TextChannel textChannel, String[] args) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("group_title", langCode);
        if (PermissionSystem.hasPermission(member, GroupPermission.SHOW_MEMBERS)) {
            final String groupName = Connection.encodeString(args[1]);
            final org.jsoup.Connection.Response res = Main.tmbAPI("group/members/" + member.getGuild().getId() + "/" + groupName, member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
            final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
            final int statusCode = res.statusCode();
            if (statusCode == 200) {
                final StringBuilder builder = new StringBuilder();
                for (JsonValue value : jsonObject.get("members").asArray()) {
                    final String id = value.asObject().getString("user_id", null);
                    if (id != null) {
                        final Member groupMember = member.getGuild().retrieveMemberById(id).complete();
                        if (groupMember != null) {
                            builder.append("- ").append(groupMember.getUser().getAsTag()).append("\n");
                        }
                    }
                }
                MessageSender.send(embedTitle, Localizations.getString("group_members", langCode, new ArrayList<String>() {
                    {
                        add(builder.substring(0, builder.length() - 1));
                    }
                }), textChannel, Color.green, langCode);
            } else if (statusCode == 404) {
                MessageSender.send(embedTitle, Localizations.getString("group_with_name_doesnt_exist", langCode, new ArrayList<String>() {
                    {
                        add(groupName);
                    }
                }), textChannel, Color.red, langCode);
            } else if (statusCode == 400) {
                MessageSender.send(embedTitle, Localizations.getString("group_no_members", langCode), textChannel, Color.red, langCode);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {
                    {
                        add(String.valueOf(statusCode));
                    }
                }), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

}
