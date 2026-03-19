 package io.github.codingmastermanager.paper_plugin;

import dev.jorel.commandapi.CommandAPICommand;

public class CustomItem {

    public static void itemcomm(){
        new CommandAPICommand("test")
                .executesPlayer((player, commandArguments) -> {
                    player.sendMessage("successful");
                })
                .register();
    }

}
