package de.bnder.taskmanager.commands;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.bnder.taskmanager.main.Command;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Settings;
import de.bnder.taskmanager.utils.*;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import de.bnder.taskmanager.utils.permissions.PermissionPermission;
import de.bnder.taskmanager.utils.permissions.TaskPermission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Data implements Command {
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws IOException {
        final Guild guild = event.getGuild();
        final String langCode = Localizations.getGuildLanguage(guild);
        MessageSender.send(Localizations.getString("data_title", langCode), Localizations.getString("data_will_be_sent", langCode), event.getMessage(), Color.cyan, langCode);

        //Get every user data
        final File file = new File("userData-" + guild.getId() + "-" + event.getAuthor().getId() + ".yml");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
        }
        final YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.set("language", langCode);
        final String guildId = guild.getId();
        final Member member = guild.retrieveMember(event.getAuthor()).complete();

        //Get Tasks
<<<<<<< Updated upstream
        final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/task/user/tasks/" + guild.getId() + "/" + member.getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final org.jsoup.Connection.Response res = Main.tmbAPI("task/user/tasks/" + guildId + "/" + member.getId(), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
        final String jsonResponse = res.parse().body().text();
        final JsonObject jsonObject = Json.parse(jsonResponse).asObject();
        if (res.statusCode() == 200) {
            JsonArray array = jsonObject.get("todo").asArray();
            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                final String deadline = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString() : "---";
                final String type = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("type").asString();
                final String groupID = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").asString() : "---";

                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "text", task);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "deadline", deadline);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "status", "not edited");
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "type", type);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "group id", groupID);
            }
            array = jsonObject.get("doing").asArray();
            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                final String deadline = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString() : "---";
                final String type = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("taskType").asString();
                final String groupID = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").asString() : "---";

                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "text", task);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "deadline", deadline);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "status", "in progress");
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "type", type);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "group id", groupID);
            }
            array = jsonObject.get("done").asArray();
            for (int i = 0; i < array.size(); i++) {
                final String taskID = array.get(i).asString();
                final String task = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("task").asString();
                final String deadline = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("deadline").asString() : "---";
                final String type = jsonObject.get("allTasks").asObject().get(taskID).asObject().get("type").asString();
                final String groupID = !jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").isNull() ? jsonObject.get("allTasks").asObject().get(taskID).asObject().get("group_id").asString() : "---";

                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "text", task);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "deadline", deadline);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "status", "done");
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "type", type);
                yamlConfiguration.set("guild" + "." + guildId + "." + "tasks" + "." + taskID + "." + "group id", groupID);
            }
        }

        //Get User Settings
        final JsonObject settingsObject = Settings.getUserSettings(member);
        yamlConfiguration.set("guild" + "." + guildId + "." + "settings" + ".direct message", Boolean.valueOf(settingsObject.get("direct_message") != null ? settingsObject.get("direct_message").toString() : "1"));
        yamlConfiguration.set("guild" + "." + guildId + "." + "settings" + ".show done tasks", Boolean.valueOf(settingsObject.get("show_done_tasks") != null ? settingsObject.get("show_done_tasks").toString() : "1"));
        yamlConfiguration.set("guild" + "." + guildId + "." + "settings" + ".notify channel", settingsObject.get("notify_channel") != null && !settingsObject.get("notify_channel").isNull() ? settingsObject.getString("notify_channel", null) : null);

        //Get Users Groups
<<<<<<< Updated upstream
        final org.jsoup.Connection.Response getGroupsRes = Jsoup.connect(Main.requestURL + "/group/list/" + guild.getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        final org.jsoup.Connection.Response getGroupsRes = Main.tmbAPI("group/list/" + guildId, member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
        final JsonObject getGroupsObject = Json.parse(getGroupsRes.parse().body().text()).asObject();
        if (getGroupsRes.statusCode() == 200) {
            JsonArray servers = getGroupsObject.get("groups").asArray();
            if (servers.size() > 0) {
                for (int i = 0; i < servers.size(); i++) {
                    final String groupName = servers.get(i).asString();
<<<<<<< Updated upstream
                    final org.jsoup.Connection.Response getMembersRes = Jsoup.connect(Main.requestURL + "/group/members/" + guild.getId() + "/" + Connection.encodeString(groupName)).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final org.jsoup.Connection.Response getMembersRes = Main.tmbAPI("group/members/" + guildId + "/" + Connection.encodeString(groupName), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
                    final JsonObject getMembersObject = Json.parse(getMembersRes.parse().body().text()).asObject();
                    if (getMembersObject.getInt("status_code", 900) == 200) {
                        final String groupID = getMembersObject.getString("group_id", null);
                        for (JsonValue value : getMembersObject.get("members").asArray()) {
                            final String id = value.asObject().getString("user_id", null);
                            if (id != null) {
                                if (id.equalsIgnoreCase(event.getAuthor().getId())) {
                                    yamlConfiguration.set("guild" + "." + guildId + "." + "group" + "." + groupID + ".name", groupName);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Get User Permissions
        for (TaskPermission permission : TaskPermission.values()) {
            final boolean hasPermission = PermissionSystem.hasPermission(member, permission);
            yamlConfiguration.set("guild" + "." + guildId + "." + "permission" + "." + permission.name(), hasPermission);
        }
        for (GroupPermission permission : GroupPermission.values()) {
            final boolean hasPermission = PermissionSystem.hasPermission(member, permission);
            yamlConfiguration.set("guild" + "." + guildId + "." + "permission" + "." + permission.name(), hasPermission);
        }
        for (PermissionPermission permission : PermissionPermission.values()) {
            final boolean hasPermission = PermissionSystem.hasPermission(member, permission);
            yamlConfiguration.set("guild" + "." + guildId + "." + "permission" + "." + permission.name(), hasPermission);
        }

        yamlConfiguration.save(file);
        event.getAuthor().openPrivateChannel().complete().sendFile(file).queue();
        file.delete();
    }
}
