/*package io.github.brainstorm.paper_plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaRequestManager {
    private static final Map<UUID, UUID> tparequests = new HashMap<>();

    public static void registerTpa(){
        new CommandAPICommand("tpa")
                .withArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("target");

                    tparequests.put(target.getUniqueId(), player.getUniqueId());

                    player.sendMessage(Component.text("Request sent to" + target.getName(), NamedTextColor.GREEN));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                })
                .register();
        }

    public static void tpAccept(){
        new CommandAPICommand("tpaccept")
                .withArguments(new EntitySelectorArgument.OneEntity("player"))
                .executesPlayer((player, args) -> {
                    Map<UUID, Long> cooldowns = new HashMap<>();
                    long COOLDOWN= 15000;
                    UUID requesterUUID = tparequests.get(player.getUniqueId());
                    Location startlocation = player.getLocation();
                    Player requester = Bukkit.getPlayer(requesterUUID);
                    long now = System.currentTimeMillis();


                    if (!requesterUUID.equals(requester.getUniqueId())){
                        player.sendMessage(Component.text("That player didn't send you a request", NamedTextColor.RED));
                        return;
                    }
                    if (cooldowns.containsKey(player.getUniqueId())){
                        long lastUse = cooldowns.get(player.getUniqueId());
                        long timeLeft = COOLDOWN - (now - lastUse);

                        if (timeLeft > 0){
                            player.sendMessage(Component.text("You are still on the cooldown! Please wait for" + timeLeft + "seconds!", NamedTextColor.YELLOW));
                            return;
                        }
                    }
                    cooldowns.put(player.getUniqueId(), now);

                    if(player.getLocation().distance(startlocation)>0.1){
                        player.sendMessage(Component.text("You moved, teleportation cancelled", NamedTextColor.RED));
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                        return;
                    }

                    requester.sendMessage(Component.text("Teleporting in 5 seconds", NamedTextColor.YELLOW));

                    Bukkit.getScheduler().runTaskLater(
                            PaperPlugin.instance,
                            () -> {
                                requester.teleport(player.getLocation());
                            },
                            100L
                    );
                    requester.sendMessage(Component.text("Teleportation successful!", NamedTextColor.GREEN));
                })
                .register();
    }

}
*/