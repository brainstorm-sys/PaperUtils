package io.github.brainstormsys.paperutils.manager;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.brainstormsys.paperutils.JailData;
import io.github.brainstormsys.paperutils.PaperUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands {

    private final ItemManager itemManager;
    private final Plugin plugin;

    public Commands(JavaPlugin plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    public void register() {
        new CommandAPICommand("paperutils")
                .withAliases("pu","utils")
                .withSubcommand(jail())
                .withSubcommand(unjail())
                .withSubcommand(items())
                .register();
    }

    private CommandAPICommand items() {
        return new CommandAPICommand("give")
                .withPermission("astral.admin")
                .withArguments(new StringArgument("item")
                        .replaceSuggestions(ArgumentSuggestions.strings(info ->
                                itemManager.getAllItems().keySet().toArray(new String[0])
                        ))
                )
                .withOptionalArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executesPlayer((player, args) -> {
                    String itemId = (String) args.get("item");
                    Player target = (Player) args.getOptional("target").orElse(player);
                    itemManager.give(target, itemId);
                });
    }

    private CommandAPICommand jail() {
        return new CommandAPICommand("jail")
                .withPermission("paperplugin.moderators")
                .withArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executes((sender, args) -> {
                    Player target = (Player) args.get("target");
                    Location loc = JailData.jailLocation;
                    if (loc == null) {
                        sender.sendMessage("§cJail location not set!");
                        return;
                    }
                    target.teleport(loc);
                    target.setBedSpawnLocation(loc, true);
                    JailData.jailedPlayers.put(target.getUniqueId(), loc);
                    JailData.saveAll();

                    target.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 100);
                    target.sendTitle("§4You've been Jailed!", "");
                    target.playSound(target, Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
                });
    }

    private CommandAPICommand unjail() {
        return new CommandAPICommand("unjail")
                .withPermission("paperplugin.moderators")
                .withArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executes((sender, args) -> {
                    Player target = (Player) args.get("target");
                    if (JailData.jailedPlayers.containsKey(target.getUniqueId())) {
                        JailData.jailedPlayers.remove(target.getUniqueId());
                        JailData.saveAll();
                        target.sendMessage("§aYou've been unjailed!");
                        target.sendTitle("§aYou've been unjailed!", "");
                    }
                });
    }
}