package plugin.superplugin.stack;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.bossbar.JetBossBar;
import plugin.superplugin.bossbar.StarBossBar;

import java.util.HashMap;

public class StarCount {
    public static HashMap<Player, StarBossBar> starBossBars = new HashMap<>();
    public static final int max = 6;

    public static void addPlayer(Player player) {
        StarBossBar starBossBar;
        if (starBossBars.containsKey(player)) {
            starBossBar = starBossBars.get(player);
        }
        else {
            starBossBar = new StarBossBar();
            starBossBar.updateProgress(1);

            starBossBars.put(player, starBossBar);
        }
        starBossBar.addPlayer(player);
    }

    public static void removePlayer(Player player) {
        if (starBossBars.containsKey(player)) {
            starBossBars.get(player).removePlayer(player);
        }
    }

    public static void updateGauge(Player player, int value) {
        if (starBossBars.containsKey(player)) {
            starBossBars.get(player).updateProgress((double) value / max);
        }

        player.getPersistentDataContainer().set(CustomKeys.STAR_POOP_STACK, PersistentDataType.INTEGER, value);
    }

    public static void addCount(Player player, int value) {
        if (getGauge(player) == null) {
            return;
        }

        Integer starPoopStack = player.getPersistentDataContainer().get(CustomKeys.STAR_POOP_STACK, PersistentDataType.INTEGER);
        if (starPoopStack == null)
            starPoopStack = 0;
        starPoopStack += value;

        if (starPoopStack > max) {
            return;
        }

        player.getPersistentDataContainer().set(CustomKeys.STAR_POOP_STACK, PersistentDataType.INTEGER, starPoopStack);
        updateGauge(player, starPoopStack);
    }

    public static StarBossBar getGauge(Player player) {
        return starBossBars.getOrDefault(player, null);
    }
}
