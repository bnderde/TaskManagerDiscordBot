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
import de.bnder.taskmanager.utils.MessageSender;
import de.bnder.taskmanager.utils.UpdateServerName;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;

public class GuildJoin extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent e) {
        try {
            UpdateServerName.update(e.getGuild());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final String intro = "Thanks for using this bot. The default language is english but you can change the language with the command `-language`.";
        final String msg = "Type `-help` for a complete list of all commands.";
        try {
            MessageSender.send("Hello!", intro + "\n" + msg, e.getGuild().getDefaultChannel(), Color.green, "en", false);
        } catch (InsufficientPermissionException | NullPointerException ex) {
            for (TextChannel tc : e.getGuild().getTextChannels()) {
                try {
                    if (tc.canTalk()) {
                        MessageSender.send("Hello!", intro + "\n" + msg, tc, Color.green, "en", false);
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        UpdateLists.updateBotLists(e.getJDA().getGuilds().size(), e.getJDA().getSelfUser().getId());
    }

}
