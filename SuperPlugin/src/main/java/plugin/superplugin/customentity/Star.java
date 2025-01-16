package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class Star {
    BlockDisplay star;
    Location beforeLoc;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();

    public Star(Location targetLoc) {
        int lifeTime = 10 * 20;
        double speed = 0.5f;
        int radius = 3;
        int sampleCountSphere = 100;

        world = targetLoc.getWorld();
        star = (BlockDisplay) world.spawnEntity(targetLoc.add(0, 5, 0), EntityType.BLOCK_DISPLAY);
        star.setBlock(Material.AMETHYST_BLOCK.createBlockData());
        beforeLoc = star.getLocation();

        Transformation transformation = new Transformation(new Vector3f(-radius, (float) -radius + 0.5f, -radius), new Quaternionf(), new Vector3f(radius * 2, radius * 2, radius * 2), new Quaternionf());
        star.setTransformation(transformation);

        Vector direction = new Vector(0, -1, 0).multiply(speed);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {

                Location starLoc = star.getLocation();

                if (timer >= lifeTime || Function.GetIsCollision(starLoc, beforeLoc, 0.1)) {

                    world.playSound(starLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);

                    for (int i = 0; i <= sampleCountSphere; i++) {
                        double pitch = Math.PI * (i / (double) sampleCountSphere);

                        for (int j = 0; j <= sampleCountSphere; j++) {
                            double yaw = 2 * Math.PI * (j / (double) sampleCountSphere);

                            for (int k = 0; k < radius; k++) {
                                double x = k * Math.sin(pitch) * Math.cos(yaw);
                                double y = k * Math.cos(pitch);
                                double z = k * Math.sin(pitch) * Math.sin(yaw);

                                Location blockLocation = starLoc.clone().add(x, y, z);

                                blockLocation.setX(blockLocation.getBlockX());
                                blockLocation.setY(blockLocation.getBlockY());
                                blockLocation.setZ(blockLocation.getBlockZ());
                                blockLocation.getBlock().setType(Material.AMETHYST_BLOCK);
                            }
                        }
                    }

                    star.remove();
                    cancel();
                    return;
                }

                world.spawnParticle(Particle.END_ROD, starLoc, 10, 0, 0, 0, 0.1);
                beforeLoc = starLoc.clone();
                star.teleport(starLoc.add(direction));
                timer++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
