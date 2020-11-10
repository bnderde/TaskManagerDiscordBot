package de.bnder.taskmanager.commands

import de.bnder.taskmanager.main.Command
import de.bnder.taskmanager.main.Main
import de.bnder.taskmanager.utils.Connection
import de.bnder.taskmanager.utils.Localizations
import de.bnder.taskmanager.utils.MessageSender
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.jsoup.Jsoup
import java.awt.Color
import java.io.IOException
import java.util.*

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
class Language : Command {
    @Throws(IOException::class)
    override fun action(args: Array<String>, event: GuildMessageReceivedEvent) {
        val guild = event.guild
        if (args.size == 0) {
            val langCode = Localizations.getGuildLanguage(guild)
            val embedTitle = Localizations.getString("language_message_title", langCode)
            MessageSender.send(embedTitle, Localizations.getString("language_message_invalid_params", langCode), event.message, Color.red)
        } else if (args.size == 1) {
            val language = args[0].toLowerCase()
            if (language.equals("de", ignoreCase = true) || language.equals("en", ignoreCase = true) || language.equals("bg", ignoreCase = true)
                    || language.equals("fr", ignoreCase = true) || language.equals("ru", ignoreCase = true) || language.equals("pl", ignoreCase = true)) {
                if (Objects.requireNonNull(event.member)!!.hasPermission(Permission.ADMINISTRATOR)) {
                    val res = Jsoup.connect(Main.requestURL + "/server/language/" + event.guild.id).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).data("language", language).postDataCharset("UTF-8").header("user_id", event.member!!.id).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute()
                    val embedTitle = Localizations.getString("language_message_title", language)
                    if (res.statusCode() == 200) {
                        MessageSender.send(embedTitle, Localizations.getString("sprache_geändert", language), event.message, Color.green)
                    } else {
                        MessageSender.send(embedTitle, Localizations.getString("abfrage_unbekannter_fehler", language), event.message, Color.red)
                    }
                } else {
                    val langCode = Localizations.getGuildLanguage(event.guild)
                    val embedTitle = Localizations.getString("language_message_title", langCode)
                    MessageSender.send(embedTitle, Localizations.getString("muss_serverbesitzer_oder_adminrechte_haben", langCode), event.message, Color.red)
                }
            } else {
                val langCode = Localizations.getGuildLanguage(event.guild)
                val embedTitle = Localizations.getString("language_message_title", langCode)
                MessageSender.send(embedTitle, Localizations.getString("language_message_invalid_params", langCode), event.message, Color.red)
            }
        }
    }
}