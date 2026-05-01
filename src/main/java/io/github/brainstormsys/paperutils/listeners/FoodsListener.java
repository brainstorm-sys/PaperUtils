package io.github.brainstormsys.paperutils.listeners;

import io.github.brainstormsys.paperutils.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FoodsListener implements Listener {

    private final ItemManager itemManager;

    public FoodsListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onEatEvent(PlayerItemConsumeEvent e){
        Player player = e.getPlayer();

        if(!itemManager.getItemId(e.getItem()).equals("sushi")) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0)); // Gang what does this have to do with sushi :sob:

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

    }

}
