package de.bnder.taskmanager.commands.board;
/*
 * Copyright (C) 2021 Jan Brinkmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.Localizations;
import de.bnder.taskmanager.utils.MessageSender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BoardList {

    public static void getBoardList(Member member, TextChannel textChannel, String prefix) throws IOException {
        final String langCode = Localizations.getGuildLanguage(member.getGuild());
        final String embedTitle = Localizations.getString("board_title", langCode);
        final org.jsoup.Connection.Response res = Main.tmbAPI("board/list/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
        final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
        final int statusCode = res.statusCode();
        if (statusCode == 200) {
            final JsonArray boardArray = jsonObject.get("boards").asArray();
            if (boardArray.size() > 0) {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < boardArray.size(); i++) {
                    final JsonObject board = boardArray.get(i).asObject();
                    final String boardName = board.getString("name", null);
                    final boolean isActive = board.getBoolean("isActiveBoard", false);

                    builder.append("- `").append(boardName).append("`");
                    if (isActive) builder.append(" ✅");
                    builder.append("\n");
                }
                MessageSender.send(embedTitle, "- `default` " + (builder.toString().contains("✅") ? "\n" : "✅\n") + builder.toString(), textChannel, Color.green, langCode);
            } else {
                MessageSender.send(embedTitle, Localizations.getString("no_boards_on_server", langCode, new ArrayList<>(){{
                    add(prefix);
                }}), textChannel, Color.red, langCode);
            }
        } else {
            MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", langCode, new ArrayList<String>() {
                {
                    add(String.valueOf(statusCode));
                }
            }), textChannel, Color.red, langCode);
        }
    }

}
