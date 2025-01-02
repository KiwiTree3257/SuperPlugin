package plugin.superplugin.customworld.darkworld;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DarkWorldEvent implements Listener {
    @EventHandler
    public void BlockCanBuildEvent(BlockPlaceEvent e) {
        e.setCancelled(false);
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        e.setCancelled(false);
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        e.setCancelled(false);
    }
}
