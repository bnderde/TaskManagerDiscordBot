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
import com.eclipsesource.json.JsonValue;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.bnder.taskmanager.main.Main;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CheckServerMember implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Method" + exchange.getRequestMethod());
        if (exchange.getRequestMethod().equals("MethodPOST")) {
            final String requestBody = inputStreamToString(exchange.getRequestBody());
            final JsonObject jsonObject = Json.parse(requestBody).asObject();
            final JsonArray serverMembers = jsonObject.get("server_members").asArray();
            for (JsonValue serverMember : serverMembers) {
                final String userID = serverMember.asObject().getString("user_id", null);
                final String serverID = serverMember.asObject().getString("server_id", null);
                if (userID != null && serverID != null) {
                    if (Main.jda.getGuildById(serverID).retrieveMemberById(userID).complete() != null) {
                        //TODO:
                        final JsonObject jsonResponse = new JsonObject();
                    }
                }
            }
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