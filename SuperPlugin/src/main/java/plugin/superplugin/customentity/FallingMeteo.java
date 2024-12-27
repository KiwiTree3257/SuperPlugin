package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class FallingMeteo {
    BlockDisplay fallingMeteo;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FallingMeteo(Player player, Location location1, Location location2) {
        int lifeTime = 10 * 20;
        double speed = 1f;
        int radius = 10;
        Location startLocation = location1.clone();
        Location endLocation = location2;

        world = location1.getWorld();
        fallingMeteo = (BlockDisplay) world.spawnEntity(startLocation, EntityType.BLOCK_DISPLAY);
        fallingMeteo.setBlock(Material.MAGMA_BLOCK.createBlockData());

        Transformation transformation = new Transformation(new Vector3f(0, 0, 0), new Quaternionf(), new Vector3f(5, 5, 5), new Quaternionf());
        fallingMeteo.setTransformation(transformation);

        world.playSound(startLocation, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 2, 0.9f);

        Vector direction = endLocation.clone().subtract(startLocation).toVector();
        direction.normalize();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location fallingMeteoLoc = fallingMeteo.getLocation().clone().add(2.5, 0, 2.5);

                if (timer >= lifeTime || fallingMeteoLoc.getBlock().getType() != Material.AIR) {
                    int sampleCountSphere = 100;

                    world.playSound(fallingMeteoLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                    world.spawnParticle(Particle.EXPLOSION, fallingMeteoLoc, 100, radius, radius, radius, 0);

                    ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fallingMeteoLoc.getNearbyLivingEntities(5));
                    PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 3, false, false, false);
                    PotionEffect BLINDNESS = new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, false, false);
                    for (LivingEntity entity : nearbyLivingEntities) {
                        entity.addPotionEffect(SLOWNESS);
                        entity.addPotionEffect(BLINDNESS);
                        entity.damage(6);
                    }

                    for (int i = 0; i <= sampleCountSphere; i++) {
                        double pitch = Math.PI * (i / (double) sampleCountSphere);

                        for (int j = 0; j <= sampleCountSphere; j++) {
                            double yaw = 2 * Math.PI * (j / (double) sampleCountSphere);

                            for (int k = 0; k < radius; k++) {
                                double x = k * Math.sin(pitch) * Math.cos(yaw);
                                double y = k * Math.cos(pitch);
                                double z = k * Math.sin(pitch) * Math.sin(yaw);

                                Location blockLocation = fallingMeteoLoc.clone().add(x, y, z);

                                blockLocation.setX(blockLocation.getBlockX());
                                blockLocation.setY(blockLocation.getBlockY());
                                blockLocation.setZ(blockLocation.getBlockZ());

                                if (blockLocation.getBlock().getType() != Material.AIR) {
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }

                    fallingMeteo.remove();
                    cancel();
                    return;
                }

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fallingMeteoLoc.getNearbyLivingEntities(3));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId() == player.getUniqueId())
                        continue;

                    entity.setFireTicks(5 * 20);
                }

                double x = startLocation.getX() + direction.getX() * speed * timer;
                double z = startLocation.getZ() + direction.getZ() * speed * timer;
                double y = startLocation.getY() + direction.getY() * speed * timer;

                Location newLocation = new Location(world, x, y, z);
                world.spawnParticle(Particle.LARGE_SMOKE, fallingMeteoLoc, 10, 0, 0, 0, 0.1);
                Particle.DustOptions redDust = new Particle.DustOptions(Color.RED, 3);
                world.spawnParticle(Particle.DUST, endLocation, 10, 1, 1, 1, 0, redDust);

                fallingMeteo.teleport(newLocation.add(-2.5, 0, -2.5));
                timer++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
