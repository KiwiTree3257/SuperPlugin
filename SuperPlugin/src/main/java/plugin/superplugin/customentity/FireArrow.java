package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.Iterator;

public class FireArrow {
    int lifeTime = 10 * 20;
    Location fireArrow;
    Vector moveDir;
    Location beforeLoc;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FireArrow(Player player) {
        moveDir = player.getLocation().getDirection().normalize().multiply(2);
        world = player.getWorld();
        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, 1.5, 0);
        spawnLoc.add(moveDir.clone().multiply(1));
        fireArrow = spawnLoc.clone();
        beforeLoc = spawnLoc.clone();

        world.playSound(spawnLoc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                ArrayList<LivingEntity> entities = new ArrayList<>(fireArrow.getNearbyLivingEntities(1));
                for (LivingEntity entity : entities) {
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        continue;
                    }
                    entity.setFireTicks(5 * 20);
                    entity.damage(2);
                    cancel();
                }

                if (timer >= lifeTime || Function.GetIsCollision(fireArrow, beforeLoc, 0.1)) {
                    if (Function.GetIsCollision(fireArrow, beforeLoc, 0.1)) {
                        boolean setFire = false;

                        for (int i = -1; i <= 1; i++) {
                            Location fireLoc = fireArrow.clone().add(i, 0, 0);
                            if (fireLoc.getBlock().getType() == Material.AIR || !fireLoc.getBlock().isCollidable()) {
                                fireLoc.getBlock().setType(Material.FIRE);
                                setFire = true;
                                break;
                            }
                        }

                        if (!setFire) {
                            for (int i = -1; i <= 1; i++) {
                                Location fireLoc = fireArrow.clone().add(0, i, 0);
                                if (fireLoc.getBlock().getType() == Material.AIR || !fireLoc.getBlock().isCollidable()) {
                                    fireLoc.getBlock().setType(Material.FIRE);
                                    setFire = true;
                                    break;
                                }
                            }
                        }

                        if (!setFire) {
                            for (int i = -1; i <= 1; i++) {
                                Location fireLoc = fireArrow.clone().add(0, 0, i);
                                if (fireLoc.getBlock().getType() == Material.AIR || !fireLoc.getBlock().isCollidable()) {
                                    fireLoc.getBlock().setType(Material.FIRE);
                                    break;
                                }
                            }
                        }
                    }

                    cancel();
                    return;
                }

                world.spawnParticle(Particle.FLAME, fireArrow, 10, 0, 0, 0, 0);
                beforeLoc = fireArrow.clone();
                fireArrow.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}