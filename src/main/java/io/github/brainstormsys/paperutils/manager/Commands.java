package io.github.brainstormsys.paperutils.manager;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.brainstormsys.paperutils.JailData;
import io.github.brainstormsys.paperutils.PaperUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Commands {

    private final ItemManager itemManager;
    private final Plugin plugin;

    private static Set<UUID> spawnCooldown = new HashSet<>();


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
                .withSubcommand(spawn())
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

    private CommandAPICommand spawn() {
        return new CommandAPICommand("spawn")  //-2694, -1551, 78
                .executesPlayer((player, commandArguments) ->{

                    if(spawnCooldown.contains(player.getUniqueId())){
                        player.sendActionBar(Component.text("⏳ You're on cooldown!", NamedTextColor.RED));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.6f, 1f);
                        return;
                    }

                    spawnCooldown.add(player.getUniqueId());

                    player.getServer().getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
                        spawnCooldown.remove(player.getUniqueId());
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

                            if (countdown == 0){//-2939 108 4153
                                player.teleport(new Location(Bukkit.getWorlds().get(0), -2939, 108, 4153, player.getYaw(), player.getPitch()));
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

                });
    }
}