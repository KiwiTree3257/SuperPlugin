package plugin.superplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.bossbar.FreezeBossBar;

import java.util.*;

public class Function {
    public static int[] FindItemPDIndexesAtPlayerInventory(Player player, ItemStack findItem, PersistentDataType dataType) {
        ArrayList<Integer> indexes = new ArrayList<>();
        Inventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize() + 1; i++) {
            ItemStack item = player.getInventory().getItem(i);

            if (item == null || findItem == null)
                continue;

            if (CompareItemPersistentData(item, findItem, dataType)) {
                indexes.add(i);
            }
        }
        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }

    public static boolean CompareItemPersistentData(ItemStack a, ItemStack b, PersistentDataType dataType) {
        if (a.getItemMeta() == null || b.getItemMeta() == null)
            return false;

        PersistentDataContainer aData = a.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer bData = b.getItemMeta().getPersistentDataContainer();
        Iterator<NamespacedKey> keys = bData.getKeys().iterator();

        while (keys.hasNext()) {
            NamespacedKey key = keys.next();

            if (aData.get(key, dataType) != bData.get(key, dataType)) {
                return false;
            }
        }

        return true;
    }

    public static Location GetHighestLocNear(Location location, int yRange) {
        for (int i = -yRange; i <= yRange; i++) {
            Location airLoc = location.clone().add(0, i + 1, 0);
            Location blockLoc = location.clone().add(0, i, 0);

            if ((blockLoc.getBlock().getType() != Material.AIR && blockLoc.getBlock().isCollidable()) && (airLoc.getBlock().getType() == Material.AIR || !airLoc.getBlock().isCollidable())) {
                return blockLoc;
            }
        }
        return null;
    }

    public static ItemStack BuildItem(Material type, int amount, String displayName, String... lore) {
        ItemStack stack = new ItemStack(type, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return stack;
    }

    public static ArrayList<Player> GetSuperPlayers(String s) {
        ArrayList<Player> temp = new ArrayList<>(Bukkit.getOnlinePlayers());
        ArrayList<Player> players = new ArrayList<>();

        for (Player player : temp) {
            if (Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), s)) {
                players.add(player);
            }
        }

        return players;
    }

    public static Location RandomSphereLocation(int radius, Location offset) {
        Random random = new Random();

        double x, y, z;
        do {
            x = (random.nextDouble() * 2 - 1) * radius;
            y = (random.nextDouble() * 2 - 1) * radius;
            z = (random.nextDouble() * 2 - 1) * radius;
        } while (x * x + y * y + z * z > radius * radius);

        double finalX = offset.getX() + x;
        double finalY = offset.getY() + y;
        double finalZ = offset.getZ() + z;

        return new Location(offset.getWorld(), finalX, finalY, finalZ);
    }

    public static Location setLookAt(Location loc1, Location loc2) {
        // Calculate the differences
        double deltaX = loc2.getX() - loc1.getX();
        double deltaY = loc2.getY() - loc1.getY();
        double deltaZ = loc2.getZ() - loc1.getZ();

        // Calculate yaw
        double yaw = Math.toDegrees(Math.atan2(-deltaX, deltaZ));

        // Calculate distance in the XZ plane
        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // Calculate pitch
        double pitch = Math.toDegrees(Math.atan2(-deltaY, distanceXZ));

        // Set loc1's yaw and pitch
        loc1.setYaw((float) yaw);
        loc1.setPitch((float) pitch);

        return loc1;
    }

    public static Location getCircleLocation(double radius, double degree, Location center) {
        double angle = Math.toRadians(degree);
        double x = radius * Math.cos(angle) + center.getX();
        double z = radius * Math.sin(angle) + center.getZ();

        Location circleLoc = center.clone();
        circleLoc.setX(x);
        circleLoc.setZ(z);

        return circleLoc;
    }

    public static boolean CheckSkillUse(Player player, String supername, int skillNumber) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (CoolTimeManager.CheckCoolTime(player, supername, skillNumber)) {
            player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, skillNumber) + "초 남음");
            return true;
        } else if (playerData.has(CustomKeys.SKILL_STOP)) {
            player.sendMessage("스킬을 사용할 수 없습니다");
            return true;
        }

        return false;
    }

    public static boolean GetIsCollision(Location nowLoc, Location beforeLoc, double step) {
        Vector dir = nowLoc.clone().subtract(beforeLoc).toVector().normalize();
        double distance = beforeLoc.distance(nowLoc);

        for (double i = 0; i <= distance; i += step) {
            Location loc = beforeLoc.clone().add(dir.clone().multiply(i));
            if (loc.getBlock().isCollidable()) {
                return true;
            }
        }

        return false;
    }
}
