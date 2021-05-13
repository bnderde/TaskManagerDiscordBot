package de.bnder.taskmanager.listeners.roles;
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

import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Connection;

import java.io.IOException;

public class RoleCreate extends ListenerAdapter {

    @Override
    public void onRoleCreate(RoleCreateEvent event) {
        final String roleID = event.getRole().getId();
        try {
            Main.tmbAPI("role/add/" + event.getGuild().getId(), null, Connection.Method.POST, event.getGuild().getId()).data("role_id", roleID).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
