package de.bnder.taskmanager.server;
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

import com.sun.net.httpserver.HttpServer;
import de.bnder.taskmanager.server.routes.CheckServerMember;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {


    public static void startServer() {
        try {
            System.out.println("Starting Server...");
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            /* Add routes */
            server.createContext("/check-server-member", new CheckServerMember());

            server.setExecutor(null);
            server.start();
            System.out.println("Server started.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
