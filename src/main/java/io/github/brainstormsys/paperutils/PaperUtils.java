package io.github.brainstormsys.paperutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URI;
import java.time.Duration;
import java.util.*;

public final class PaperUtils extends JavaPlugin implements Listener {

    public static PaperUtils instance;
    public static Location jailLocation; //ez lmao var
    //test configurations

    private static final String PACK_URL =
            "https://github.com/brainstorm-sys/PaperUtils/releases/download/v1.0/ResourcePack.zip";
    private static final Component PROMPT = Component.text(
            "You have to download the ResourcePack in order to join the server",
            NamedTextColor.YELLOW
    );

    public static final Key LOW_PURITY = Key.key("arbiters_crossbow", "low_purity");
    public static final Key LOWMID_PURITY = Key.key("arbiters_crossbow", "lowmid_purity");
    public static final Key MID_PURITY = Key.key("arbiters_crossbow", "mid_purity");
    public static final Key CRYSTALMETH = Key.key("arbiters_crossbow", "crystal");

    @Override
    public void onEnable() {

        instance = this;
        // Plugin startup logic
        this.getComponentLogger().info("<red>The server has Started!");
        CommandAPI.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new JailListener(this), this);
        getServer().getPluginManager().registerEvents(new JailWandItem(this), this);
        getServer().getPluginManager().registerEvents(new CrystalMeth(this), this);
        BurgerLogic.registerRecipe();
        registerRecipies();

        getLogger().info("ForceResourcePack enabled!");

        getConfig().options().copyDefaults();
        saveDefaultConfig();


        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        CustomConfig.get().addDefault("taco", "rice");
        PaperUtils.jailLocation = CustomConfig.loadJailLocation();
        CustomConfig.loadJailedPlayers();;

    }

    public static PaperUtils getInstance(){
        return instance;
    }

    public void registerRecipies(){
        //test for now jesus the amount of shit code
        NamespacedKey key = new NamespacedKey(this, "amythist_to_crystal");

        BlastingRecipe recipe = new BlastingRecipe(
                key,
                CrystalMeth.getlowpurity(),
                Material.AMETHYST_SHARD,
                1.0f,
                140

        );

        Bukkit.addRecipe(recipe);

        BlastingRecipe recipe1 = new BlastingRecipe(
                new NamespacedKey(this, "crystalstage2"),
                CrystalMeth.lowmidpurity(),
                new RecipeChoice.ExactChoice(CrystalMeth.getlowpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe1);

        BlastingRecipe recipe2 = new BlastingRecipe(
                new NamespacedKey(this, "crystalstage3"),
                CrystalMeth.midpurity(),
                new RecipeChoice.ExactChoice(CrystalMeth.lowmidpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe2);

        BlastingRecipe recipe3 = new BlastingRecipe(
                new NamespacedKey(this, "crystalstage4"),
                CrystalMeth.getmeth(),
                new RecipeChoice.ExactChoice(CrystalMeth.midpurity()),
                1.0f,
                140
        );

        Bukkit.addRecipe(recipe3);
    }

    @Override
    public void onLoad(){
        System.out.println("NEW_FILE");
        CommandAPI.onLoad(
                new CommandAPIPaperConfig(this)
                        .silentLogs(true)
                        .fallbackToLatestNMS(false)
                        .missingExecutorImplementationMessage("An error occured")
        );

        SpawnCommand.spawncomm();
        JailCommand.jailcomm();
        CustomItem.itemcomm();
        JailWandItem.gibitemjailwand();
        CrystalMeth.getmeth();
        CrystalMeth.getlowpurity();
        CrystalMeth.lowmidpurity();
        CrystalMeth.midpurity();
        FartCommand.onFart();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();
        CustomConfig.saveJailedPlayers();
        CustomConfig.saveJailLocation(PaperUtils.jailLocation);

        Bukkit.removeRecipe(new NamespacedKey(this, "amythist_to_crystal"));
    }




    // ------------------------------------ uh stuff message ------------------------------------------------------------
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
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
// ------------------------------------ uh stuff message ------------------------------------------------------------


    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        // Changed to DRIED_KELP  cuz low eat time. but im dumb i couldve js used .consumetime()
        if(item.getType() != Material.DRIED_KELP) return;

        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if(model == null) return;

        e.setReplacement(null);

        // Pure Crystal (99.3%)
        if(CRYSTALMETH.equals(model)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Low Purity (20%)
        if(LOW_PURITY.equals(model)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Low-Mid Purity (40%)
        if(LOWMID_PURITY.equals(model)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }

        // Mid Purity (60%)
        if(MID_PURITY.equals(model)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            player.getWorld().playSound(player, "arbiters_crossbow:crystal_smell", 1.0f, 1.0f);
            return;
        }
    }


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

    // Add at top with other variables
    private HashMap<UUID, Long> sniffCooldown = new HashMap<>();
    private static final long COOLDOWN_MS = 2000; // 2 seconds cooldown

    @EventHandler
    public void onStartEating(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null) return;
        if (item.getType() != Material.DRIED_KELP) return;

        Key model = item.getData(DataComponentTypes.ITEM_MODEL);
        if (model == null) return;

        if (CRYSTALMETH.equals(model) || LOW_PURITY.equals(model) ||
                LOWMID_PURITY.equals(model) || MID_PURITY.equals(model)) {

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


    @EventHandler
    public void onEatEvent(PlayerItemConsumeEvent e){
        Player player = e.getPlayer();

        if(e.getItem().getItemMeta()  == null ) return;

        if(!BurgerLogic.SUSHI_KEY.equals(e.getItem().getItemMeta().getItemModel())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0));

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

    }



}