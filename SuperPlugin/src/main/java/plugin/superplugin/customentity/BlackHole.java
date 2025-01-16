package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class BlackHole {
    BlockDisplay blackHole;
    UUID playerUUID;
    public BlackHole(UUID playerUUID, Location spawnLoc) {
        this.playerUUID = playerUUID;
        Player player = Bukkit.getPlayer(playerUUID);
        World world = player.getWorld();

        blackHole = (BlockDisplay) world.spawnEntity(spawnLoc, EntityType.BLOCK_DISPLAY);
        blackHole.setBlock(Material.BLACK_CONCRETE.createBlockData());
        Transformation transformation = new Transformation(new Vector3f(-1.5f, -1, -1.5f), new Quaternionf(), new Vector3f(3, 3, 3), new Quaternionf());
        blackHole.setTransformation(transformation);

        new BukkitRunnable() {
            int timer = 0;
            int radius = 5;
            int circlePoints = 40;
            int angleSpeed = 10;

            @Override
            public void run() {
                Location blackHoleLoc = blackHole.getLocation();
                if (timer >= 3 * 20) {
                    ArrayList<LivingEntity> entities = new ArrayList<>(blackHoleLoc.getNearbyLivingEntities(radius));
                    for (LivingEntity entity : entities) {
                        entity.setVelocity(entity.getLocation().subtract(blackHoleLoc).toVector().multiply(0.5));
                    }
                    blackHole.remove();

                    cancel();
                    return;
                }


                for (int i = 0; i < circlePoints; i++) {
                    Location particleLoc = Function.getCircleLocation(radius, i * ((double) 360 / circlePoints), blackHoleLoc);
                    Location highestParticleLoc = Function.GetHighestLocNear(particleLoc, 2);
                    if (highestParticleLoc == null) {
                        world.spawnParticle(Particle.END_ROD, particleLoc, 5, 0, 0, 0, 0);
                    }
                    else {
                        highestParticleLoc.add(0, 1, 0);
                        world.spawnParticle(Particle.END_ROD, highestParticleLoc, 5, 0, 0, 0, 0);
                    }
                }

                ArrayList<LivingEntity> entities = new ArrayList<>(blackHoleLoc.getNearbyLivingEntities(radius));
                for (LivingEntity entity : entities) {
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        //  continue;
                    }

                    Location entityLocation = entity.getLocation();

                    // 중심점에서 플레이어까지의 상대 위치 계산
                    Vector relativeVec = new Vector(entityLocation.getX() - blackHoleLoc.getX(), 0, entityLocation.getZ() - blackHoleLoc.getZ());

                    // 현재 각도 계산
                    double currentAngle = Math.atan2(relativeVec.getZ(), relativeVec.getX());

                    // 각도 증가 (도 단위를 라디안으로 변환)
                    double angleChange = Math.toRadians(angleSpeed);
                    double newAngle = currentAngle + angleChange;

                    // 새로운 X, Z 좌표 계산 (반지름 유지)
                    double newRadius = relativeVec.length() > 1 ? relativeVec.length() - 0.2 : 1;
                    double newX = blackHoleLoc.getX() + newRadius  * Math.cos(newAngle);
                    double newZ = blackHoleLoc.getZ() + newRadius * Math.sin(newAngle);

                    // 플레이어의 속도 설정 (새로운 위치 방향으로 이동)
                    Vector velocity = new Vector(newX - entityLocation.getX(), (entityLocation.getY() < blackHoleLoc.getY() ? 1 : -1) * 0.2, newZ - entityLocation.getZ());
                    entity.setVelocity(Function.Lerp(entity.getVelocity(), velocity, 0.5));
                }

                blackHole.setRotation(blackHole.getYaw() + 5, 0);
                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0 ,1);
    }
}
