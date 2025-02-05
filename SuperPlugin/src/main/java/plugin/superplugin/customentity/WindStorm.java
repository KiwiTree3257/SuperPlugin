package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class WindStorm {
    int lifeTime = 2 * 20;
    Location windStorm;
    Vector moveDir;
    Vector knockBackDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();

    ArrayList<Entity> moveEntities;

    public WindStorm(Player player) {
        moveDir = player.getLocation().getDirection().normalize().multiply(1);
        knockBackDir = moveDir.clone().multiply(2);
        world = player.getWorld();
        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, 1.5, 0);
        spawnLoc.add(moveDir.clone().multiply(3));
        windStorm = spawnLoc;
        moveEntities = new ArrayList<>();

        world.playSound(spawnLoc, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 1);

        new BukkitRunnable() {
            int timer = 0;
            @Override
            public void run() {
                timer++;

                if (timer >= lifeTime) {
                    cancel();
                    return;
                }

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(windStorm.getNearbyLivingEntities(2));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId() == player.getUniqueId() || moveEntities.contains(entity))
                        continue;

                    Vector velocity = entity.getVelocity();
                    velocity.add(knockBackDir);
                    entity.setVelocity(velocity);
                    entity.damage(4);

                    moveEntities.add(entity);
                }

                world.spawnParticle(Particle.WHITE_SMOKE, windStorm, 60, 1, 1, 1, 0);
                windStorm.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
