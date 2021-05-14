package de.bnder.taskmanager.utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

public class Task {

    String id = null;
    String text = null;
    String deadline = null;
    TaskType type = null;
    TaskStatus status = TaskStatus.TODO;
    String holder = null;
    int statusCode = -1;
    boolean exists = false;
    final Guild guild;
    String newLanguageSuggestion = null;
    String notifyChannelMessageID = null;
    String responseMessage = null;
    String activeBoardName = null;

    public Task(String taskID, Guild guild) {
        this.id = taskID;
        this.guild = guild;

        try {
            JsonObject jsonObject = null;
            Response res;
            //Try user task
            res = Main.tmbAPI("task/user/info/" + guild.getId() + "/" + taskID, null, Method.GET, null).execute();
            setStatusCode(res.statusCode());
            setResponseMessage(res.body());
            if (getStatusCode() == 200) {
                this.type = TaskType.USER;
                final Document a = res.parse();
                jsonObject = Json.parse(a.body().text()).asObject();
                notifyChannelMessageID = (jsonObject.get("notify_channel_message_id") != null && !jsonObject.get("notify_channel_message_id").isNull()) ? jsonObject.getString("notify_channel_message_id", null) : null;
            } else if (getStatusCode() == 404) {
                //Try group task
                res = Main.tmbAPI("task/group/info/" + guild.getId() + "/" + taskID, null, Method.GET, null).execute();
                setStatusCode(res.statusCode());
                setResponseMessage(res.body());
                if (getStatusCode() == 200) {
                    this.type = TaskType.GROUP;
                    final Document a = res.parse();
                    jsonObject = Json.parse(a.body().text()).asObject();
                    notifyChannelMessageID = (jsonObject.get("notify_channel_message_id") != null && !jsonObject.get("notify_channel_message_id").isNull()) ? jsonObject.getString("notify_channel_message_id", null) : null;
                } else {
                    this.exists = false;
                    return;
                }
            }

            if (getStatusCode() == 200) {
                this.exists = true;
                final int taskStatusInt = Objects.requireNonNull(jsonObject).getInt("status", 0);
                if (taskStatusInt >= 0) {
                    this.status = TaskStatus.values()[taskStatusInt];
                }
                this.deadline = jsonObject.get("deadline").toString();
                this.holder = this.type == TaskType.USER ? jsonObject.getString("user_id", null) : jsonObject.getString("group_name", null);
                this.text = jsonObject.getString("text", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Task(String taskID, Guild guild, String text, String deadline, TaskType type, TaskStatus status, String holder) {
        this.id = taskID;
        this.guild = guild;
        this.text = text;
        this.deadline = deadline;
        this.type = type;
        this.status = status;
        this.holder = holder;
        this.exists = true;
        this.statusCode = 200;
    }

    public Task(Guild guild, String text, String deadline, Member member) {
        this.guild = guild;
        this.text = text;
        this.deadline = deadline;
        this.type = TaskType.USER;
        this.holder = member.getId();
        try {
            final Document a = Main.tmbAPI("task/user/" + guild.getId(), null, Method.POST, null).data("task_text", text).data("deadline", deadline != null ? deadline : "").post();
            final String jsonResponse = a.body().text();
            final JsonObject jsonObject = Json.parse(jsonResponse).asObject();
            setStatusCode(200);
            setResponseMessage(a.body().text());
            this.id = jsonObject.getString("id", null);
            this.activeBoardName = jsonObject.getString("board", null);
            this.newLanguageSuggestion = jsonObject.get("new_language_suggestion").isNull() ? null : jsonObject.getString("new_language_suggestion", null);


            try {
                //Send task into group notify channel
                final org.jsoup.Connection.Response getNotifyChannelRes = Main.tmbAPI("user/notify-channel/" + guild.getId(), holder, Method.GET, guild.getId()).execute();
                if (getNotifyChannelRes.statusCode() == 200) {
                    final JsonObject notifyChannelObject = Json.parse(getNotifyChannelRes.body()).asObject();
                    if (notifyChannelObject.get("channel") != null && !notifyChannelObject.get("channel").isNull()) {
                        final String channel = notifyChannelObject.getString("channel", null);
                        if (guild.getTextChannelById(channel) != null) {
                            final String langCode = Localizations.getGuildLanguage(guild);
                            EmbedBuilder builder = new EmbedBuilder().setColor(Color.cyan);
                            builder.addField(Localizations.getString("task_info_field_task", langCode), text, false);
                            builder.addField(Localizations.getString("task_info_field_type_user", langCode), member.getUser().getAsTag(), true);
                            builder.addField(Localizations.getString("task_info_field_deadline", langCode), (deadline != null) ? deadline : "---", true);
                            builder.addField(Localizations.getString("task_info_field_id", langCode), id, true);
                            builder.addField(Localizations.getString("task_info_field_state", langCode), Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode), true);
                            final Message message = guild.getTextChannelById(channel).sendMessage(builder.build()).complete();
                            Main.tmbAPI("task/user/set-notify-channel-message-id/" + guild.getId() + "/" + this.id, holder, Method.POST, guild.getId()).data("notify_channel_message_id", message.getId()).execute();
                            message.addReaction("↩️").queue();
                            message.addReaction("⏭️").queue();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SocketTimeoutException e) {
            setStatusCode(504);
            e.printStackTrace();
        } catch (Exception e) {
            setStatusCode(-1);
            e.printStackTrace();
        }
    }

    public Task(Guild guild, String text, String deadline, String holder, Member commandProcessor) {
        this.guild = guild;
        this.text = text;
        this.deadline = deadline;
        this.type = TaskType.GROUP;
        this.holder = holder;
        try {
            final Document a = Main.tmbAPI("task/group/" + guild.getId() + "/" + holder, commandProcessor.getId(), Method.POST, guild.getId()).data("task_text", text).data("deadline", deadline != null ? deadline : "").post();
            final String jsonResponse = a.body().text();
            final JsonObject jsonObject = Json.parse(jsonResponse).asObject();
            setStatusCode(200);
            setResponseMessage(a.body().text());
            this.id = jsonObject.getString("id", null);


            //Send task into group notify channel
            final org.jsoup.Connection.Response getNotifyChannelRes = Main.tmbAPI("group/notify-channel/" + guild.getId() + "/" + holder, null, Method.GET, guild.getId()).execute();
            if (getNotifyChannelRes.statusCode() == 200) {
                final JsonObject notifyChannelObject = Json.parse(getNotifyChannelRes.parse().body().text()).asObject();
                if (notifyChannelObject.get("channel") != null && !notifyChannelObject.get("channel").isNull()) {
                    final String channel = notifyChannelObject.getString("channel", null);
                    if (guild.getTextChannelById(channel) != null) {
                        final String langCode = Localizations.getGuildLanguage(guild);
                        EmbedBuilder builder = new EmbedBuilder().setColor(Color.cyan);
                        builder.addField(Localizations.getString("task_info_field_task", langCode), text, false);
                        builder.addField(Localizations.getString("task_info_field_type_group", langCode), holder, true);
                        builder.addField(Localizations.getString("task_info_field_deadline", langCode), (deadline != null) ? deadline : "---", true);
                        builder.addField(Localizations.getString("task_info_field_id", langCode), id, true);
                        builder.addField(Localizations.getString("task_info_field_state", langCode), Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode), true);
                        final Message message = guild.getTextChannelById(channel).sendMessage(builder.build()).complete();
                        Main.tmbAPI("task/group/set-notify-channel-message-id/" + guild.getId() + "/" + this.id, null, Method.POST, null).data("notify_channel_message_id", message.getId()).execute();
                        message.addReaction("↩️").queue();
                        message.addReaction("⏭️").queue();
                    }
                }
            }
        } catch (Exception e) {
            setStatusCode(-1);
            e.printStackTrace();
        }
    }

    void setResponseMessage(String jsoupResponse) {
        final JsonObject object = Json.parse(jsoupResponse).asObject();
        if (object.get("message") != null && !object.get("message").isNull()) {
            this.responseMessage = object.getString("message", null);
        }
    }

    public String newLanguageSuggestion() {
        return newLanguageSuggestion;
    }

    public String getNotifyChannelMessageID() {
        if (this.notifyChannelMessageID == null) {
            try {
                final Response res = Main.tmbAPI("task/" + ((this.type == TaskType.GROUP) ? "group" : "user") + "/info/" + this.guild.getId() + "/" + this.id, null, Method.GET, null).execute();
                if (res.statusCode() == 200) {
                    this.type = TaskType.GROUP;
                    final Document a = res.parse();
                    final JsonObject jsonObject = Json.parse(a.body().text()).asObject();
                    this.notifyChannelMessageID = (jsonObject.get("notify_channel_message_id") != null && !jsonObject.get("notify_channel_message_id").isNull()) ? jsonObject.getString("notify_channel_message_id", null) : null;
                    return (jsonObject.get("notify_channel_message_id") != null && !jsonObject.get("notify_channel_message_id").isNull()) ? jsonObject.getString("notify_channel_message_id", null) : null;
                }
            } catch (Exception e) {
                setStatusCode(-2);
                e.printStackTrace();
            }
            return null;
        }
        return notifyChannelMessageID;
    }

    public void setText(String text) {
        try {
            Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/edit/" + this.guild.getId() + "/" + this.id, null, Method.POST, null).data("task_text", text).execute();
            setStatusCode(res.statusCode());
            setResponseMessage(res.body());
            if (getStatusCode() == 200) {
                this.text = text;

                //Update in notify channel
                final String langCode = Localizations.getGuildLanguage(guild);
                updateNotifyChannel(this.guild, Localizations.getString("task_info_field_task", langCode), this.text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Task setDeadline(String deadline) {
        try {
            final Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/set-deadline/" + guild.getId() + "/" + this.id, null, Method.POST, null).data("deadline", deadline).execute();
            setStatusCode(res.statusCode());
            setResponseMessage(res.body());
            if (getStatusCode() == 200) {
                this.deadline = deadline;

                //Update in notify channel
                final String langCode = Localizations.getGuildLanguage(guild);
                updateNotifyChannel(this.guild, Localizations.getString("task_info_field_deadline", langCode), this.deadline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getNotifyChannelID(Guild guild) {
        try {
            final org.jsoup.Connection.Response getNotifyChannelRes = Main.tmbAPI(((this.type == TaskType.GROUP) ? "group" : "user") + "/notify-channel/" + guild.getId() + "/" + ((this.type == TaskType.GROUP) ? holder : ""), (this.type == TaskType.GROUP) ? null : holder, Method.GET, guild.getId()).execute();
            if (getNotifyChannelRes.statusCode() == 200) {
                final JsonObject notifyChannelObject = Json.parse(getNotifyChannelRes.parse().body().text()).asObject();
                if (notifyChannelObject.get("channel") != null) {
                    final String channel = notifyChannelObject.getString("channel", null);
                    return channel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateNotifyChannel(Guild guild, String valueTitle, String newValue) {
        final String messageID = getNotifyChannelMessageID();
        if (messageID != null) {
            final String channelID = getNotifyChannelID(guild);
            if (channelID != null && guild.getTextChannelById(channelID) != null) {
                final Message message = guild.getTextChannelById(channelID).retrieveMessageById(messageID).complete();
                if (message.getAuthor().isBot()) {
                    EmbedBuilder newEmbed = new EmbedBuilder();
                    for (MessageEmbed embed : message.getEmbeds()) {
                        newEmbed.setColor(embed.getColor());
                        newEmbed.setTitle(embed.getTitle());
                        for (MessageEmbed.Field field : embed.getFields()) {
                            if (!field.getName().equalsIgnoreCase(valueTitle)) {
                                newEmbed.addField(field.getName(), field.getValue(), field.isInline());
                            } else {
                                newEmbed.addField(field.getName(), newValue, field.isInline());
                            }
                        }
                    }
                    message.editMessage(newEmbed.build()).queue();
                }
            }
        }
    }

    public Task delete() {
        try {
            final Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/" + this.guild.getId() + "/" + this.id, null, Method.DELETE, null).execute();
            setStatusCode(res.statusCode());
            setResponseMessage(res.body());
            this.exists = false;

            //Delete message channel
            final String messageID = getNotifyChannelMessageID();
            if (messageID != null) {
                final String channelID = getNotifyChannelID(guild);
                if (channelID != null && guild.getTextChannelById(channelID) != null) {
                    final Message message = guild.getTextChannelById(channelID).retrieveMessageById(messageID).complete();
                    message.delete().queue();
                }
            }
            return this;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Task setStatus(TaskStatus status, Member member) {
        try {
            int number = 0;
            switch (status) {
                case DONE:
                    number = 2;
                    break;
                case IN_PROGRESS:
                    number = 1;
                    break;
            }
            final Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/set-status/" + guild.getId() + "/" + this.id + "/" + number, member.getId(), Method.PUT, member.getGuild().getId()).execute();
            setStatusCode(res.statusCode());
            setResponseMessage(res.body());
            if (getStatusCode() == 200) {
                this.status = status;

                //Update in notify channel
                final String langCode = Localizations.getGuildLanguage(guild);
                String newStatus = Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode);
                if (status == TaskStatus.IN_PROGRESS)
                    newStatus = Localizations.getString("aufgaben_status_wird_bearbeitet", langCode);
                else if (status == TaskStatus.DONE)
                    newStatus = Localizations.getString("aufgaben_status_erledigt", langCode);
                updateNotifyChannel(this.guild, Localizations.getString("task_info_field_state", langCode), newStatus);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public Task proceed(Member member) {
        try {
            final Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/update/" + guild.getId() + "/" + this.id, member.getId(), Method.PUT, member.getGuild().getId()).execute();
            processResponseProceedUndoCommand(res);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    private void processResponseProceedUndoCommand(Response res) throws IOException {
        setStatusCode(res.statusCode());
        setResponseMessage(res.body());
        if (getStatusCode() == 200) {
            final Document document = res.parse();
            final String jsonResponse = document.body().text();
            final JsonObject jsonObject = Json.parse(jsonResponse).asObject();
            this.status = TaskStatus.values()[jsonObject.getInt("status", 0)];

            //Update in notify channel
            final String langCode = Localizations.getGuildLanguage(guild);
            String newStatus = Localizations.getString("aufgaben_status_nicht_bearbeitet", langCode);
            if (status == TaskStatus.IN_PROGRESS)
                newStatus = Localizations.getString("aufgaben_status_wird_bearbeitet", langCode);
            else if (status == TaskStatus.DONE)
                newStatus = Localizations.getString("aufgaben_status_erledigt", langCode);
            updateNotifyChannel(this.guild, Localizations.getString("task_info_field_state", langCode), newStatus);
        }
    }

    public Task undo(Member member) {
        try {
            final Response res = Main.tmbAPI("task/" + (this.type == TaskType.USER ? "user" : "group") + "/undo/" + guild.getId() + "/" + this.id, member.getId(), Method.PUT, member.getGuild().getId()).execute();
            processResponseProceedUndoCommand(res);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getHolder() {
        return holder;
    }

    public boolean exists() {
        return exists;
    }

    public String getText() {
        return text;
    }

    public TaskType getType() {
        return type;
    }

    public String getDeadline() {
        return deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getActiveBoardName() {
        return activeBoardName;
    }
}

