package io.github.brainstormsys.paperutils.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e){
        Player player = e.getPlayer();

        player.sendMessage(Component.empty());

        player.sendMessage(Component.text("  ───────────────────────────", NamedTextColor.DARK_GRAY));

        player.sendMessage(Component.empty());

        player.sendMessage(
                Component.text()
                        .append(Component.text("                    ⬡ ", NamedTextColor.LIGHT_PURPLE))
                        .append(Component.text("TesseractSMP", NamedTextColor.WHITE))
                        .append(Component.text(" ⬡", NamedTextColor.LIGHT_PURPLE))
                        .build()
        );

        player.sendMessage(Component.empty());

        player.sendMessage(
                Component.text()
                        .append(Component.text("                Welcome, ", NamedTextColor.GRAY))
                        .append(Component.text(player.getName(), NamedTextColor.WHITE))
                        .append(Component.text("!", NamedTextColor.GRAY))
                        .build()
        );

        player.sendMessage(Component.empty());

        player.sendMessage(
                Component.text()
                        .append(Component.text("               « ", NamedTextColor.DARK_GRAY))
                        .append(Component.text("Dimension beyond reach", NamedTextColor.GRAY))
                        .append(Component.text(" »", NamedTextColor.DARK_GRAY))
                        .build()
        );

        player.sendMessage(Component.empty());

        player.sendMessage(Component.text("  ───────────────────────────", NamedTextColor.DARK_GRAY));

        player.sendMessage(Component.empty());
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1f);
    }
}
