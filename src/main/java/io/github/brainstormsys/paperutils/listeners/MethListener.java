package io.github.brainstormsys.paperutils.listeners;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MethListener implements Listener {

    private final ItemManager itemManager;

    public MethListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (!itemManager.isItem(item, "meth")) return;

        String itemId = itemManager.getItemId(item);

        e.setReplacement(null);

        // Pure Crystal (99.3%)
        if(Objects.equals(itemId, "crystal")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Low Purity (20%)
        if(Objects.equals(itemId, "low_purity")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Low-Mid Purity (40%)
        if(Objects.equals(itemId, "lowmid_purity")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Mid Purity (60%)
        if(Objects.equals(itemId, "mid_purity")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }
    }

    // Add at top with other variables
    private HashMap<UUID, Long> sniffCooldown = new HashMap<>();
    private static final long COOLDOWN_MS = 2000; // 2 seconds cooldown

    @EventHandler
    public void onStartEating(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null) return;

        if (
                itemManager.getItemId(item).equals("crystal") ||
                        itemManager.getItemId(item).equals("crystal") ||
                        itemManager.getItemId(item).equals("crystal")
        ) {

            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();

            // Check cooldown
            if (sniffCooldown.containsKey(playerId)) {
                long lastUsed = sniffCooldown.get(playerId);
                if (currentTime - lastUsed < COOLDOWN_MS) {
                    return; // Still on cooldown, don't play sound
                }
            }

            sniffCooldown.put(playerId, currentTime);

            player.getWorld().playSound(
                    player.getLocation(),
                    "arbiters_crossbow:crystal_sniff",
                    1.0f,
                    1.0f
            );
        }
    }
}
