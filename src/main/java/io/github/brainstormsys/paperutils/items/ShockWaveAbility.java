package io.github.brainstormsys.paperutils.items;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.brainstormsys.paperutils.PaperUtils;
import io.github.brainstormsys.paperutils.items.EnderiteLogic;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.naming.Name;
import java.awt.print.Paper;
import java.util.*;

public class ShockWaveAbility implements Listener {

    private static final double SHOCKWAVE_RADIUS = 4.0;
    private static final double SHOCKWAVE_POWER = 1.5;
    private static final int COOLDOWN_TICKS = 20 * 15; // 15 seconds
    private static final long DOUBLE_SNEAK_TIME = 400; // 400ms window

    private final Map<UUID, Long> firstSneak = new HashMap<>();

    public ShockWaveAbility(PaperUtils paperUtils) {
    }

    private final Set<UUID> cooldown = new HashSet<>();

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;

        Player player = e.getPlayer();

        // Check chestplate
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null) return;

        Key model = chestplate.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null) return;
        if (!model.equals(EnderiteLogic.CHESTPLATE)) return;
        if (!player.isOnGround()) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if(cooldown.contains(player.getUniqueId())){
            player.sendActionBar(Component.text("⏳ On cooldown!", NamedTextColor.RED));
            return;
        }

        // checking for the second sneak, subtracting the time from first and second one
        if (!firstSneak.containsKey(uuid) || now - firstSneak.get(uuid) > DOUBLE_SNEAK_TIME) {
            firstSneak.put(uuid, now);

            player.sendActionBar(Component.text("⚡ Sneak again to shockwave!", NamedTextColor.YELLOW));
            return;
        }


        firstSneak.remove(uuid);

        cooldown.add(player.getUniqueId());

        if (!model.equals(EnderiteLogic.CHESTPLATE)) return;

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            cooldown.remove(player.getUniqueId());
            player.sendActionBar(Component.text("⟳ Ability Restored", NamedTextColor.BLUE));
        }, COOLDOWN_TICKS);
        
        doShockwave(player);

    }

    private void doShockwave(Player player) {
        Location center = player.getLocation();

        // Entity pusher thing
        for (Entity entity : player.getNearbyEntities(SHOCKWAVE_RADIUS, SHOCKWAVE_RADIUS, SHOCKWAVE_RADIUS)) {
            if (!(entity instanceof LivingEntity)) continue;

            Vector direction = entity.getLocation().toVector().subtract(center.toVector()).normalize();
            direction.setY(0.5); // Add upward push
            direction.multiply(SHOCKWAVE_POWER);

            entity.setVelocity(direction);
            ((LivingEntity) entity).damage(4.0, player);
        }

        new BukkitRunnable() {
            double radius = 0.6;

            @Override
            public void run() {
                if (radius >= SHOCKWAVE_RADIUS) {
                    cancel();
                    return;
                }


                for (int i = 0; i < 12; i++) {
                    double angle = (2 * Math.PI * i) / 12;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location particleLoc = center.clone().add(x, 0.5, z);
                    player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, particleLoc, 3, 0, 0, 0, 0.05);
                    player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLoc, 1, 0, 0, 0, 0);
                }

                radius += 0.5;
            }
        }.runTaskTimer(PaperUtils.getInstance(), 0L, 1L);

        new BukkitRunnable() {
            int rings = 0;

            @Override
            public void run() {
                if (rings >= 4) {
                    cancel();
                    return;
                }

                double r = 1 + (rings * 0.8);
                for (int i = 0; i < 16; i++) {
                    double angle = (2 * Math.PI * i) / 16;
                    double x = Math.cos(angle) * r;
                    double z = Math.sin(angle) * r;

                    Location loc = center.clone().add(x, 0.1, z);
                    player.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP, loc, 2, 0, 0, 0, 0);
                }

                rings++;
            }
        }.runTaskTimer(PaperUtils.getInstance(), 0L, 2L);

        // Sound effects ez
        player.getWorld().playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.7f, 1.2f);
        player.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.5f);

        // Flash
        player.getWorld().spawnParticle(Particle.FLASH, center.clone().add(0, 1, 0), 1);

        // Message
        player.sendActionBar(Component.text("⚡ Shockwave!", NamedTextColor.LIGHT_PURPLE));
    }

    private final Map<UUID, Long> firstjump = new HashMap<>();


    private final Set<UUID> legging_cooldown = new HashSet<>();
    private final Set<UUID> just_land = new HashSet<>();

    @EventHandler
    public void onJump(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;

        Player player = e.getPlayer();

        // Check leggings
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings == null) return;

        Key model = leggings.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null) return;
        if (!model.equals(EnderiteLogic.LEGGINGS_MDOEL)) return;
        if (player.isOnGround()) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if(legging_cooldown.contains(player.getUniqueId())){
            player.sendActionBar(Component.text("⏳ On cooldown!", NamedTextColor.RED));
            return;
        }

        legging_cooldown.add(player.getUniqueId());

        if (!model.equals(EnderiteLogic.LEGGINGS_MDOEL)) return;

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            legging_cooldown.remove(player.getUniqueId());
            player.sendActionBar(Component.text("⟳ Ability Restored", NamedTextColor.BLUE));
        }, COOLDOWN_TICKS);

        doBoost(player);

    }

    private void doBoost(Player player){

        Vector direction = player.getLocation().getDirection();
        direction.multiply(1.6);
        direction.setY(0.7);

        player.setVelocity(direction);
        Location loc = player.getLocation();
        player.getWorld().playSound(loc, Sound.ENTITY_BREEZE_JUMP, 1f, 1f);
        player.getWorld().spawnParticle(Particle.SPIT, loc, 6, 0, 0.3, 0);

    }
}