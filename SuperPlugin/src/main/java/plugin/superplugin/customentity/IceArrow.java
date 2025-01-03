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
import plugin.superplugin.stack.FreezeStack;

import java.util.ArrayList;
import java.util.Iterator;

public class IceArrow {
    int lifeTime = 10 * 20;
    Location iceArrow;
    Location beforeLoc;
    Vector moveDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public IceArrow(Player player, Location start) {
        moveDir = start.getDirection().normalize().multiply(2);
        world = player.getWorld();
        Location spawnLoc = start.clone();
        iceArrow = spawnLoc;
        beforeLoc = spawnLoc.clone();

        world.playSound(spawnLoc, Sound.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(iceArrow.getNearbyLivingEntities(1));
                Iterator<LivingEntity> iterator = nearbyLivingEntities.iterator();
                while (iterator.hasNext()) {
                    LivingEntity entity = iterator.next();
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        iterator.remove();
                    }
                }

                if (timer >= lifeTime || iceArrow.getBlock().getType() != Material.AIR || !nearbyLivingEntities.isEmpty()) {
                    if (!nearbyLivingEntities.isEmpty()) {
                        LivingEntity entity = nearbyLivingEntities.get(0);
                        FreezeStack.FreezeEntity(entity);
                        entity.damage(1);
                    }

                    if (Function.GetIsCollision(iceArrow, beforeLoc, 0.1) && iceArrow.getBlock().getType() != Material.SNOW) {
                        Location snowLoc = Function.GetHighestLocNear(iceArrow, 2);
                        if (snowLoc != null) {
                            snowLoc.add(0, 1, 0).getBlock().setType(Material.SNOW);
                        }
                    }

                    cancel();
                    return;
                }



                world.spawnParticle(Particle.SNOWFLAKE, iceArrow, 10, 0, 0, 0, 0);
                beforeLoc = iceArrow.clone();
                iceArrow.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}