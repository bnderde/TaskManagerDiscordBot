package de.bnder.taskmanager.commands;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.bnder.taskmanager.main.Command;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Search implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws IOException {
        final String langCode = Localizations.getGuildLanguage(event.getGuild());
        final String embedTitle = Localizations.getString("search_title", langCode);
        final int taskResultLimit = 10;
        final int groupResultLimit = 10;
        if (args.length > 0) {
            final StringBuilder searchTermBuilder = new StringBuilder();
            final StringBuilder searchTermBuilderRaw = new StringBuilder();
            for (final String searchTermArg : args) {
                //Check if username is in searchtermarg
                Member member = event.getGuild().getMembersByName(searchTermArg, true).size() > 0 ? event.getGuild().getMembersByName(searchTermArg, true).get(0) : null;
                if (member == null) {
                    member = event.getGuild().getMembersByNickname(searchTermArg, true).size() > 0 ? event.getGuild().getMembersByNickname(searchTermArg, true).get(0) : null;
                }
                if (member != null) {
                    searchTermBuilder.append(member.getId()).append(" ");
                } else {
                    searchTermBuilder.append(searchTermArg).append(" ");
                }
                searchTermBuilderRaw.append(searchTermArg).append(" ");
            }
            final String searchTerm = searchTermBuilder.substring(0, searchTermBuilder.length() - 1);
            final String searchTermRaw = searchTermBuilderRaw.substring(0, searchTermBuilderRaw.length() - 1);
            if (searchTermRaw.length() >= 3) {
<<<<<<< Updated upstream
                final Connection.Response res = Jsoup.connect(Main.requestURL + "/global/search/" + event.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).data("search_term", searchTerm).postDataCharset("UTF-8").header("user_id", event.getMember().getId()).timeout(de.bnder.taskmanager.utils.Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                final Connection.Response res = Main.tmbAPI("global/search/" + event.getGuild().getId(), event.getAuthor().getId(), Connection.Method.POST, event.getGuild().getId()).data("search_term", searchTerm).execute();
>>>>>>> Stashed changes
                final JsonObject jsonObject = Json.parse(res.body()).asObject();
                final JsonArray tasksResultArray = jsonObject.get("task_results").asArray();
                final JsonArray groupsResultArray = jsonObject.get("group_results").asArray();
                if (tasksResultArray.size() > 0 || groupsResultArray.size() > 0) {
                    int tasksResultArrayIterations = 0;
                    for (JsonValue array : tasksResultArray) {
                        if (tasksResultArrayIterations < taskResultLimit) {
                            final String taskID = array.asString();
                            final Task task = new Task(taskID, event.getGuild());
                            final EmbedBuilder builder = new EmbedBuilder().setColor(Color.cyan);
                            builder.setTitle(Localizations.getString("search_result_task_title", langCode));
                            builder.setDescription(Localizations.getString("search_result_task_description", langCode));
                            if (task.getStatus() == TaskStatus.TODO) {
                                builder.addField(Localizations.getString("task_info_field_state", langCode), Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode), true);
                            } else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                                builder.addField(Localizations.getString("task_info_field_state", langCode), Localizations.getString("aufgaben_status_wird_bearbeitet", langCode), true);
                            } else if (task.getStatus() == TaskStatus.DONE) {
                                builder.addField(Localizations.getString("task_info_field_state", langCode), Localizations.getString("aufgaben_status_erledigt", langCode), true);
                            }
                            if (task.getDeadline() != null) {
                                builder.addField(Localizations.getString("task_info_field_deadline", langCode), task.getDeadline(), true);
                            }
                            if (task.getType() == TaskType.USER) {
                                builder.addField(Localizations.getString("task_info_field_type", langCode), Localizations.getString("task_info_field_type_user", langCode), true);
                                final String userID = task.getHolder();
                                if (userID != null && event.getGuild().retrieveMemberById(userID).complete() != null) {
                                    builder.addField(Localizations.getString("task_info_field_assigned", langCode), event.getGuild().retrieveMemberById(userID).complete().getUser().getAsTag(), true);
                                }
                            } else if (task.getType() == TaskType.GROUP) {
                                builder.addField(Localizations.getString("task_info_field_type", langCode), Localizations.getString("task_info_field_type_group", langCode), true);
                                builder.addField(Localizations.getString("task_info_field_assigned", langCode), task.getHolder(), true);
                            }
                            builder.addField(Localizations.getString("task_info_field_task", langCode), task.getText(), false);
                            builder.addField(Localizations.getString("task_info_field_id", langCode), taskID, false);
                            event.getChannel().sendMessage(builder.build()).queue();
                            tasksResultArrayIterations++;
                        } else {
                            MessageSender.send(embedTitle, Localizations.getString("search_results_hidden_text", langCode), event.getChannel(), Color.red, langCode);
                            break;
                        }
                    }

                    int groupsResultArrayIterations = 0;
                    for (JsonValue value : groupsResultArray) {
                        if (groupsResultArrayIterations < groupResultLimit) {
                            final String groupID = value.asString();
                            final EmbedBuilder builder = new EmbedBuilder().setColor(Color.cyan);

<<<<<<< Updated upstream
                            final Connection.Response groupInfoRes = Jsoup.connect(Main.requestURL + "/group/info/" + event.getGuild().getId() + "/" + groupID).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", event.getMember().getId()).timeout(de.bnder.taskmanager.utils.Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                            final Connection.Response groupInfoRes = Main.tmbAPI("group/info/" + event.getGuild().getId() + "/" + groupID, event.getMember().getId(), Connection.Method.GET, event.getGuild().getId()).execute();
>>>>>>> Stashed changes
                            final JsonObject groupInfoJsonObject = Json.parse(groupInfoRes.body()).asObject();
                            final String groupName = groupInfoJsonObject.getString("name", null);

                            if (!groupInfoJsonObject.get("notifyChannelID").isNull()) {
                                final String channelID = groupInfoJsonObject.getString("notifyChannelID", null);
                                if (channelID != null && event.getGuild().getTextChannelById(channelID) != null) {
                                    builder.addField(Localizations.getString("search_notify_channel_field", langCode), event.getGuild().getTextChannelById(channelID).getAsMention(), true);
                                }
                            }
                            final JsonArray membersList = groupInfoJsonObject.get("memberList").asArray();
                            if (membersList.size() > 0) {
                                final StringBuilder membersStringBuilder = new StringBuilder();
                                for (JsonValue membersValue : membersList) {
                                    final String userID = membersValue.asObject().get("user_id").asString();
                                    if (event.getGuild().retrieveMemberById(userID).complete() != null) {
                                        membersStringBuilder.append(event.getGuild().retrieveMemberById(userID).complete().getUser().getAsTag()).append(", ");
                                    }
                                }
                                builder.addField(Localizations.getString("search_members_field", langCode), membersStringBuilder.substring(0, membersStringBuilder.length() - 2), false);
                            }

                            builder.setTitle(Localizations.getString("search_result_group_title", langCode));
                            builder.setDescription(Localizations.getString("search_result_group_description", langCode));
                            builder.addField(Localizations.getString("search_group_name_field", langCode), groupName, true);

                            event.getChannel().sendMessage(builder.build()).queue();
                            groupsResultArrayIterations++;
                        } else {
                            MessageSender.send(embedTitle, Localizations.getString("search_results_hidden_text", langCode), event.getChannel(), Color.red, langCode);
                            break;
                        }
                    }
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("no_search_results", langCode, new ArrayList<String>(){{
                        add(searchTermRaw);
                    }}), event.getMessage(), Color.red, langCode);
                }
            }
        }
    }
}
