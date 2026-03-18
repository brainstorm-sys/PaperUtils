/*package New_Project_Paper.plugin.paperPlugin;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.entity.Entity;

public class XPBottleDestroyerPro implements Listener {
    @EventHandler
    public void xpBottleDestroyer(ExpBottleEvent e){
        Block block = e.getHitBlock();
        if (block != null)
            block.breakNaturally();

        Entity hitEntity = e.getHitEntity();

        if (hitEntity instanceof LivingEntity living){
            living.setHealth(0);

            Entity entity = e.getEntity();

            if (e.getEntity().getShooter() instanceof Player p){
                p.sendMessage("§4Rip L Bozo");
                p.playSound(p.getLocation(), Sound.ITEM_SPEAR_ATTACK, 1f, 1f);
            }
        }

    }


}
*/