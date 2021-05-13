package de.bnder.taskmanager.commands;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Command;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Connection;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.Base64;

public class Token implements Command {
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws IOException {
        final Guild guild = event.getGuild();
        final String langCode = Localizations.getGuildLanguage(guild);
        final String embedTitle = Localizations.getString("token_message_title", langCode);
        try {
            final PrivateChannel channel = event.getAuthor().openPrivateChannel().complete();
            if (args.length == 1 && args[0].equalsIgnoreCase("new")) {
                final String token = getNewToken(event.getAuthor());
                MessageSender.send(embedTitle, Localizations.getString("token_erhalten", langCode), event.getMessage(), Color.green, langCode);
                channel.sendMessage(token).queue();
                channel.sendMessage(new EmbedBuilder().setImage("http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=" + Base64.getEncoder().encodeToString(("tmb_" + token).getBytes()) + "&qzone=1&margin=0&size=400x400&ecc=L").build()).queue();
                channel.sendMessage(Localizations.getString("token_info", langCode)).queue();
            } else {
                final String token = getToken(event.getAuthor());
                MessageSender.send(embedTitle, Localizations.getString("token_erhalten", langCode), event.getMessage(), Color.green, langCode);
                channel.sendMessage(token).queue();
                channel.sendMessage(new EmbedBuilder().setImage("http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=" + Base64.getEncoder().encodeToString(("tmb_" + token).getBytes()) + "&qzone=1&margin=0&size=400x400&ecc=L").build()).queue();
                channel.sendMessage(Localizations.getString("token_info", langCode)).queue();
            }
        } catch (Exception e){
            MessageSender.send(embedTitle, Localizations.getString("token_konnte_nicht_gesendet_werden", langCode), event.getMessage(), Color.red, langCode);
        }
    }

    private String getNewToken(User user) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/token/create").method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", user.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("user/token/create", user.getId(), org.jsoup.Connection.Method.GET, null).execute();
>>>>>>> Stashed changes
            final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
            if (res.statusCode() == 200) {
                return jsonObject.getString("token", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getToken(User user) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/token").method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", user.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("user/token", user.getId(), org.jsoup.Connection.Method.GET, null).execute();
>>>>>>> Stashed changes
            final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
            if (res.statusCode() == 200) {
                return jsonObject.getString("token", null);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
