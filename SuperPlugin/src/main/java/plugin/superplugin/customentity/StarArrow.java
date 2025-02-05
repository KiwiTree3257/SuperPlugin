package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class StarArrow {
    Location starArrow;
    Vector moveDir;

    public StarArrow(Player player) {
        starArrow = player.getLocation().add(0, 1.5, 0);
        moveDir = player.getLocation().getDirection().normalize().multiply(0.5);
        World world = player.getWorld();

        new BukkitRunnable() {
            int timer = 0;
            Location beforeLoc = starArrow.clone();

            @Override
            public void run() {
                if (timer >= 3 * 20) {
                    cancel();
                }

                ArrayList<LivingEntity> entities = new ArrayList<>(starArrow.getNearbyLivingEntities(1));
                for (LivingEntity entity : entities) {
                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                        continue;
                    }
                    entity.damage(6);
                    cancel();
                }

                world.spawnParticle(Particle.GLOW, starArrow, 10, 0.1, 0.1, 0.1, 0.05);
                if (timer % 10 == 0) {
                    world.spawnParticle(Particle.END_ROD, starArrow, 50, 0, 0, 0, 0.1);
                    world.playSound(starArrow, Sound.ENTITY_ENDER_EYE_LAUNCH, 1, 2);
                }

                beforeLoc = starArrow.clone();
                starArrow.add(moveDir);

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
