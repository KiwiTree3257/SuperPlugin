package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class FireExplosion {
    int lifeTime = 2 * 20;
    Location fireExplosion;
    Vector moveDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;
    int speed = 1;

    public FireExplosion(Player player) {
        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, 1.5, 0);
        spawnLoc.setPitch(0);
        moveDir = spawnLoc.clone().getDirection().normalize();
        Vector left = moveDir.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        spawnLoc.add(moveDir.clone().multiply(4));
        world = player.getWorld();

        fireExplosion = spawnLoc;
        new BukkitRunnable() {
            ArrayList<UUID> damagedEntity = new ArrayList<>();

            @Override
            public void run() {
                timer++;

                Location highest = Function.GetHighestLocNear(fireExplosion.clone().add(moveDir.clone().multiply(speed)), 10);
                if (timer >= lifeTime || highest == null) {
                    if (highest == null) {
                        world.createExplosion(fireExplosion, 3);
                    }
                    else {
                        world.createExplosion(fireExplosion, 10);
                    }
                    cancel();
                    return;
                }

                for (int i = 5; i >= -5; i--) {
                    for (int j = -3; j <= 3; j++) {
                        Location location = fireExplosion.clone();
                        location.add(left.clone().multiply(j));
                        location.add(0, i, 0);

                        for (int k = -1; k <= 1; k++) {
                            Block block = location.clone().add(k, 0, 0).getBlock();

                            if (block.getType() != Material.AIR) {
                                block.setType(Material.AIR);
                            }
                        }

                        for (int k = -1; k <= 1; k++) {
                            Block block = location.clone().add(0, 0, k).getBlock();

                            if (block.getType() != Material.AIR) {
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
                world.playSound(fireExplosion, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                world.spawnParticle(Particle.EXPLOSION, fireExplosion, 10, 1, 1, 1, 0.2);

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fireExplosion.getNearbyLivingEntities(6));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId() == player.getUniqueId() || damagedEntity.contains(entity.getUniqueId()))
                        continue;

                    entity.damage(6);
                    damagedEntity.add(entity.getUniqueId());
                }

                fireExplosion.add(moveDir.clone().multiply(speed));
                fireExplosion.setY(highest.getY());
            }
        }.runTaskTimer(plugin, 10, 1);
    }
}
