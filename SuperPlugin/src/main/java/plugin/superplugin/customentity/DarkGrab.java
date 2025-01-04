package plugin.superplugin.customentity;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class DarkGrab {
    private Location darkGrab;
    private Location beforeLoc;
    private Vector moveVec;
    private int grabTime = 2 * 20;
    private int lifeTime = 5 * 20;
    private World world;
    LivingEntity targetEntity = null;
    Block targetBlock = null;
    private final Particle.DustOptions blackDust = new Particle.DustOptions(Color.BLACK, 1);

    public boolean isDead = false;

    public DarkGrab(Player player, int cooltime) {
        moveVec = player.getLocation().getDirection().normalize().multiply(1);
        darkGrab = player.getLocation().clone().add(0, 1.5, 0).add(moveVec);
        beforeLoc = darkGrab.clone();
        world = player.getWorld();

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if ((timer >= grabTime && targetEntity == null && targetBlock == null) || !Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), "supereunhoo") || timer >= lifeTime || isDead) {
                    isDead = true;
                    cancel();
                }

                //충돌
                if (targetEntity != null || targetBlock != null) {
                    Location playerLoc = player.getLocation().clone().add(0, 1, 0);
                    Vector grabDir = darkGrab.clone().subtract(playerLoc).toVector().normalize();
                    playerLoc.add(grabDir);
                    double distance = playerLoc.distance(darkGrab);
                    for (double i = 0; i < distance; i += 0.1) {
                        world.spawnParticle(Particle.DUST, playerLoc.clone().add(grabDir.clone().multiply(i)), 1, 0 ,0 ,0, 0, blackDust);
                    }

                    if (targetEntity != null) {
                        darkGrab = targetEntity.getLocation().clone().add(0, targetEntity.getHeight() / 2, 0);
                    }
                    else {
                        darkGrab = targetBlock.getLocation().add(0.5, 0.5, 0.5);
                    }
                }
                else {
                    world.spawnParticle(Particle.DUST, darkGrab, 10, 0 ,0 ,0, 0.1, blackDust);

                    ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(darkGrab.getNearbyLivingEntities(0.5));
                    for (LivingEntity entity : nearbyLivingEntities) {
                        if (entity.getUniqueId() == player.getUniqueId())
                            continue;

                        targetEntity = entity;
                    }
                    if (Function.GetIsCollision(darkGrab, beforeLoc, 0.1)) {
                        targetBlock = darkGrab.getBlock();
                    }

                    beforeLoc = darkGrab.clone();
                    darkGrab.add(moveVec);
                }

                CoolTimeManager.SetCoolTime(player, "supereunhoo", 1, cooltime);

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }

    public Location getTargetLocation() {
        Location targetLoc = null;

        if (targetEntity != null) {
            targetLoc = targetEntity.getLocation().clone().add(0, targetEntity.getHeight() / 2, 0);
        }
        else if (targetBlock != null) {
            targetLoc = targetBlock.getLocation().add(0.5, 0.5, 0.5);
        }

        return targetLoc;
    }
}
