package de.bnder.taskmanager.commands.task;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.bnder.taskmanager.commands.group.GroupNotifications;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.PermissionSystem;
import de.bnder.taskmanager.utils.permissions.TaskPermission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddTask {

    public static void addTask(String commandMessage, Member member, List<Member> mentionedMembers, TextChannel textChannel, String[] args) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("task_message_title", langCode);
        if (PermissionSystem.hasPermission(member, TaskPermission.CREATE_TASK)) {
            if (mentionedMembers != null && mentionedMembers.size() > 0) {
                final String task = getTaskFromArgs(1 + mentionedMembers.size(), commandMessage, true);
                for (Member mentionedMember : mentionedMembers) {
                    final de.bnder.taskmanager.utils.Task taskObject = new de.bnder.taskmanager.utils.Task(member.getGuild(), task, null, mentionedMember);
                    if (taskObject.getStatusCode() == 200) {
                        String newLanguageSuggestionAppend = taskObject.newLanguageSuggestion() != null ? Localizations.getString("task_new_language_suggestion_text", taskObject.newLanguageSuggestion(), new ArrayList<>() {{
                            add(String.valueOf(commandMessage.charAt(0)));
                            add(taskObject.newLanguageSuggestion());
                        }}) : "";
                        sendTaskMessage(mentionedMember, member, taskObject.getId(), langCode, task);
                        MessageSender.send(embedTitle + " - " + taskObject.getId(), Localizations.getString("aufgabe_erstellt", langCode, new ArrayList<>() {
                            {
                                add(mentionedMember.getUser().getName());
                                add(taskObject.getActiveBoardName());
                            }
                        }) + newLanguageSuggestionAppend, textChannel, Color.green, langCode);
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("aufgabe_erstellt_unbekannter_fehler", langCode, new ArrayList<>() {
                            {
                                add(taskObject.getStatusCode() + " " + taskObject.getResponseMessage());
                            }
                        }), textChannel, Color.red, langCode);
                    }
                }
            } else if (GroupNotifications.serverHasGroup(args[1], member.getGuild())) {
                final String groupName = Connection.encodeString(args[1]);
                final String task = getTaskFromArgs(3, commandMessage, false);
                final de.bnder.taskmanager.utils.Task taskObject = new de.bnder.taskmanager.utils.Task(textChannel.getGuild(), task, null, groupName, member);
                final int statusCode = taskObject.getStatusCode();
                if (statusCode == 200) {
<<<<<<< Updated upstream
                    final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/group/members/" + textChannel.getGuild().getId() + "/" + groupName).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final org.jsoup.Connection.Response res = Main.tmbAPI("group/members/" + textChannel.getGuild().getId() + "/" + groupName, member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
                    final int getGroupMembersStatusCode = res.statusCode();
                    if (getGroupMembersStatusCode == 200) {
                        final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
                        int usersWhoReceivedTheTaskAmount = 0;
                        for (JsonValue value : jsonObject.get("members").asArray()) {
                            final String id = value.asObject().getString("user_id", null);
                            if (id != null) {
                                final Member groupMember = member.getGuild().retrieveMemberById(id).complete();
                                if (groupMember != null) {
                                    usersWhoReceivedTheTaskAmount++;
                                    sendTaskMessage(groupMember, member, taskObject.getId(), langCode, task);
                                }
                            }
                        }
                        int finalUsersWhoReceivedTheTaskAmount = usersWhoReceivedTheTaskAmount;
                        MessageSender.send(embedTitle + " - " + taskObject.getId(), Localizations.getString("aufgabe_an_x_mitglieder_gesendet", langCode, new ArrayList<>() {
                            {
                                add(String.valueOf(finalUsersWhoReceivedTheTaskAmount));
                            }
                        }), textChannel, Color.green, langCode);
                    } else if (getGroupMembersStatusCode == 404) {
                        MessageSender.send(embedTitle + " - " + taskObject.getId(), Localizations.getString("aufgabe_an_x_mitglieder_gesendet", langCode, new ArrayList<>() {
                            {
                                add("0");
                            }
                        }), textChannel, Color.green, langCode);
                    }
                } else if (statusCode == 404) {
                    MessageSender.send(embedTitle, Localizations.getString("keine_gruppen_auf_server", langCode, new ArrayList<>() {
                        {
                            add(groupName);
                        }
                    }), textChannel, Color.red, langCode);
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("aufgabe_erstellt_unbekannter_fehler", langCode, new ArrayList<>() {
                        {
                            add(taskObject.getStatusCode() + " " + taskObject.getResponseMessage());
                        }
                    }), textChannel, Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("aufgabe_erstellen_fehlende_argumente", langCode), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), textChannel, Color.red, langCode);
        }
    }

    private static void sendTaskMessage(Member member, Member author, String task_id, String langCode, String task) {
        final JsonObject settings = de.bnder.taskmanager.utils.Settings.getUserSettings(member);
        if (settings.getString("direct_message", "1").equalsIgnoreCase("1")) {
            try {
                final PrivateChannel channel = member.getUser().openPrivateChannel().complete();
                channel.sendMessage(Localizations.getString("aufgabe_erhalten", langCode, new ArrayList<>() {
                    {
                        add(author.getUser().getAsTag());
                        add(task_id);
                    }
                })).queue();
                try {
                    channel.sendMessage(URLDecoder.decode(task, StandardCharsets.UTF_8.toString())).queue();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (Exception ignored) {}
        } else if (!settings.get("notify_channel").isNull()) {
            final TextChannel channel = author.getGuild().getTextChannelById(settings.getString("notify_channel", ""));
            if (channel != null) {
                channel.sendMessage(member.getAsMention() + Localizations.getString("aufgabe_erhalten", langCode, new ArrayList<>() {
                    {
                        add(author.getUser().getAsTag());
                        add(task_id);
                    }
                })).queue();
                try {
                    channel.sendMessage(URLDecoder.decode(task, StandardCharsets.UTF_8.toString())).queue();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getTaskFromArgs(int startIndex, String messageText, boolean mentionedUsers) {
        int beginIndex = startIndex;
        final StringBuilder taskBuilder = new StringBuilder();
        final String[] args = messageText.split(" ");
        if (mentionedUsers) beginIndex++;
        for (int i = beginIndex; i < args.length; i++) {
            taskBuilder.append(args[i]).append(" ");
        }
        if (taskBuilder.length() > 0) {
            taskBuilder.subSequence(0, taskBuilder.length() - 2);
        }
        return taskBuilder.toString();
    }

}
