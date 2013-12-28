package undercast.client;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class UndercastChatHandler {

    public UndercastChatHandler() {
    }

    public boolean handleMessage(String message, String username, EntityPlayer player) {
        return handleMessage(message, username, player, message);
    }

    /**
     * handle a chat message received by the playe
     * 
     * @param message
     *            The message received
     * @param username
     *            The player's username receiving the message
     * @param player
     *            an EntityPlayer instance linking to the client-player
     * @return true if the message should be displayed to the user
     */
    public boolean handleMessage(String message, String username, EntityPlayer player, String normalMessage) {
        boolean returnStatement = true;
        // Friend tracking Joining.
        if (message.contains(" joined the game")) {
            String name;
            String server;
            message = message.replace(" joined the game", "");
            // If it starts with [EU] or [US]
            if (message.startsWith("[" + UndercastData.locationNames[1] + "]") || message.startsWith("[" + UndercastData.locationNames[0] + "]")) {
                name = message.split(" ")[2];
                server = message.split(" ")[1].replace("[", "").replace("]", "");
            } else if (message.contains("[")) {
                name = message.split(" ")[1];
                server = message.split(" ")[0].replace("[", "").replace("]", "");
            } else {
                name = message.substring(message.lastIndexOf("*") + 1, message.length());
                server = UndercastData.server;
            }

            if (message.startsWith("[" + UndercastData.locationNames[1] + "]")) {
                server = server + UndercastData.locationNames[1];
            } else if (message.contains("[" + UndercastData.locationNames[0] + "]")) {
                server = server + UndercastData.locationNames[0];
            } else {
                server = server + (UndercastData.isEU ? UndercastData.locationNames[1] : UndercastData.locationNames[0]);
            }
            if (UndercastData.friends.containsKey(name)) {
                UndercastData.friends.put(name, server);
            }

        } // friend tracking. Leaving
        else if (message.contains("left the game")) {
            String name;
            String server;
            String location;
            message = message.replace(" left the game", "");
            if (message.startsWith("[" + UndercastData.locationNames[1] + "]") || message.startsWith("[" + UndercastData.locationNames[0] + "]")) {
                name = message.split(" ")[2];
                if (message.startsWith("[" + UndercastData.locationNames[0] + "]")) {
                    location = UndercastData.locationNames[0];
                    message = message.replace("[" + UndercastData.locationNames[0] + "]", "");
                } else if (message.startsWith("[" + UndercastData.locationNames[1] + "]")) {
                    location = UndercastData.locationNames[1];
                    message = message.replace("[" + UndercastData.locationNames[1] + "]", "");
                } else {
                    // This should obviously never happens
                    location = UndercastData.isEU ? UndercastData.locationNames[1] : UndercastData.locationNames[0];
                }

                server = message.split(" ")[1].replace("[", "").replace("]", "");
            } else if (message.contains("[")) {
                name = message.split(" ")[1];
                location = UndercastData.isEU ? UndercastData.locationNames[1] : UndercastData.locationNames[0];
                server = message.split(" ")[0].replace("[", "").replace("]", "");
            } else {
                name = message.substring(message.lastIndexOf("*") + 1, message.length());
                location = UndercastData.isEU ? UndercastData.locationNames[1] : UndercastData.locationNames[0];
                server = UndercastData.server;
            }
            if (UndercastData.friends.containsKey(name)) {
                if (UndercastData.friends.get(name).equals(server + location)) {
                    UndercastData.friends.put(name, "offline");
                }
            }
        } // friend tracking - switching
        else if (message.contains(" changed servers")) {
            String name;
            String server;
            String location;
            if (message.startsWith("[" + UndercastData.locationNames[0] + "]")) {
                location = UndercastData.locationNames[0];
                message = message.replace("[" + UndercastData.locationNames[0] + "]", "");
            } else if (message.startsWith("[" + UndercastData.locationNames[1] + "]")) {
                location = UndercastData.locationNames[1];
                message = message.replace("[" + UndercastData.locationNames[1] + "]", "");
            } else {
                location = UndercastData.isEU ? UndercastData.locationNames[1] : UndercastData.locationNames[0];
            }

            message = message.replace(" changed servers", "");
            name = message.substring(message.indexOf("]") + 2);
            server = message.substring(message.indexOf("» ") + 2, message.indexOf("]"));
            if (UndercastData.friends.containsKey(name)) {
                UndercastData.friends.put(name, server + location);
            }
        } // update what map you are playing on
        else if (message.contains("Now playing")) {
            message = message.replace("Now playing ", "");
            UndercastData.setMap((message.split(" by ")[0]));
            if (UndercastData.getKills() == 0 && UndercastData.getDeaths() == 0) { // new
                // match
                // or
                // observer
                // or
                // noob
                UndercastData.reloadServerInformations(false);
                UndercastData.reloadStats();
            }
        } // if you die
        else if (message.startsWith(username) && !message.toLowerCase().endsWith(" team")) {
            // if you die form someone
            if ((message.contains(" by ") || message.contains(" took ") || message.contains(" fury of"))) {
                String killer = message.substring(message.indexOf("by") + 3, message.lastIndexOf("'s") == -1 ? message.length() : message.lastIndexOf("'s"));
                // cut the distance message
                if (killer.contains(" ")) {
                    killer = killer.substring(0, killer.indexOf(' '));
                }

                if (message.contains(" by ") && UndercastCustomMethods.isTeamkill(normalMessage, username, killer)) {
                    return true;
                }
                UndercastData.addKilled(1);
            }
            UndercastData.addDeaths(1);
            UndercastData.setPreviousKillstreak((int) UndercastData.getKillstreak());
            UndercastData.resetKillstreak();
        } else if (message.startsWith(username + " scored") && message.toLowerCase().contains(" team")) {
            int score;
            try {
                score = Integer.parseInt(message.substring(message.indexOf(" scored ") + 8, message.indexOf(" points")));
            } catch (Exception e) {
                score = 0;
            }
            UndercastData.addScore(score);
        }// if you kill a person
        else if ((message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username)) && !message.toLowerCase().contains(" destroyed by ")) {
            if (!UndercastCustomMethods.isTeamkill(normalMessage, username, message.substring(0, message.indexOf(" ")))) {
                UndercastData.addKills(1);
                UndercastData.addKillstreak(1);
            }

        } // when you join a match
        else if (message.contains("You joined the")) {
            UndercastData.reloadStats();
            try {
                UndercastData.setTeam(UndercastData.Teams.valueOf(message.replace("You joined the ", "").replace(" Team", "").replace(" team", "").replace(" Squad", "").replace(" Sun", "").replace(" Moon", "")));
            } catch (Exception e) {
                // if the team set fails because of an alias, set the team to
                // Unknown
                UndercastData.setTeam(UndercastData.Teams.Unknown);
            }
        } else if (!message.startsWith("<") && message.toLowerCase().contains("game over")) {
            UndercastData.isGameOver = true;
            UndercastData.isNextKillFirstBlood = false;
            UndercastData.finalStats = new FinalStats();
            try {
                // stop the timer
                UndercastData.matchTimer.stop();
            } catch (Exception ignored) {
            }
        } else if (!message.startsWith("<") && message.toLowerCase().contains("the match has started")) {
            UndercastData.isGameOver = false;
            UndercastData.isNextKillFirstBlood = true;
            UndercastData.reloadStats();
            UndercastData.victimList.clear();
            UndercastData.killerList.clear();
            // stop the timer
            try {
                UndercastData.matchTimer.stop();
            } catch (Exception ignored) {
            }
            // and start one which starts from 0
            UndercastData.incrementMatchTime = true;
            UndercastData.matchTimeHours = 0;
            UndercastData.matchTimeMin = 0;
            UndercastData.matchTimeSec = 0;
            UndercastData.matchTimer = new MatchTimer();

        } // when a map is done. Display all the stats
        else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second")) {
            UndercastData.resetKills();
            UndercastData.resetKilled();
            UndercastData.resetDeaths();
            UndercastData.resetKillstreak();
            UndercastData.resetLargestKillstreak();
            UndercastData.resetScore();
            UndercastData.setTeam(UndercastData.Teams.Observers);
            return false;
        } // redirection and lobby detection
        else if (message.contains("Welcome to the Overcast Network")) {
            if (UndercastData.redirect) {
                UndercastData.redirect = false;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + UndercastData.directionServer);
            } else {
                UndercastData.setServer("Lobby");
                UndercastCustomMethods.handleServerSwap();
            }
            if (UndercastConfig.showFriends && UndercastData.friends.isEmpty()) {
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            UndercastModClass.getInstance().friendHandler.isListening = true;
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/fr");
                        } catch (InterruptedException ex) {
                        }

                    }
                });
                t1.start();
            }
        } // server detection
        else if (message.contains("Teleporting you to ")) {
            // usage of the /hub or /lobby command
            if (message.contains("the lobby...")) {
                UndercastData.setServer("Lobby");

                UndercastCustomMethods.handleServerSwap();
            } else {
                String server = message.replace("Teleporting you to ", "");
                if (!server.equals(UndercastData.server)) {
                    UndercastData.setServer(server);
                    if (!message.toLowerCase().contains("lobby")) {
                        UndercastData.welcomeMessageExpected = true;
                    }
                    UndercastCustomMethods.handleServerSwap();
                }
            }
        } else if (message.contains("Connecting to ")) {
            String server = message.replace("Connecting to ", "");
            if (!server.equals(UndercastData.server)) {
                UndercastData.setServer(server);
                if (!message.toLowerCase().contains("lobby")) {
                    UndercastData.welcomeMessageExpected = true;
                }
                UndercastCustomMethods.handleServerSwap();
            }
        } else if (message.contains("You are currently on ")) {
            if (UndercastData.serverDetectionCommandExecuted) {
                UndercastData.serverDetectionCommandExecuted = false;
                String server = message.replace("You are currently on ", "");
                if (!server.equals(UndercastData.server)) {
                    UndercastData.setServer(server);
                    UndercastCustomMethods.handleServerSwap();
                }
            }
        } else if (normalMessage.equals("§c§c§e§e§c§c") || message.equals("To view classes, type /classes")) { // GS servers do not send "§c§c§e§e§c§c"
            if (!UndercastData.welcomeMessageExpected) {
                UndercastData.serverDetectionCommandExecuted = true;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server");
            } else {
                UndercastData.welcomeMessageExpected = false;
            }
            if (UndercastConfig.matchOnServerJoin || UndercastConfig.showMatchTime) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/match");
            }
        } // start and sync the match timer
        else if (message.toLowerCase().contains("time:") || message.toLowerCase().contains("score:") || message.toLowerCase().contains("time remaining: ")) {
            String time = "-2:-2";
            String messageToReplace;
            // stop the timer
            try {
                UndercastData.matchTimer.stop();
            } catch (Exception ignored) {
            }
            // extract the time
            messageToReplace = message.split("[0-9]{1,2}[:]{1}[0-5]?[0-9]{1}[:]?[0-5]?[0-9]?")[0];
            time = message.replace(messageToReplace, "");

            // detect if it should increment or decrement
            if (messageToReplace.toLowerCase().contains("time:")) {
                UndercastData.incrementMatchTime = true;
            } else {
                UndercastData.incrementMatchTime = false;
            }

            // read the time
            String[] numbers = time.split("[:]{1}");
            if (numbers.length == 3) {
                UndercastData.matchTimeHours = Integer.parseInt(numbers[0]);
                UndercastData.matchTimeMin = Integer.parseInt(numbers[1]);
                UndercastData.matchTimeSec = Integer.parseInt(numbers[2]);
            } else {
                UndercastData.matchTimeHours = 0;
                UndercastData.matchTimeMin = Integer.parseInt(numbers[0]);
                UndercastData.matchTimeSec = Integer.parseInt(numbers[1]);
            }
            // start the timer
            UndercastData.matchTimer = new MatchTimer();
        } else if (message.startsWith("Current class:")) {
            UndercastData.currentGSClass = message.replace("Current class: ", "");
        } else if (message.startsWith("You have selected")) {
            UndercastData.currentGSClass = message.replace("You have selected ", "");
        } else if (message.startsWith("You have chosen: ")) {
            UndercastData.currentGSClass = message.replace("You have chosen: ", "");
        }
        return returnStatement;

    }

    public static boolean handleTip(String message) {
        try {
            if (message.contains("Tip") && UndercastConfig.filterTips) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
