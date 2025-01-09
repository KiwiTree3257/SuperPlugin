package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class FireBreath {
    int lifeTime = 20;
    Location fireBreath;
    Vector moveDir;
    Location beforeLoc;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FireBreath(Player player, Vector dir) {
        moveDir = dir.clone();
        world = player.getWorld();
        Location spawnLoc = player.getEyeLocation();
        spawnLoc.add(moveDir.clone().normalize().multiply(player.getVelocity().length()));
        fireBreath = spawnLoc.clone();
        beforeLoc = spawnLoc.clone();

//        world.playSound(spawnLoc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fireBreath.getNearbyLivingEntities(1));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        continue;
                    }

                    entity.setFireTicks(5 * 20);
                    entity.damage(2);
                    cancel();
                }

                if (timer >= lifeTime || Function.GetIsCollision(fireBreath, beforeLoc, 0.1)) {
                    cancel();
                    return;
                }

                world.spawnParticle(Particle.FLAME, fireBreath, 10, 0, 0, 0, 0.1);
                beforeLoc = fireBreath.clone();
                fireBreath.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}