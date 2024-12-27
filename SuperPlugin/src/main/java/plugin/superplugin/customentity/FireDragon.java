package plugin.superplugin.customentity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.SuperPlugin;

public class FireDragon {
    private EnderDragon enderDragon;
    private World world;
    private Vector moveDir;

    private int lifeTime = 2 * 20;

    public FireDragon(Player player, Location location) {
        world = player.getWorld();
        Location spawnLoc = location.clone();
        spawnLoc.setDirection(spawnLoc.getDirection().multiply(-1));
        enderDragon = (EnderDragon) world.spawnEntity(spawnLoc, EntityType.ENDER_DRAGON);
        enderDragon.getPersistentDataContainer().set(CustomKeys.FIRE_DRAGON, PersistentDataType.BOOLEAN, true);
        moveDir = spawnLoc.getDirection().normalize().multiply(-1);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (timer >= lifeTime) {
                    enderDragon.setSilent(true);
                    enderDragon.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100 * 20, 0, false, false));
                    enderDragon.setHealth(0);
                    cancel();
                }

                Location newLoc = enderDragon.getLocation().clone().add(moveDir);
                Location playerNewLoc = newLoc.clone();
                playerNewLoc.setDirection(playerNewLoc.getDirection().multiply(-1));
                playerNewLoc.add(0, 2, 0);
                player.teleport(playerNewLoc);
                enderDragon.teleport(newLoc);
                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
