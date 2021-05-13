package de.bnder.taskmanager.commands;

import de.bnder.taskmanager.main.Command;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Prefix implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws IOException {
        final String langCode = Localizations.getGuildLanguage(event.getGuild());
        final String embedTitle = Localizations.getString("prefix_title", langCode);
        if (event.getMember().isOwner() || event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length == 1) {
                final String prefix = args[0];
                if (prefix.length() == 1) {
<<<<<<< Updated upstream
                    final Connection.Response res = Jsoup.connect(Main.requestURL + "/server/prefix/" + event.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).data("prefix", prefix).postDataCharset("UTF-8").header("user_id", event.getMember().getId()).timeout(de.bnder.taskmanager.utils.Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final Connection.Response res = Main.tmbAPI("server/prefix/" + event.getGuild().getId(), event.getAuthor().getId(), Connection.Method.POST, event.getGuild().getId()).data("prefix", prefix).execute();
>>>>>>> Stashed changes
                    if (res.statusCode() == 200) {
                        MessageSender.send(embedTitle, Localizations.getString("prefix_changed", langCode, new ArrayList<String>(){{
                            add(prefix);
                        }}), event.getMessage(), Color.green, langCode);
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>(){{
                            add("PREFIX-${res.statusCode()}");
                        }}), event.getMessage(), Color.red, langCode);
                    }
                } else {
                    MessageSender.send(embedTitle, Localizations.getString("prefix_only_one_char", langCode), event.getMessage(), Color.red, langCode);
                }
            } else {
                MessageSender.send(embedTitle, Localizations.getString("prefix_no_arg", langCode), event.getMessage(), Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), event.getMessage(), Color.red, langCode);
        }
    }
}
