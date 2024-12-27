package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class Tornado {
    private Location tornadoLoc;
    private int angleSpeed = 5;
    private double addYVelocity = 0.1;
    private int lifeTime = 20 * 10;
    private World world;

    public Tornado(Player player, Location startLoc, int radius) {
        tornadoLoc = startLoc;
        world = player.getWorld();

        new BukkitRunnable() {
            final PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 2, false, false, false);
            int timer = 0;

            @Override
            public void run() {
                if (timer >= lifeTime) {
                    cancel();
                }

                ArrayList<LivingEntity> entities = new ArrayList<>(tornadoLoc.getNearbyLivingEntities(radius));
                for (LivingEntity entity : entities) {
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        continue;
                    }

                    Location entityLocation = entity.getLocation();

                    // 중심점에서 플레이어까지의 상대 위치 계산
                    Vector relativeVec = new Vector(entityLocation.getX() - tornadoLoc.getX(), 0, entityLocation.getZ() - tornadoLoc.getZ());

                    // 현재 각도 계산
                    double currentAngle = Math.atan2(relativeVec.getZ(), relativeVec.getX());

                    // 각도 증가 (도 단위를 라디안으로 변환)
                    double angleChange = Math.toRadians(angleSpeed);
                    double newAngle = currentAngle + angleChange;

                    // 새로운 X, Z 좌표 계산 (반지름 유지)
                    double newRadius = relativeVec.length() < (radius - 1) ? relativeVec.length() + 0.05 : radius - 1;
                    double newX = tornadoLoc.getX() + newRadius  * Math.cos(newAngle);
                    double newZ = tornadoLoc.getZ() + newRadius * Math.sin(newAngle);

                    // Y축 변화 추가 (소용돌이 효과)
                    double newY = entityLocation.getY() + addYVelocity;

                    // 플레이어의 속도 설정 (새로운 위치 방향으로 이동)
                    Vector velocity = new Vector(newX - entityLocation.getX(), newY - entityLocation.getY(), newZ - entityLocation.getZ());
                    entity.setVelocity(velocity);

                    entity.addPotionEffect(SLOWNESS);
                }

                for (int i = 0; i <= 1; i++) {
                    Location circleLoc = Function.getCircleLocation(radius, angleSpeed * timer + (i * 180), tornadoLoc);
                    for (int j = -radius; j < radius; j++) {
                        Location particleLoc = circleLoc.clone();
                        particleLoc.setY(particleLoc.getY() + j);
                        world.spawnParticle(Particle.CLOUD, particleLoc, 10, 0, 0, 0, 0.05);
                    }
                }

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
