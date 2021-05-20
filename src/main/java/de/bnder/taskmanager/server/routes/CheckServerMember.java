package de.bnder.taskmanager.server.routes;
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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.server.ServerResponse;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CheckServerMember implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        if (exchange.getRequestMethod().equals("POST")) {
            //TODO: change key
            if (exchange.getRequestHeaders().get("authorization").get(0).equals("abcd")) {
                final String requestBody = inputStreamToString(exchange.getRequestBody());
                final JsonObject jsonObject = Json.parse(requestBody).asObject();
                if (jsonObject.get("user_id") != null) {
                    final String userID = jsonObject.get("user_id").asString();
                    if (Main.jda.retrieveUserById(userID).complete() != null) {
                        final User user = Main.jda.retrieveUserById(userID).complete();
                        final JsonArray jsonArray = new JsonArray();
                        for (Guild g : user.getMutualGuilds()) {
                            jsonArray.add(g.getId());
                        }
                        ServerResponse.sendResponse(jsonArray.toString(), 200, exchange);
                    } else {
                        ServerResponse.sendResponse("[]", 401, exchange);
                    }
                } else {
                    ServerResponse.sendResponse("Post value user_id not set!", 401, exchange);
                }
            } else {
                ServerResponse.sendResponse("Invalid authorization header!", 401, exchange);
            }
        } else {
            ServerResponse.sendResponse("Invalid Method!", 401, exchange);
        }
    }

    static String inputStreamToString(InputStream inputStream) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textBuilder.toString();
    }
}