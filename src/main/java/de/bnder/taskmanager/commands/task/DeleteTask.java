package de.bnder.taskmanager.commands.task;

import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.TaskPermission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class DeleteTask {

    public static void deleteTask(Member member, TextChannel textChannel, String[] args) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("task_message_title", langCode);
        if (PermissionSystem.hasPermission(member, TaskPermission.DELETE_TASK)) {
            final String taskID = Connection.encodeString(args[1]);
            if (!taskID.equalsIgnoreCase("done")) {
                final de.bnder.taskmanager.utils.Task task = new de.bnder.taskmanager.utils.Task(taskID, textChannel.getGuild()).delete();
                final int statusCode = task.getStatusCode();
                if (statusCode == 200) {
                    MessageSender.send(embedTitle, Localizations.getString("aufgabe_geloescht", langCode, new ArrayList<>() {
                        {
                            add(taskID);
                        }
                    }), textChannel, Color.green, langCode);
                } else if (statusCode == 404) {
                    MessageSender.send(embedTitle, Localizations.getString("keine_aufgabe_mit_id", langCode, new ArrayList<>() {
                        {
                            add(taskID);
                        }
                    }), textChannel, Color.red, langCode);
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {
                        {
                            add(statusCode + " " + task.getResponseMessage());
                        }
                    }), textChannel, Color.red, langCode);
                }
            } else {
<<<<<<< Updated upstream
                final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/server/done-tasks/" + member.getGuild().getId())
                        .method(org.jsoup.Connection.Method.DELETE)
                        .header("authorization", "TMB " + Main.authorizationToken)
                        .header("user_id", member.getId()).timeout(Connection.timeout)
                        .userAgent(Main.userAgent)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .execute();
=======
                final org.jsoup.Connection.Response res = Main.tmbAPI("server/done-tasks/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.DELETE, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
                if (res.statusCode() == 200) {
                    MessageSender.send(embedTitle, Localizations.getString("deleted_done_tasks", langCode), textChannel, Color.green, langCode);
                }
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

}
