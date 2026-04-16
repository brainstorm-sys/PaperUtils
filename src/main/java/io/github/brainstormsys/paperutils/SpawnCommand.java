package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class SpawnCommand {

    private static Set<UUID> spawncooldown = new HashSet<>();

    public static void spawncomm(){
        new CommandAPICommand("spawn")  //-2694, -1551, 78
                .executesPlayer((player, commandArguments) ->{

                    if(spawncooldown.contains(player.getUniqueId())){
                        player.sendActionBar(Component.text("⏳ You're on cooldown!", NamedTextColor.RED));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.6f, 1f);
                        return;
                    }

                    spawncooldown.add(player.getUniqueId());

                    player.getServer().getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
                        spawncooldown.remove(player.getUniqueId());
                    }, 100L);

                    player.sendMessage("§6Teleporting in 5 seconds, §4DONT MOVE!");
                    Location startLocation = player.getLocation();
                    new BukkitRunnable(){

                        int countdown = 5;

                        @Override

                        public void run(){
                            if (player.getLocation().distance(startLocation) > 0.1){
                                player.sendMessage("§4You moved!! Teleportation has been cancelled");
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                                cancel();
                                return;
                            }

                            if (countdown == 0){
                                player.teleport(new Location(Bukkit.getWorlds().get(0), -3076, 102, 4172, player.getYaw(), player.getPitch()));
                                player.sendMessage("§2Teleportation Successful!");
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                                player.getWorld().spawnParticle(
                                        Particle.GLOW_SQUID_INK,
                                        player.getLocation(),
                                        100
                                );
                                cancel();
                                return;
                            }
                            player.sendTitle("§aTeleporting in ", countdown+" §aSeconds", 0, 25, 5 );
                            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f);
                            countdown--;

                        }
                    } .runTaskTimer(JavaPlugin.getPlugin(PaperUtils.class), 0L, 20L);

                })

                .register();

    }
}
