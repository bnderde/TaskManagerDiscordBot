package de.bnder.taskmanager.commands.group;

import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CreateGroup {

    public static void createGroup(Member member, TextChannel textChannel, String[] args) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("group_title", langCode);
        if (PermissionSystem.hasPermission(member, GroupPermission.CREATE_GROUP)) {
            final String groupName = Connection.encodeString(args[1]);
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/create/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).data("group_name", groupName).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
            final int statusCode = res.statusCode();
            if (statusCode == 200) {
                MessageSender.send(embedTitle, Localizations.getString("group_created_successfully", langCode, new ArrayList<String>() {
                    {
                        add(groupName);
                    }
                }), textChannel, Color.green);
            } else if (statusCode == 400) {
                MessageSender.send(embedTitle, Localizations.getString("group_not_created_name_already_exists", langCode, new ArrayList<String>() {
                    {
                        add(groupName);
                    }
                }), textChannel, Color.red);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("group_not_created_unknown_error", langCode, new ArrayList<String>() {
                    {
                        add(groupName);
                        add(String.valueOf(statusCode));
                    }
                }), textChannel, Color.red);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red);
        }
    }

}
