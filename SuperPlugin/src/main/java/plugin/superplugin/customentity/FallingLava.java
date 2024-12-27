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

public class FallingLava {
    BlockDisplay fallingLava;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FallingLava(Player player, Location location1, Location location2, double height, int time) {
        int lifeTime = 10 * 20;
        int totalTime = time;
        double peakHeight = height;
        Location startLocation = location1.clone();
        Location endLocation = location2;

        world = location1.getWorld();
        fallingLava = (BlockDisplay) world.spawnEntity(startLocation, EntityType.BLOCK_DISPLAY);
        fallingLava.setBlock(Material.MAGMA_BLOCK.createBlockData());

        // x, z축에서의 선형 거리 및 이동 속도
        Vector directionXZ = endLocation.clone().subtract(startLocation).toVector();
        directionXZ.setY(0); // x, z 방향만 남기기
        double distanceXZ = directionXZ.length();
        directionXZ.normalize(); // 단위 벡터로 만들기

        // y축에서의 최고점 계산 (최대 높이와 현재 위치 기반)
        double startY = startLocation.getY();
        double endY = endLocation.getY();
        double maxY = Math.max(startY, endY); // 최고점 y 좌표

        double incrementXZ = distanceXZ / totalTime;

        new BukkitRunnable() {
            @Override
            public void run() {
                Location fallingLavaLoc = fallingLava.getLocation().clone();

                if (timer >= lifeTime || fallingLavaLoc.getBlock().isCollidable()) {
                    Location lavaLoc = Function.GetHighestLocNear(fallingLavaLoc, 5);
                    if (lavaLoc != null) {
                        lavaLoc.add(0, 1, 0);
                        lavaLoc.getBlock().setType(Material.LAVA);
                        world.playSound(lavaLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                lavaLoc.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(plugin, 10 * 20);
                    }

                    fallingLava.remove();
                    cancel();
                    return;
                }

                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(fallingLavaLoc.getNearbyLivingEntities(1));
                for (LivingEntity entity : nearbyLivingEntities) {
                    if (entity.getUniqueId() == player.getUniqueId())
                        continue;

                    entity.setFireTicks(5 * 20);
                }

                // x, z 이동 (선형 이동)
                double x = startLocation.getX() + directionXZ.getX() * incrementXZ * timer;
                double z = startLocation.getZ() + directionXZ.getZ() * incrementXZ * timer;

                // y 이동 (포물선 운동)
                double progress = (double) timer / totalTime;
                double y;
                if (progress < 0.5) {
                    y = maxY + peakHeight * (1 - Math.pow(1 - progress * 2, 2));
                } else {
                    y = maxY + peakHeight * (1 - Math.pow((progress - 0.5) * 2, 2));
                }

                Location newLocation = new Location(world, x, y, z);
                world.spawnParticle(Particle.LARGE_SMOKE, newLocation, 5, 0, 0, 0, 0.1);

                fallingLava.teleport(newLocation.add(-0.5, 0, -0.5));
                timer++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
