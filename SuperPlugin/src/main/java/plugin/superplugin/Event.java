package plugin.superplugin;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
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
        Location from = e.getFrom();
        Location to = e.getTo();

        //이동 취소 태그
        if (entityData.has(CustomKeys.MOVE_STOP)) {
            Location fixedLocation = new Location(from.getWorld(), from.getX(), Math.min(to.getY(), from.getY()), from.getZ(), from.getYaw(), from.getPitch());
            e.setTo(fixedLocation);
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        Location from = e.getFrom();
        Location to = e.getTo();

        //이동 취소 태그
        if (playerData.has(CustomKeys.MOVE_STOP)) {
            Location fixedLocation = new Location(from.getWorld(), from.getX(), Math.min(to.getY(), from.getY()), from.getZ(), from.getYaw(), from.getPitch());
            e.setTo(fixedLocation);
        }
    }
}
