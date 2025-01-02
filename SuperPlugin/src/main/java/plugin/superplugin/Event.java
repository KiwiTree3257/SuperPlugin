package plugin.superplugin;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;

public class Event implements Listener {
    @EventHandler
    public void EntityMoveEvent(EntityMoveEvent e) {
        Entity entity = e.getEntity();
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        //이동 취소 태그
        if (entityData.has(CustomKeys.MOVE_STOP)) {
            e.setCancelled(true);
        }
    }
}
