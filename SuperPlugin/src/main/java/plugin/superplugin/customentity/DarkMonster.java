package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.Random;

public class DarkMonster {
    Zombie darkMonster;
    Player target;
    World world;
    double radius = 15;
    Random random = new Random();

    final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 20, 2, false, false);

    public DarkMonster(Player player) {
        target = player;
        world = player.getWorld();
        Location spawnLoc = getRadiusLoc(player.getLocation(), radius);

        darkMonster = (Zombie) world.spawnEntity(spawnLoc, EntityType.ZOMBIE);
        darkMonster.getEquipment().clear();
        darkMonster.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        darkMonster.setInvisible(true);
        darkMonster.setCustomName("DarkMonster");
        darkMonster.setCustomNameVisible(false);
        darkMonster.setSilent(true);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (timer >= 60 * 20 || !player.getWorld().getName().equals(world.getName()) || darkMonster.isDead()) {
                    darkMonster.setHealth(0);
                    cancel();
                }

                darkMonster.setMemory(MemoryKey.ANGRY_AT, player.getUniqueId());
                darkMonster.addPotionEffect(SPEED);
                world.spawnParticle(Particle.SQUID_INK, darkMonster.getLocation().add(0, 1, 0), 5, 0, 0, 0, 0);

                if (darkMonster.getLocation().distance(target.getLocation()) > radius) {
                    darkMonster.teleport(getRadiusLoc(player.getLocation(), radius));
                    world.playSound(darkMonster.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.1f);
                }
                if (timer % 60 == 0 && random.nextInt(5) == 0) {
                    world.playSound(darkMonster.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.5f, 2);
                }

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }

    private Location getRadiusLoc(Location center, double fradius) {
        int degree = random.nextInt(360);
        Location location = Function.getCircleLocation(fradius, degree, center);

        for (int i = 0; i < 360; i += 2) {
            if (!location.getBlock().isCollidable() && location.clone().add(0, -1, 0).getBlock().isCollidable()) {
                break;
            }
            else {
                location = Function.getCircleLocation(fradius, degree + i, center);
            }
        }

        return location;
    }
}
