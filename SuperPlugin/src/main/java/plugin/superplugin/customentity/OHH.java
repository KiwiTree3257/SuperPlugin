package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.SuperPlugin;

public class OHH {
    ArmorStand ohhEntity;
    Location ohhLocation;
    Location targetLocation;
    Vector moveDir;
    double speed = 0.1;
    World world;

    public OHH(Player player) {
        world = player.getWorld();
        Location playerHeadLoc = player.getLocation().add(0, 1.5, 0);
        targetLocation = playerHeadLoc.clone().add(playerHeadLoc.getDirection().normalize());
        moveDir = playerHeadLoc.getDirection().getCrossProduct(new Vector(0, 1, 0)).getCrossProduct(playerHeadLoc.getDirection()).normalize();
        ohhLocation = targetLocation.clone().add(moveDir.clone().multiply(-1.5));
        ohhEntity = (ArmorStand) world.spawnEntity(ohhLocation.clone().add(0, -1.5, 0), EntityType.ARMOR_STAND);
        ohhEntity.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        //ohhEntity.setInvisible(true);
        ohhEntity.setInvulnerable(true);
        ohhEntity.setSilent(true);
        ohhEntity.setCanMove(false);
        ohhEntity.setGravity(false);

        // 방향 벡터 계산
        double deltaX = playerHeadLoc.getX() - targetLocation.getX();
        double deltaY = playerHeadLoc.getY() - targetLocation.getY(); // 아머 스탠드 머리 높이 보정
        double deltaZ = playerHeadLoc.getZ() - targetLocation.getZ();

        // Yaw (좌우 회전) 계산
        double yaw = Math.atan2(deltaX, deltaZ);

        // Pitch (상하 회전) 계산
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ); // 수평 거리
        double pitch = Math.atan2(deltaY, distance);

        // EulerAngle로 변환 (라디안을 각도로 변환)
        player.sendMessage(Math.toDegrees(pitch) + ", " + Math.toDegrees(yaw));
        EulerAngle headPose = new EulerAngle(-pitch, ohhEntity.getHeadPose().getY() + Math.toRadians(180), 0); // Pitch는 음수로 설정해야 올바르게 보임
        player.sendMessage(headPose.getX() + ", " + headPose.getY());
        ohhEntity.setHeadPose(headPose);

        player.getPersistentDataContainer().set(CustomKeys.MOVE_STOP, PersistentDataType.BOOLEAN, true);

        Vector moveSpeed = moveDir.clone().multiply(speed);
        new BukkitRunnable() {
            int timer = 0;
            double nextDistance = ohhLocation.clone().add(moveSpeed).distance(playerHeadLoc);
            boolean stop = false;
            @Override
            public void run() {
                if (timer >= 2 * 20) {
                    player.getPersistentDataContainer().remove(CustomKeys.MOVE_STOP);
                    cancel();
                }
                double distance = ohhLocation.distance(playerHeadLoc);
                if (nextDistance < distance && !stop) {
                    ohhLocation.add(moveSpeed);
                    nextDistance = distance;
                }
                else {
                    stop = true;
                    ohhLocation = targetLocation;
                }
                ohhEntity.teleport(ohhLocation.clone().add(0, -1.5, 0));

                timer++;
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
    }
}
