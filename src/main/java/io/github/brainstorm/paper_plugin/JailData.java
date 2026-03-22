package io.github.brainstorm.paper_plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class JailData {
    public static HashMap<UUID, Location> jailedPlayers = new HashMap<>();

    public static void jailcall(Player player){
        Location jailLocation = PaperPlugin.jailLocation;
        player.teleport(jailLocation);
        player.setRespawnLocation(jailLocation);
        JailData.jailedPlayers.put(player.getUniqueId(), jailLocation);
        CustomConfig.saveJailedPlayers();

        player.getWorld().spawnParticle(
                Particle.LAVA,
                player.getLocation(),
                100

        );

        player.sendTitlePart(
                TitlePart.TITLE,
                Component.text("You've been Jailed!", NamedTextColor.DARK_RED)

        );

        player.sendMessage(Component.text("You've been Jailed, ask the admins for more info!", NamedTextColor.DARK_RED));
        player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
    }
}
