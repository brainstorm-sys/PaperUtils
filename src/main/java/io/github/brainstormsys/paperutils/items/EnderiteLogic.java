package io.github.brainstormsys.paperutils.items;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.brainstormsys.paperutils.PaperUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.naming.Name;
import java.awt.print.Paper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EnderiteLogic implements Listener {

    public static final Key ENDERITE_MODEL = Key.key("arbiters_crossbow", "enderite");
    public static final Key SWORD_MODEL = Key.key("arbiters_crossbow","ender_blade");

    public static ItemStack getEnderite(){

        ItemStack enderite = new ItemStack(Material.ANCIENT_DEBRIS);
        enderite.setData(DataComponentTypes.ITEM_MODEL, ENDERITE_MODEL);


        ItemMeta meta = enderite.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><italic:false>Enderite<italic:false></bold></gradient>"
        ));
        meta.lore(List.of(
                Component.empty(),
                Component.text("Enderite!")
        ));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        enderite.setItemMeta(meta);
        return enderite;
    }

    public static ItemStack getSword(){
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.setData(DataComponentTypes.ITEM_MODEL, SWORD_MODEL);

        sword.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_damage"),
                                        9.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )
                        .addModifier(Attribute.BLOCK_INTERACTION_RANGE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_reach"),
                                        15,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )
                        .addModifier(Attribute.ATTACK_SPEED,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "enderite_speed"),
                                        -0.50,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.MAINHAND
                                )

        );

        ItemMeta meta = sword.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><italic:false>Enderblade</italic:false></bold></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("꩜ Shift and Right-Click to Teleport!", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Cooldown: 10 seconds", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Greater Heights!")
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);

        sword.setItemMeta(meta);
        return sword;
    }

    public static void enderComs(){
        new CommandAPICommand("endergive")
                .withPermission("paperplugin.moderators")
                .executesPlayer((player, args) -> {
                    player.give(getEnderite());

                })
                .register();

    }






    // ============ SWORD LOGIC =============//

    public static final int COOLDOWN_TICKS = 20 * 10;
    // js to define shit
    public EnderiteLogic(PaperUtils plugin){
    }
    //

    public static boolean isEnderSword(ItemStack item){
        if(item == null) return false;
        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if(model == null) return false;
        return model.equals(SWORD_MODEL);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!isEnderSword(item)) return;

        if(player.hasCooldown(Material.NETHERITE_SWORD)) return;

        if(!player.isSneaking()) return;

        Block block = e.getClickedBlock();
        Location tpLoc = block.getLocation().add(0.5, 1, 0.5);
        tpLoc.setYaw(player.getLocation().getYaw());
        tpLoc.setPitch(player.getLocation().getPitch());

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), 50, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.teleport(tpLoc);

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, tpLoc.add(0, 1,0), 40, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().playSound(tpLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        player.setCooldown(Material.NETHERITE_SWORD, COOLDOWN_TICKS);

    }

    // ============ SWORD LOGIC =============//


    // ============ HELMET LOGIC ============//

    public static final Key HELMET_MODEL = Key.key("minecraft","enderite_helmet");

    public static ItemStack getHelmet(){
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        helmet.setData(DataComponentTypes.ITEM_MODEL, HELMET_MODEL);

        helmet.setData(DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(EquipmentSlot.HEAD)
                        .assetId(Key.key("minecraft","enderite"))
                        .build()
                );

        helmet.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ARMOR,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "Enderite_Head_armor"),
                                        10,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.HEAD
                                )
                        .addModifier(Attribute.ARMOR_TOUGHNESS,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "Enderite_Head_Tuffness"),
                                        4,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.HEAD
                                )
                        .addModifier(Attribute.KNOCKBACK_RESISTANCE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "knockback"),
                                        0.1,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.HEAD
                        )

                        .build()



                );

        ItemMeta meta = helmet.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><!italic>Enderite Helmet</!italic></bold></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("Be aware of water!", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("Teleport upon damage..", NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addEnchant(Enchantment.PROTECTION, 8, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);

        helmet.setItemMeta(meta);
        return helmet;
    }

    public static final int HELMET_COOLDOWN = 20 * 10;
    private static Set<UUID> cooldown = new HashSet<>();

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player player)) return;

        ItemStack helmet = player.getInventory().getHelmet();
        if(helmet == null) return;

        if(cooldown.contains(player.getUniqueId())) return;

        Key model = helmet.getData(DataComponentTypes.ITEM_MODEL);
        if(model == null) return;
        if(!model.equals(EnderiteLogic.HELMET_MODEL)) return;

        if(Math.random() > 0.20) return;

        double distance = 1 + (Math.random() * 3);
        double angle = Math.random() * 2 * Math.PI;

        double x = Math.cos(angle) * distance;
        double z = Math.sin(angle) * distance;

        Location tpLoc = player.getLocation().add(x, 0, z);
        tpLoc.setY(player.getWorld().getHighestBlockYAt(tpLoc) + 1);

        boolean inWater = player.isInWater();
        boolean inRain = player.getWorld().hasStorm()
                && player.getLocation().getY() >= player.getWorld().getHighestBlockYAt(player.getLocation())
                && !player.getWorld().isClearWeather();

        if(inWater || inRain){
            player.damage(0.5);

            player.getWorld().spawnParticle(
                    Particle.SMOKE,
                    player.getLocation().add(0, 1.8, 0),
                    10, 0.2, 0.1, 0.2, 0.02
            );

            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3f, 1.5f);
        }

        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0,1,0), 30, 0.3, 0.5, 0.3, 0.05);
        player.teleport(tpLoc);
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, tpLoc.clone().add(0, 1, 0), 30, 0.3, 0.5, 0.3, 0.05);
        player.getWorld().playSound(tpLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.5f);

        player.setCooldown(Material.NETHERITE_HELMET, HELMET_COOLDOWN);

        e.setCancelled(true);
        player.sendActionBar(Component.text("✦ Dodge!", NamedTextColor.LIGHT_PURPLE));


        cooldown.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(PaperUtils.getInstance(), () -> {
            cooldown.remove(player.getUniqueId());
        }, HELMET_COOLDOWN);

    }

    public static void startWaterDamageTaskshi(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (var player : PaperUtils.getInstance().getServer().getOnlinePlayers()) {
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet == null) continue;

                    Key model = helmet.getData(DataComponentTypes.ITEM_MODEL);
                    if (model == null) continue;
                    if (!model.equals(EnderiteLogic.HELMET_MODEL)) continue;

                    // Check if in water or rain
                    boolean inWater = player.isInWater();
                    boolean inRain = player.getWorld().hasStorm()
                            && player.getLocation().getBlockY() >= player.getWorld().getHighestBlockYAt(player.getLocation())
                            && !player.getWorld().isClearWeather();

                    if (inWater || inRain) {
                        player.damage(1.0);

                        player.getWorld().spawnParticle(
                                Particle.SMOKE,
                                player.getLocation().add(0, 1.8, 0),
                                10, 0.2, 0.1, 0.2, 0.02
                        );


                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3f, 1.5f);

                        player.sendActionBar(Component.text("☠ Water burns the Enderite!", NamedTextColor.RED));
                    }
                }
            }
        }.runTaskTimer(PaperUtils.getInstance(), 0L, 20L);
    }

    // ============ *END* HELMET LOGIC *END* ============//

    // ============ *START* CHESTPLATE LOGIC *START* =============//

    public static final Key CHESTPLATE = Key.key("minecraft","enderite_chestplate");

    public static ItemStack getChestplate (){
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><!italic><bold>Enderite Chestplate</bold></!italic></gradient>"
        ));

        meta.lore(List.of(
                Component.empty(),
                Component.text("〰 ShockWave", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("   Double Sneak to produce shockwave!", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addEnchant(Enchantment.PROTECTION, 8, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);

        chestplate.setItemMeta(meta);

        chestplate.setData(DataComponentTypes.ITEM_MODEL, CHESTPLATE);
        chestplate.setData(DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(EquipmentSlot.CHEST)
                        .assetId(Key.key("minecraft","enderite"))
                        .build()
                );

        chestplate.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ARMOR,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "chestplate_armor"),
                                        10,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                        EquipmentSlotGroup.CHEST
                        )
                        .addModifier(Attribute.ARMOR_TOUGHNESS,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "chestplate_toughness"),
                                        4,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.CHEST
                        )
                        .addModifier(Attribute.KNOCKBACK_RESISTANCE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "knockback"),
                                        0.1,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.CHEST
                        )
                        .build()
        );
        return chestplate;
    }

    // ============ *END* CHESTPLATE LOGIC *END* =============//

    // ============ *START* LEGGINGS LOGIC *START* ============//

    public static final Key LEGGINGS_MDOEL = Key.key("minecraft", "enderite_leggings");

    public static ItemStack getLeggings() {
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);

        ItemMeta meta = leggings.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#b43fef:#6978f3><bold><!italic>Enderite Leggings</!italic></bold></gradient>"
        ));

        meta.addEnchant(Enchantment.PROTECTION, 8, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);

        meta.lore(List.of(
                Component.empty(),
                Component.text("ᯓ Hypr-Lunge", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Jump and Sneak to Lunge!", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        leggings.setItemMeta(meta);


        leggings.setData(DataComponentTypes.ITEM_MODEL, LEGGINGS_MDOEL);

        leggings.setData(DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(EquipmentSlot.LEGS)
                        .assetId(Key.key("minecraft", "enderite"))
                        .build()
        );

        leggings.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ARMOR,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "leggings_armor"),
                                        10,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.LEGS
                        )
                        .addModifier(Attribute.ARMOR_TOUGHNESS,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "leggings_tuff"),
                                        4,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.LEGS
                        )
                        .addModifier(Attribute.KNOCKBACK_RESISTANCE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "knockback"),
                                        0.1,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.LEGS
                                )

                        .build()
        );

        return leggings;
    }

    // ============ *END* LEGGINGS LOGIC *END* =============//


    // ============ *START* LEGGINGS LOGIC *START* ============//

    public static Key BOOTS_MODEL = new NamespacedKey("minecraft","enderite_boots");

    public static ItemStack getBoots(){
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);

        ItemMeta meta = boots.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#AB32E9:#4054FA><!italic><bold>Enderite Leggings</bold></!italic></gradient>"
        ));

        meta.addEnchant(Enchantment.PROTECTION, 8, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);

        meta.lore(List.of(
                Component.empty(),
                Component.text("⚡ Spped-boost!", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Sprint and Right Click for spped-boost!", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        boots.setItemMeta(meta);

        boots.setData(DataComponentTypes.ITEM_MODEL, BOOTS_MODEL);
        boots.setData(DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(EquipmentSlot.FEET)
                        .assetId(Key.key("minecraft","enderite"))
                        .build()
        );

        boots.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ARMOR,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "boots_armor"),
                                        10,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.FEET
                        )
                        .addModifier(Attribute.ARMOR_TOUGHNESS,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "boots_tough"),
                                        4,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.FEET
                        )
                        .addModifier(Attribute.KNOCKBACK_RESISTANCE,
                                new AttributeModifier(
                                        new NamespacedKey(PaperUtils.getInstance(), "knockbackresboot"),
                                        0.1,
                                        AttributeModifier.Operation.ADD_NUMBER
                                ),
                                EquipmentSlotGroup.FEET
                        )

                        .build()
        );

        return boots;
    }

    public static final Key ORE_MODEL = new NamespacedKey("arbiters_crossbow", "raw_enderite");

    public static ItemStack getRawOre() {
        ItemStack ore = new ItemStack(Material.PAPER);
        ItemMeta meta = ore.getItemMeta();

        ore.displayName().decoration(TextDecoration.ITALIC, false);
        Component displayName = Component.text("Raw Enderite", NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false);

        meta.displayName(displayName);
        meta.getPersistentDataContainer().set(new NamespacedKey("quarkusmagnimusprime", "custom_item_id"), PersistentDataType.STRING, "raw_enderite");
        meta.setItemModel(NamespacedKey.fromString("arbiters_crossbow:raw_enderite"));

        ore.setItemMeta(meta);

        return ore;
    }

    // ============ *END* CHESTPLATE LOGIC *END* =============//

    // pickaxe.

    public static final Key PICKAXE_MODEL = Key.key("arbiters_crossbow", "enderite_pickaxe");

    public static ItemStack getPickaxe() {
        ItemStack pick = new ItemStack(Material.NETHERITE_PICKAXE);
        pick.setData(DataComponentTypes.ITEM_MODEL, PICKAXE_MODEL);

        ItemMeta meta = pick.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><!italic>Enderite Pickaxe</!italic></bold></gradient>"
        ));
        meta.lore(List.of(
                Component.empty(),
                Component.text("⛏ Ender-Mining (5s)", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Shift + Break to activate", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Cooldown: 30s", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addEnchant(Enchantment.EFFICIENCY, 6, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        pick.setItemMeta(meta);
        return pick;
    }

    public static final Key AXE_MODEL = Key.key("arbiters_crossbow", "enderite_axe");

    public static ItemStack getEnderiteAxe() {
        ItemStack axe = new ItemStack(Material.NETHERITE_AXE);
        axe.setData(DataComponentTypes.ITEM_MODEL, AXE_MODEL);

        ItemMeta meta = axe.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize(
                "<gradient:#B43FEF:#6978F3><bold><!italic>Enderite Axe</!italic></bold></gradient>"
        ));
        meta.lore(List.of(
                Component.empty(),
                Component.text("⚔ EnderMark:", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Hitting players makes them GLOW", NamedTextColor.DARK_GRAY),
                Component.empty(),
                Component.text("🪓 VeinMiner:", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  Shift + Break logs to fell trees", NamedTextColor.DARK_GRAY)
        ));

        meta.addEnchant(Enchantment.SHARPNESS, 5, true); // Extra damage
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        axe.setItemMeta(meta);

        axe.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.itemAttributes()
                        .addModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(
                                new NamespacedKey(PaperUtils.getInstance(), "enderite_axe_damage"),
                                13.0,
                                AttributeModifier.Operation.ADD_NUMBER
                        ), EquipmentSlotGroup.MAINHAND
                        )

                        .addModifier(Attribute.ATTACK_SPEED, new AttributeModifier(
                                new NamespacedKey(PaperUtils.getInstance(), "enderite_axe_speed"),
                                -3.2,
                                AttributeModifier.Operation.ADD_NUMBER), EquipmentSlotGroup.MAINHAND)
                        .build());


        return axe;
    }

}
