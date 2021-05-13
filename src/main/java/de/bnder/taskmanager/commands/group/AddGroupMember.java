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
import java.util.List;

public class AddGroupMember {

    public static void addGroupMember(Member member, TextChannel textChannel, String[] args, List<Member> mentionedMembers) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("group_title", langCode);
        if (PermissionSystem.hasPermission(member, GroupPermission.ADD_MEMBERS)) {
            if (args.length >= 3) {
                if (mentionedMembers != null && mentionedMembers.size() > 0) {
                    final String groupName = Connection.encodeString(args[1 + mentionedMembers.size()]);
                    for (Member mentionedMember : mentionedMembers) {
<<<<<<< Updated upstream
                        final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/add-member/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.PUT).header("authorization", "TMB " + Main.authorizationToken).header("user_id", mentionedMember.getId()).data("group_name", groupName).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                        final org.jsoup.Connection.Response res = Main.tmbAPI("group/add-member/" + member.getGuild().getId(), mentionedMember.getId(), org.jsoup.Connection.Method.PUT, member.getGuild().getId()).data("group_name", groupName).execute();
>>>>>>> Stashed changes
                        final int statusCode = res.statusCode();
                        if (statusCode == 200) {
                            MessageSender.send(embedTitle, Localizations.getString("user_added_to_group", langCode, new ArrayList<String>() {
                                {
                                    add(mentionedMember.getUser().getName());
                                    add(groupName);
                                }
                            }), textChannel, Color.green, langCode);
                        } else if (statusCode == 404) {
                            MessageSender.send(embedTitle, Localizations.getString("group_with_name_doesnt_exist", langCode, new ArrayList<String>() {
                                {
                                    add(groupName);
                                }
                            }), textChannel, Color.red, langCode);
                            return;
                        } else if (statusCode == 400) {
                            MessageSender.send(embedTitle, Localizations.getString("user_already_in_group", langCode), textChannel, Color.red, langCode);
                        } else {
                            MessageSender.send(embedTitle, Localizations.getString("user_added_to_group_unknown_error", langCode, new ArrayList<String>() {
                                {
                                    add(String.valueOf(statusCode));
                                }
                            }), textChannel, Color.red, langCode);
                            return;
                        }
                    }
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("user_needs_to_be_mentioned", langCode), textChannel, Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("group_name_needs_to_be_given", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

}
