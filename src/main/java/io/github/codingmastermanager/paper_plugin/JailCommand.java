package io.github.codingmastermanager.paper_plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class JailCommand {
    public static void jailcomm() {

        new CommandAPICommand("jail")
                .withArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("target");//-2645 -2148 77
                    Location jailLocation = new Location(Bukkit.getWorlds().get(0), -2645, 77 ,-2148);
                    target.teleport(jailLocation);
                    target.setBedSpawnLocation(jailLocation);
                    JailData.jailedPlayers.put(target.getUniqueId(), jailLocation);
                    CustomConfig.saveJailedPlayers();

                    target.getWorld().spawnParticle(
                            Particle.LAVA,
                            target.getLocation(),
                            100

                    );

                    target.sendTitle("§4You've been Jailed!", "");
                    target.sendMessage("§4You've been jailed, ask admins for more info");
                    target.playSound(target, Sound.BLOCK_ANVIL_PLACE, 1f, 1f);


                })
                .register();

        new CommandAPICommand("unjail")
                .withArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("target");
                    if (JailData.jailedPlayers.containsKey(target.getUniqueId())){
                        JailData.jailedPlayers.remove(target.getUniqueId());
                        target.sendMessage("§aYou've been unjailed! Don't forget to read the rules this time!");
                        target.playSound(target, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1f, 1f);
                        target.getWorld().spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                target.getLocation(),
                                100

                        );
                        target.sendTitle("§aYou've been unjailed!","");
                    }

                })
                .register();
    }
}

