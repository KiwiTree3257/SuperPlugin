package plugin.superplugin;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class CoolTimeManager {
    public static HashMap<Player, HashMap<String, HashMap<Integer, Double>>> playerCoolTimes;

    public CoolTimeManager() {
        playerCoolTimes = new HashMap<>();
    }

    public static void SetCoolTime(Player player, String superName, int superNumber, int delayTick) {
        double coolTime = System.currentTimeMillis() + ((double) delayTick / 20 * 1000L);
        playerCoolTimes.putIfAbsent(player, new HashMap<>());
        playerCoolTimes.get(player).putIfAbsent(superName, new HashMap<>());
        playerCoolTimes.get(player).get(superName).put(superNumber, coolTime);
    }

    public static Double GetCoolTime(Player player, String superName, int superNumber) {
        if (playerCoolTimes.containsKey(player) && playerCoolTimes.get(player).containsKey(superName) && playerCoolTimes.get(player).get(superName).containsKey(superNumber)) {
            return (double) Math.round((playerCoolTimes.get(player).get(superName).get(superNumber) - System.currentTimeMillis()) / 10) / 100;
        }
        return null;
    }

    public static boolean CheckCoolTime(Player player, String superName, int superNumber) {
        if (!playerCoolTimes.containsKey(player) || !playerCoolTimes.get(player).containsKey(superName) || !playerCoolTimes.get(player).get(superName).containsKey(superNumber) ||
            playerCoolTimes.get(player).get(superName).get(superNumber) < System.currentTimeMillis()) {
            return true;
        }

        return false;
    }
}
