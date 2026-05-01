package io.github.brainstormsys.paperutils.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.time.Duration;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getPlayer();
        Location deathloc = player.getLocation();

        player.getWorld().strikeLightningEffect(deathloc);

        player.getWorld().spawnParticle(
                Particle.SOUL,
                deathloc,
                30
        );
        Title deathtitle = Title.title(
                Component.text("☠ YOU DIED ☠", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                Component.text("Prepare to respawn...", NamedTextColor.GRAY),
                Title.Times.times(
                        Duration.ofMillis(200),
                        Duration.ofSeconds(2),
                        Duration.ofMillis(300)
                )
        );

        player.showTitle(deathtitle);
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        Location respawnloc = e.getRespawnLocation();
        p.playSound(respawnloc, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 1f);
        p.getWorld().spawnParticle(
                Particle.END_ROD,
                respawnloc,
                30
        );


    }


}
