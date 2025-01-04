package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.SuperPlugin;

public class OHH {
    ArmorStand ohhEntity;
    Location ohhLocation;
    Location targetLocation;
    Vector moveDir;
    double speed = 0.5;
    World world;

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

        new BukkitRunnable() {
            int timer = 0;
            double beforeDistance = ohhLocation.distance(playerHeadLoc);
            Location playerHeadLoc_B = playerHeadLoc;
            @Override
            public void run() {
                if (timer >= 2 * 20) {
                    cancel();
                }
                playerHeadLoc_B = player.getLocation().clone().add(0, 1.5, 0);
                double distance = ohhLocation.distance(playerHeadLoc_B);
                if (beforeDistance < distance) {

                }
                else {
                    ohhLocation.add(moveDir.clone().multiply(speed));
                    ohhEntity.teleport(ohhLocation.clone().add(0, -1.5, 0));
                }
                beforeDistance = distance;

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
