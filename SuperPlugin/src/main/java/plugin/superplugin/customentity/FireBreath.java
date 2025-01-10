package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class FireBreath {
    int lifeTime = 20;
    Fireball fireBreath;
    Vector moveDir;
    World world;
    SuperPlugin plugin = SuperPlugin.getInstance();
    int timer = 0;

    public FireBreath(Player player, Vector dir) {
        moveDir = dir.clone();
        world = player.getWorld();
        Location spawnLoc = player.getEyeLocation();
        spawnLoc.add(moveDir.normalize());
        fireBreath = (Fireball) world.spawnEntity(spawnLoc, EntityType.FIREBALL);
        fireBreath.setVelocity(moveDir);
        fireBreath.setIsIncendiary(true);
        fireBreath.setYield(1);

        world.playSound(spawnLoc, Sound.ITEM_FIRECHARGE_USE, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;

                if (timer >= lifeTime) {
                    fireBreath.remove();
                    cancel();
                    return;
                }

                world.spawnParticle(Particle.FLAME, fireBreath.getLocation(), 10, 0, 0, 0, 0.1);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}