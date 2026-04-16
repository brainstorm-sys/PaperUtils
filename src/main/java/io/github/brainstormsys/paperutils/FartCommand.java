package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FartCommand {

    private static Set<UUID> cooldown = new HashSet<>();


    public static void onFart(){
        new CommandAPICommand("fart")
                .executesPlayer((player, args) -> {

                    if(cooldown.contains(player.getUniqueId())){
                        player.sendActionBar(Component.text("⏳ Too gassy! Wait a moment...", NamedTextColor.RED));
                        return;
                    }

                    cooldown.add(player.getUniqueId());

                    player.getServer().getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
                        cooldown.remove(player.getUniqueId());
                    }, 200L);


                    player.sendMessage(Component.text("Ew", NamedTextColor.RED));
                    player.getWorld().playSound(
                            player.getLocation(),
                            "arbiters_crossbow:shart",
                            0.5f,
                            1f

                    );

                    new BukkitRunnable() {
                        int ticks = 0;

                        @Override
                        public void run() {
                            if (ticks >= 10) {
                                cancel();
                                return;
                            }

                            Location buttLocation = player.getLocation().add(
                                    player.getLocation().getDirection().multiply(-0.5)
                            );
                            buttLocation.setY(buttLocation.getY() + 0.8);

                            // Green cloud
                            player.getWorld().spawnParticle(
                                    Particle.ENTITY_EFFECT,
                                    buttLocation,
                                    5,
                                    0.2, 0.1, 0.2,
                                    0,
                                    Color.fromRGB(76, 153, 0)
                            );

                            // Rising smoke
                            player.getWorld().spawnParticle(
                                    Particle.CAMPFIRE_COSY_SMOKE,
                                    buttLocation,
                                    2,
                                    0.1, 0.1, 0.1,
                                    0.01
                            );

                            ticks++;
                        }
                    }.runTaskTimer(PaperUtils.getInstance(), 0L, 2L);
                })
                .register();
    }

}
