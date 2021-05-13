package de.bnder.taskmanager.commands.task;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.Settings;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SelfTaskList {

    public static void selfTaskList(Member member, TextChannel textChannel) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("task_message_title", langCode);
<<<<<<< Updated upstream
        final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/task/user/tasks/" + textChannel.getGuild().getId() + "/" + member.getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final org.jsoup.Connection.Response res = Main.tmbAPI("task/user/tasks/" + textChannel.getGuild().getId() + "/" + member.getId(), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
        final Document document = res.parse();
        final String jsonResponse = document.body().text();
        final JsonObject jsonObject = Json.parse(jsonResponse).asObject();
        if (res.statusCode() == 200) {
            final String boardName = jsonObject.getString("board", null);
            JsonArray array = jsonObject.get("todo").asArray();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                String deadline = "";
                if (!jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull()) {
                    deadline = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString();
                }
                String dLine = "";
                if (deadline.length() > 0) {
                    dLine = deadline + " |";
                }
                builder.append("- ").append(task).append(" (" + Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode) + " | ").append(dLine).append(" ").append(taskID).append(")").append("\n");
            }
            //TASKS NOT STARTED
            if (builder.length() > 0) {
                StringBuilder finalBuilder2 = builder;
                MessageSender.send(embedTitle, Localizations.getString("alle_aufgaben_von_nutzer", langCode, new ArrayList<String>() {
                    {
                        add(member.getAsMention());
                        add(boardName);
                        add(finalBuilder2.toString());
                    }
                }), textChannel, Color.orange, langCode);
            }
            builder = new StringBuilder();
            array = jsonObject.get("doing").asArray();
            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                String deadline = "";
                if (!jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull()) {
                    deadline = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString();
                }
                String dLine = "";
                if (deadline.length() > 0) {
                    dLine = deadline + " |";
                }
                builder.append("- ").append(task).append(" (" + Localizations.getString("aufgaben_status_wird_bearbeitet", langCode) + " | ").append(dLine).append(" ").append(taskID).append(")").append("\n");
            }
            //TASKS IN PROGRESS
            if (builder.length() > 0) {
                StringBuilder finalBuilder = builder;
                MessageSender.send(embedTitle, Localizations.getString("alle_aufgaben_von_nutzer", langCode, new ArrayList<String>() {
                    {
                        add(member.getAsMention());
                        add(boardName);
                        add(finalBuilder.toString());
                    }
                }), textChannel, Color.yellow, langCode);
            }
            builder = new StringBuilder();

            //TASKS DONE
            array = jsonObject.get("done").asArray();
            String showDoneTasks = Settings.getUserSettings(member).getString("show_done_tasks", "1");
            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                String deadline = "";
                if (!jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull()) {
                    deadline = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString();
                }
                String dLine = "";
                if (deadline.length() > 0) {
                    dLine = deadline + " |";
                }

                if (showDoneTasks.equals("1")) {
                    builder.append("- ").append(task).append(" (" + Localizations.getString("aufgaben_status_erledigt", langCode) + " | ").append(dLine).append(" ").append(taskID).append(")").append("\n");
                }
            }
            if (showDoneTasks.equals("1") && builder.length() > 0) {
                StringBuilder finalBuilder1 = builder;
                MessageSender.send(embedTitle, Localizations.getString("alle_aufgaben_von_nutzer", langCode, new ArrayList<String>() {
                    {
                        add(member.getAsMention());
                        add(boardName);
                        add(finalBuilder1.toString());
                    }
                }), textChannel, Color.green, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("keine_aufgaben", langCode), textChannel, Color.red, langCode);
        }
    }

}
