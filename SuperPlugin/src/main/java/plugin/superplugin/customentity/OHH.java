package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.Random;

public class OHH {
    Zombie ohhEntity;
    Location ohhLocation;
    Location targetLocation;
    Vector moveDir;
    double speed = 0.1;
    World world;
    Random random = new Random();
    int lifeTIme = 2 * 20;

    public OHH(Player player) {
        world = player.getWorld();
        Location playerHeadLoc = player.getLocation().add(0, 1.5, 0);
        targetLocation = playerHeadLoc.clone().add(playerHeadLoc.getDirection().normalize());
        Function.setLookAt(targetLocation, playerHeadLoc);
        moveDir = playerHeadLoc.getDirection().getCrossProduct(new Vector(0, 1, 0)).getCrossProduct(playerHeadLoc.getDirection()).normalize();
        ohhLocation = targetLocation.clone().add(moveDir.clone().multiply(-1.5));
        ohhEntity = (Zombie) world.spawnEntity(ohhLocation.clone().add(0, -1.5, 0), EntityType.ZOMBIE);
        ohhEntity.getEquipment().clear();
        ohhEntity.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        ohhEntity.setInvisible(true);
        ohhEntity.setInvulnerable(true);
        ohhEntity.setSilent(true);
        ohhEntity.setAI(false);
        ohhEntity.setAdult();
        ohhEntity.setGravity(false);
        ohhEntity.setCollidable(false);

        Vector moveSpeed = moveDir.clone().multiply(speed);

        player.getPersistentDataContainer().set(CustomKeys.MOVE_STOP, PersistentDataType.BOOLEAN, true);
        world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_SCREAM, 1, 1);
        new BukkitRunnable() {
            int timer = 0;
            double nextDistance = ohhLocation.clone().add(moveSpeed).distance(playerHeadLoc);
            boolean stop = false;
            @Override
            public void run() {
                if (timer >= lifeTIme) {
                    ohhEntity.remove();
                    player.getPersistentDataContainer().remove(CustomKeys.MOVE_STOP);
                    cancel();
                    stop = true;
                }
                double distance = ohhLocation.distance(playerHeadLoc);
                if (nextDistance < distance && !stop) {
                    ohhLocation.add(moveSpeed);
                    nextDistance = distance;
                }
                else {
                    ohhLocation = targetLocation;
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false));
                ohhEntity.teleport(ohhLocation.clone().add(0, -1.5, 0));
                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
