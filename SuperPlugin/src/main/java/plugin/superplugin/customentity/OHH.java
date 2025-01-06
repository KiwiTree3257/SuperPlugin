package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.SuperPlugin;

import java.util.Random;

public class OHH {
    ArmorStand ohhEntity;
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
        moveDir = playerHeadLoc.getDirection().getCrossProduct(new Vector(0, 1, 0)).getCrossProduct(playerHeadLoc.getDirection()).normalize();
        ohhLocation = targetLocation.clone().add(moveDir.clone().multiply(-1.5));
        ohhEntity = (ArmorStand) world.spawnEntity(ohhLocation.clone().add(0, -1.5, 0), EntityType.ARMOR_STAND);
        ohhEntity.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        ohhEntity.setInvisible(true);
        ohhEntity.setInvulnerable(true);
        ohhEntity.setSilent(true);
        ohhEntity.setCanMove(false);
        ohhEntity.setGravity(false);

        double deltaX = playerHeadLoc.getX() - targetLocation.getX();
        double deltaY = playerHeadLoc.getY() - targetLocation.getY();
        double deltaZ = playerHeadLoc.getZ() - targetLocation.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitch = Math.atan2(deltaY, distance);
        EulerAngle headPose = new EulerAngle(-pitch, ohhEntity.getHeadPose().getY() + Math.toRadians(180), 0); // Pitch는 음수로 설정해야 올바르게 보임

        Vector moveSpeed = moveDir.clone().multiply(speed);

        player.getPersistentDataContainer().set(CustomKeys.MOVE_STOP, PersistentDataType.BOOLEAN, true);
        world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_SCREAM, 1, 1);
        new BukkitRunnable() {
            int timer = 0;
            double nextDistance = ohhLocation.clone().add(moveSpeed).distance(playerHeadLoc);
            boolean stop = false;
            @Override
            public void run() {
                if (timer >= 2 * 20) {
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
                ohhEntity.setHeadPose(headPose.add(Math.toRadians(random.nextInt(21)-10), Math.toRadians(random.nextInt(21)-10), 0));
                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
