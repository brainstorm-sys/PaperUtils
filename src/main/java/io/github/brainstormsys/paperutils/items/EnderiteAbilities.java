package io.github.brainstormsys.paperutils.items;

import io.github.brainstormsys.paperutils.PaperUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class EnderiteAbilities implements Listener {

    private static final double SHOCKWAVE_RADIUS = 4.0;
    private static final double SHOCKWAVE_POWER = 1.5;
    private static final int COOLDOWN_TICKS = 20 * 15; // 15 seconds
    private static final long DOUBLE_SNEAK_TIME = 400; // 400ms window

    private final Map<UUID, Long> firstSneak = new HashMap<>();

    public EnderiteAbilities(PaperUtils paperUtils) {
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
        player.setCooldown(Material.NETHERITE_CHESTPLATE, COOLDOWN_TICKS);

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
        player.setCooldown(Material.NETHERITE_LEGGINGS, COOLDOWN_TICKS);

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

    public final Set<UUID> SPRINT_COOLDOWN = new HashSet<>();
    public static final int COOLDOWN = 15*20;

    @EventHandler
    public void onSprint(PlayerInteractEvent e){
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!player.isSprinting()) return;
        if(SPRINT_COOLDOWN.contains(uuid)) return;
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null) return;
        Key model = player.getInventory().getBoots().getData(DataComponentTypes.ITEM_MODEL);
        if (!model.equals(EnderiteLogic.BOOTS_MODEL)) return;

        SPRINT_COOLDOWN.add(uuid);
        player.setCooldown(Material.NETHERITE_BOOTS, COOLDOWN);
        doSpeed(player);
        EnderiteAbilities.activate(player, 20*5);

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            SPRINT_COOLDOWN.remove(uuid);
        }, COOLDOWN);

    }

    private static final Set<UUID> activeTrails = new HashSet<>();

    public static void activate(Player player, int durationTicks) {
        if (activeTrails.contains(player.getUniqueId())) return;

        activeTrails.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            activeTrails.remove(player.getUniqueId());
        }, durationTicks);
    }

    public static void startFootParticles() {
        new BukkitRunnable() {
            final Particle.DustOptions purple = new Particle.DustOptions(
                    Color.fromRGB(180, 63, 239),
                    1.0f
            );

            @Override
            public void run() {
                for (Player player : PaperUtils.getInstance().getServer().getOnlinePlayers()) {

                    if (!activeTrails.contains(player.getUniqueId())) continue;
                    if (!player.isOnGround()) continue;

                    Location feet = player.getLocation().add(0, 0.1, 0);

                    player.getWorld().spawnParticle(
                            Particle.DUST,
                            feet,
                            3,
                            0.15, 0, 0.15,
                            0,
                            purple
                    );

                    player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            feet,
                            1,
                            0.1, 0, 0.1,
                            0.01
                    );


                }
            }
        }.runTaskTimer(PaperUtils.getInstance(), 0L, 2L);
    }

    private void doSpeed(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 3));
        player.sendActionBar(Component.text("Speed!", NamedTextColor.GRAY));

    }

    // pickaxe




    private final Set<UUID> activeMining = new HashSet<>();
    private final Set<UUID> miningCooldown = new HashSet<>();
    private final int MINING_COOLDOWN = 20*30;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR || !item.hasItemMeta()) return;
        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null || !model.equals(EnderiteLogic.PICKAXE_MODEL)) return;

        // activation logic
        if (player.isSneaking() && !activeMining.contains(player.getUniqueId())) {
            if (miningCooldown.contains(player.getUniqueId())) {
                player.sendActionBar(Component.text("⏳ Mining ability on cooldown!", NamedTextColor.RED));
                return;
            }

            activateMiningAbility(player);
        }

        // break
        if (activeMining.contains(player.getUniqueId())) {
            breakSurroundingBlocks(e.getBlock(), player, item);
        }
    }

    private void activateMiningAbility(Player player) {
        UUID uuid = player.getUniqueId();
        activeMining.add(uuid);
        miningCooldown.add(uuid);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
        player.sendActionBar(Component.text("✨ ENDER-MINING ACTIVE (5s)", NamedTextColor.LIGHT_PURPLE));
        player.setCooldown(Material.NETHERITE_PICKAXE, MINING_COOLDOWN);

        // duration setin
        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            activeMining.remove(uuid);
            if (player.isOnline()) {
                player.sendActionBar(Component.text("⟳ Ability Expired", NamedTextColor.GRAY));
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1.5f);
            }
        }, 100L);

        // cooldown
        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            miningCooldown.remove(uuid);
            if (player.isOnline()) {
                player.sendActionBar(Component.text("✔ Mining Ready!", NamedTextColor.GREEN));
            }
        }, 600L);
    }

    private void breakSurroundingBlocks(Block center, Player player, ItemStack tool) {
        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip original block

                    Block relative = center.getRelative(x, y, z);

                    if (relative.getType().isAir() || relative.getType() == Material.BEDROCK) continue;


                    relative.getWorld().spawnParticle(
                            Particle.REVERSE_PORTAL,
                            relative.getLocation().add(0.5, 0.5, 0.5),
                            5, 0.2, 0.2, 0.2, 0.05
                    );

                    relative.breakNaturally(tool);
                }
            }
        }
    }

    private final Set<UUID> veinMinerCooldown = new HashSet<>();
    private final Set<UUID> markCooldown = new HashSet<>();



    // first ab
    @EventHandler
    public void onAxeHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player attacker) || !(e.getEntity() instanceof LivingEntity victim)) return;

        ItemStack item = attacker.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;
        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null || !model.equals(EnderiteLogic.AXE_MODEL)) return;

        if (markCooldown.contains(attacker.getUniqueId())) return;

        // Apply Glowing for 10 seconds
        victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
        attacker.sendActionBar(Component.text("🎯 EnderMark Applied!", NamedTextColor.LIGHT_PURPLE));

        // Tiny ender particles on victim
        victim.getWorld().spawnParticle(Particle.REVERSE_PORTAL, victim.getLocation().add(0, 1, 0), 15, 0.3, 0.5, 0.3, 0.1);

        markCooldown.add(attacker.getUniqueId());
        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> markCooldown.remove(attacker.getUniqueId()), 100L); // 5s Cooldown
    }

    private static int AXE_COOLDOWN = 20*20;

    // sec ab
    @EventHandler
    public void onTreeBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!Tag.LOGS.isTagged(block.getType())) return;
        if (!player.isSneaking()) return;

        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null || !model.equals(EnderiteLogic.AXE_MODEL)) return;

        if (veinMinerCooldown.contains(player.getUniqueId())) {
            player.sendActionBar(Component.text("⏳ VeinMiner on cooldown!", NamedTextColor.RED));
            return;
        }

        // Start recursive breaking
        veinMine(block, 0);

        veinMinerCooldown.add(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 0.5f, 2f);
        player.setCooldown(Material.NETHERITE_AXE, AXE_COOLDOWN);

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            veinMinerCooldown.remove(player.getUniqueId());
            if (player.isOnline()) player.sendActionBar(Component.text("✔ VeinMiner Ready", NamedTextColor.GREEN));
        }, 400L);
    }

    private void veinMine(Block block, int count) {
        // Limit to 64 blocks to prevent lag/crashing
        if (count > 64 || !Tag.LOGS.isTagged(block.getType())) return;

        // Particles and break
        block.getWorld().spawnParticle(Particle.REVERSE_PORTAL, block.getLocation().add(0.5, 0.5, 0.5), 3, 0.1, 0.1, 0.1, 0.05);
        block.breakNaturally();


        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block relative = block.getRelative(x, y, z);
                    if (Tag.LOGS.isTagged(relative.getType())) {
                        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> veinMine(relative, count + 1), 1L);
                    }
                }
            }
        }
    }

}