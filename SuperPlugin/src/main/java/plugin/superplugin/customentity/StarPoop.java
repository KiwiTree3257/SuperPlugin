package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.stack.StarCount;

import java.util.ArrayList;
import java.util.Objects;

public class StarPoop {
    Slime starPoop;

    public StarPoop(Location spawnLoc) {
        World world = spawnLoc.getWorld();
        starPoop = (Slime) world.spawnEntity(spawnLoc, EntityType.SLIME);
        starPoop.getPersistentDataContainer().set(CustomKeys.STAR_POOP_ENTITY, PersistentDataType.BOOLEAN, true);
        starPoop.setSize(1);
        starPoop.setInvulnerable(true);
        starPoop.setInvisible(true);
        starPoop.setGravity(false);
        starPoop.setSilent(true);
        starPoop.setAI(false);
        starPoop.setGravity(false);
        PotionEffect GLOWING = new PotionEffect(PotionEffectType.GLOWING, 20, 0, false, false);

        new BukkitRunnable() {
            int timer = 0;
            Location nextLoc = starPoop.getLocation();

            @Override
            public void run() {
                Location starPoopLoc = starPoop.getLocation().clone();
                starPoop.addPotionEffect(GLOWING);
                world.spawnParticle(Particle.END_ROD, starPoopLoc, 5, 0, 0, 0, 0.05);

                if (timer >= 10 * 20) {
                    starPoop.teleport(starPoopLoc.clone().set(0, -10000, 0));
                    starPoop.remove();
                    cancel();
                }

                if (Function.GetIsCollision(nextLoc, starPoopLoc, 0.1)) {
                    ArrayList<LivingEntity> entities = new ArrayList<>(starPoopLoc.getNearbyLivingEntities(1));
                    for (LivingEntity entity : entities) {
                        if (entity instanceof Player player && Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), "superkiwi")) {
                            StarCount.addCount(player, 1);

                            starPoop.teleport(starPoopLoc.clone().set(0, -10000, 0));
                            starPoop.remove();
                            cancel();
                        }
                    }
                }
                else {
                    starPoop.teleport(nextLoc);
                    nextLoc = starPoop.getLocation().clone().add(0, -1, 0);
                }

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
