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

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MemberRoleAdd extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        updateUserRoles(event.getMember());
    }

    public static void updateUserRoles(final Member member) {
        final JsonArray jsonArray = new JsonArray();
        for (Role role : member.getRoles()) {
            jsonArray.add(new JsonObject().add("role_id", role.getId()).add("has_admin_permission", role.hasPermission(Permission.ADMINISTRATOR)));
        }
        try {
            Jsoup.connect(Main.tmbApiUrl + "/role-member/add/" + member.getGuild().getId())
                    .method(Connection.Method.POST)
                    .header("authorization", "TMB " + Main.tmbApiAuthorizationToken)
                    .header("user_id", member.getId())
                    .timeout(de.bnder.taskmanager.utils.Connection.timeout)
                    .data("roles", jsonArray.toString())
                    .userAgent(Main.userAgent)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .postDataCharset("UTF-8")
                    .followRedirects(true)
                    .execute();
        } catch (IOException ignored) {
        }
    }

}
