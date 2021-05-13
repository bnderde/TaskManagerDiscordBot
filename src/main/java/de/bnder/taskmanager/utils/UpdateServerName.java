package de.bnder.taskmanager.utils;
/*
 * Copyright (C) 2019 Jan Brinkmann
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

import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.entities.Guild;
import org.jsoup.Jsoup;

import java.io.IOException;

public class UpdateServerName {

    public static void update(Guild guild) throws IOException {
        String name = guild.getName();
<<<<<<< Updated upstream
        Jsoup.connect(Main.requestURL + "/server/name/" + guild.getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").data("name", name).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
        Main.tmbAPI("server/name/" + guild.getId(), null, org.jsoup.Connection.Method.POST, null).data("name", name).data("owner_id", guild.retrieveOwner().complete().getId()).execute();
>>>>>>> Stashed changes
    }

}
