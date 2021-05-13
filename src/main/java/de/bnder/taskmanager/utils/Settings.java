package de.bnder.taskmanager.utils;
/*
 * Copyright (C) 2020 Jan Brinkmann
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
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.entities.Member;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Settings {

    public static JsonObject getUserSettings(Member member) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/settings/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.GET).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("user/settings/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.GET, member.getGuild().getId()).execute();
>>>>>>> Stashed changes
            final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
            if (res.statusCode() == 200) {
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

}
