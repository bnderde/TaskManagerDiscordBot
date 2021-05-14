package de.bnder.taskmanager.listeners;
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

import de.bnder.taskmanager.botlists.UpdateLists;
import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildLeave extends ListenerAdapter {

    public void onGuildLeave(GuildLeaveEvent event) {
        try {
            Main.tmbAPI("server/delete/" + event.getGuild().getId(), null, org.jsoup.Connection.Method.POST, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UpdateLists.updateBotLists(event.getJDA().getGuilds().size(), event.getJDA().getSelfUser().getId());
    }

}
