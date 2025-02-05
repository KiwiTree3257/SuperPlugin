package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
        int radius = 7;
        int sampleCountSphere = 100;

        world = targetLoc.getWorld();
        star = (BlockDisplay) world.spawnEntity(targetLoc.clone().add(0, 20, 0), EntityType.BLOCK_DISPLAY);
        star.setBlock(Material.AMETHYST_BLOCK.createBlockData());
        beforeLoc = star.getLocation();

        Transformation transformation = new Transformation(new Vector3f(-radius, 0, -radius), new Quaternionf(), new Vector3f(radius * 2, radius * 2, radius * 2), new Quaternionf());
        star.setTransformation(transformation);

        Vector direction = new Vector(0, -1, 0).multiply(speed);
        world.playSound(star.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 2, 0.1f);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {

                Location starLoc = star.getLocation();

                if (timer >= lifeTime || targetLoc.getY() >= starLoc.getY()) {

                    world.playSound(starLoc, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 2, 1);
                    ArrayList<Location> sphereLoc = new ArrayList<>();

                    for (int i = 0; i <= sampleCountSphere; i++) {
                        double pitch = Math.PI * (i / (double) sampleCountSphere);

                        for (int j = 0; j <= sampleCountSphere; j++) {
                            double yaw = 2 * Math.PI * (j / (double) sampleCountSphere);

                            for (int k = 0; k <= radius; k++) {
                                double x = k * Math.sin(pitch) * Math.cos(yaw);
                                double y = k * Math.cos(pitch);
                                double z = k * Math.sin(pitch) * Math.sin(yaw);

                                Location blockLocation = starLoc.clone().add(x, y + radius, z);

                                blockLocation.setX(blockLocation.getBlockX());
                                blockLocation.setY(blockLocation.getBlockY());
                                blockLocation.setZ(blockLocation.getBlockZ());

                                if (k == radius) {
                                    blockLocation.getBlock().setType(Material.AMETHYST_BLOCK);
                                    sphereLoc.add(blockLocation);
                                }
                                else {
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }

                    star.remove();
                    cancel();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            world.playSound(starLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                            for (Location loc : sphereLoc) {
                                if (loc.getBlock().getType() == Material.AMETHYST_BLOCK) {
                                    loc.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }.runTaskLater(SuperPlugin.getInstance(), 5 * 20);
                    return;
                }

                beforeLoc = starLoc.clone();
                star.teleport(starLoc.add(direction));
                timer++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
