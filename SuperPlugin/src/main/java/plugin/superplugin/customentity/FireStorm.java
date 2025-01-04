package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class FireStorm {
    int lifeTime = 2 * 20;
    Location fireStorm;
    Location beforeLoc;
    Vector moveDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FireStorm(Player player) {
        moveDir = player.getLocation().getDirection().normalize().multiply(1);
        world = player.getWorld();
        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, 1.5, 0);
        spawnLoc.add(moveDir.clone().multiply(5));
        fireStorm = spawnLoc;
        beforeLoc = spawnLoc.clone();

        world.playSound(spawnLoc, Sound.ITEM_FIRECHARGE_USE, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                if (timer >= lifeTime || Function.GetIsCollision(fireStorm, beforeLoc, 0.1)) {
                    world.createExplosion(fireStorm, 5);
                    cancel();
                    return;
                }

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Location fireLoc = Function.GetHighestLocNear(fireStorm.clone().add(i, 0, j), 3);

                        if (fireLoc != null) {
                            fireLoc.add(0, 1, 0).getBlock().setType(Material.FIRE);
                        }
                    }
                }

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fireStorm.getNearbyLivingEntities(3));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId() == player.getUniqueId())
                        continue;

                    entity.setFireTicks(5 * 20);
                }

                world.spawnParticle(Particle.FLAME, fireStorm, 50, 1, 1, 1, 0.3);
                world.spawnParticle(Particle.LARGE_SMOKE, fireStorm, 30, 1, 1, 1, 0.3);
                beforeLoc = fireStorm.clone();
                fireStorm.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
