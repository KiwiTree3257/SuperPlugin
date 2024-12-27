package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.SuperPlugin;

import java.util.Random;

public class IceSpike {
    private int lifeTime = 15;
    private Vector moveDir;
    private Location iceSpikeLoc;
    private World world;
    public IceSpike(double minYaw, double maxYaw, double minPitch, double maxPitch, Location spawnLoc) {
        Random random = new Random();
        double randomYaw = minYaw + (maxYaw - minYaw) * random.nextFloat();
        double randomPitch = minPitch + (maxPitch - minPitch) * random.nextFloat();
        Location randomLocation = spawnLoc.clone();
        randomLocation.setYaw((float) randomYaw);
        randomLocation.setPitch((float) randomPitch);

        iceSpikeLoc = randomLocation.clone();
        world = iceSpikeLoc.getWorld();
        moveDir = randomLocation.getDirection().normalize();

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (timer >= lifeTime) {
                    cancel();
                    return;
                }

                if (iceSpikeLoc.getBlock().getType() == Material.AIR || !iceSpikeLoc.getBlock().isCollidable()) {
                    iceSpikeLoc.getBlock().setType(Material.BLUE_ICE);
                    world.playSound(iceSpikeLoc, Sound.BLOCK_POWDER_SNOW_PLACE, 1, 1);
                }

                world.spawnParticle(Particle.SNOWFLAKE, iceSpikeLoc, 5, 0, 0, 0, 0.1);

                iceSpikeLoc.add(moveDir);

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}