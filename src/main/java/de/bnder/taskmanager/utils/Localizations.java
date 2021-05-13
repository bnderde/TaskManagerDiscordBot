package de.bnder.taskmanager.utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.entities.Guild;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Localizations {

    private static JsonObject toParse = null;

    public static String getString(String path, String languageCode) {
        if (toParse == null) {
            toParse = Json.parse(localizationsJSONSource()).asObject();
        }
        if (toParse.get(path) != null) {
            return toParse.get(path).asObject().getString(languageCode, path);
        }
        return path;
    }

    public static String getString(String path, String languageCode, ArrayList<String> args) {
        if (toParse == null) {
            toParse = Json.parse(localizationsJSONSource()).asObject();
        }
        if (toParse.get(path) != null) {
            final String localizationsText = toParse.get(path).asObject().getString(languageCode, "null");
            int argCount = 0;
            StringBuilder messageBuilder = new StringBuilder();

            for (String paragraph : localizationsText.split("\n")) {
                for (String word : paragraph.split(" ")) {
                    if (word.contains("$")) {
                        if (args.size() >= argCount + 1) {
                            messageBuilder.append(word.replace("$", args.get(argCount))).append(" ");
                            argCount++;
                        } else {
                            messageBuilder.append(word.replace("$", " ")).append(" ");
                        }
                    } else {
                        messageBuilder.append(word).append(" ");
                    }
                }
                if (paragraph.contains(" ")){
                    messageBuilder = new StringBuilder().append(messageBuilder.substring(0, messageBuilder.length() - 1));
                }
                messageBuilder.append("\n");
            }
            return messageBuilder.substring(0, messageBuilder.length() - 1);
        }
        return path;
    }

    public static String getGuildLanguage(Guild guild) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/server/language/" + guild.getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("server/language/" + guild.getId(), null, org.jsoup.Connection.Method.GET, null).execute();
>>>>>>> Stashed changes
            if (res.statusCode() == 200) {
                return Json.parse(res.parse().body().text()).asObject().getString("language", "en");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "en";
    }

    private static String localizationsJSONSource() {
        try {
            final BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream("teammanagerbotLocalizations.json")));
            final StringBuilder sb = new StringBuilder();
            String line = buf.readLine();
            while (line != null) {
                sb.append(line);
                line = buf.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
