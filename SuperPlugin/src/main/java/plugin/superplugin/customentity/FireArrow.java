package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.Iterator;

public class FireArrow {
    int lifeTime = 10 * 20;
    Location fireArrow;
    Vector moveDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FireArrow(Player player) {
        moveDir = player.getLocation().getDirection().normalize().multiply(2);
        world = player.getWorld();
        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, 1.5, 0);
        spawnLoc.add(moveDir.clone().multiply(1));
        fireArrow = spawnLoc;

        world.playSound(spawnLoc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fireArrow.getNearbyLivingEntities(1));
                Iterator<LivingEntity> iterator = nearbyLivingEntities.iterator();
                while (iterator.hasNext()) {
                    LivingEntity entity = iterator.next();
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        iterator.remove();
                    }
                }

                if (timer >= lifeTime || fireArrow.getBlock().getType() != Material.AIR || !nearbyLivingEntities.isEmpty()) {
                    if (!nearbyLivingEntities.isEmpty()) {
                        for (LivingEntity entity : nearbyLivingEntities) {
                            entity.setFireTicks(5 * 20);
                            entity.damage(2);
                        }
                    }
                    if (fireArrow.getBlock().getType() != Material.AIR) {
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
                fireArrow.add(moveDir);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}