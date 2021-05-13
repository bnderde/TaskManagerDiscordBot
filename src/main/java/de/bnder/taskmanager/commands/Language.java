package de.bnder.taskmanager.commands;

import de.bnder.taskmanager.main.Command;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Language implements Command {

    public static ArrayList<String> validLangCodes = new ArrayList<String>(){{
        add("de");
        add("en");
        add("bg");
        add("fr");
        add("ru");
        add("pl");
        add("tr");
    }};

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws IOException {
        final Guild guild = event.getGuild();
        if (args.length == 0) {
            final String langCode = Localizations.getGuildLanguage(guild);
            final String embedTitle = Localizations.getString("language_message_title", langCode);
            MessageSender.send(embedTitle, Localizations.getString("language_message_invalid_params", langCode), event.getMessage(), Color.red, langCode);
        } else if (args.length == 1) {
            final String language = args[0].toLowerCase();
            if (validLangCodes.contains(language)) {
                if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
<<<<<<< Updated upstream
                    final Connection.Response res = Jsoup.connect(Main.requestURL + "/server/language/" + event.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).data("language", language).postDataCharset("UTF-8").header("user_id", event.getMember().getId()).timeout(de.bnder.taskmanager.utils.Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                    final Connection.Response res = Main.tmbAPI("server/language/" + event.getGuild().getId(), event.getAuthor().getId(), Connection.Method.POST, event.getGuild().getId()).data("language", language).execute();
>>>>>>> Stashed changes
                    final String embedTitle = Localizations.getString("language_message_title", language);
                    if (res.statusCode() == 200) {
                        MessageSender.send(embedTitle, Localizations.getString("sprache_geaendert", language), event.getMessage(), Color.green, language);
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", language), event.getMessage(), Color.red, language);
                    }
                } else {
                    final String langCode = Localizations.getGuildLanguage(event.getGuild());
                    final String embedTitle = Localizations.getString("language_message_title", langCode);
                    MessageSender.send(embedTitle, Localizations.getString("need_to_be_serveradmin_or_have_admin_permissions", langCode), event.getMessage(), Color.red, langCode);
                }
            } else {
                final String langCode = Localizations.getGuildLanguage(event.getGuild());
                final String embedTitle = Localizations.getString("language_message_title", langCode);
                MessageSender.send(embedTitle, Localizations.getString("language_message_invalid_params", langCode), event.getMessage(), Color.red, langCode);
            }
        }
    }
}
