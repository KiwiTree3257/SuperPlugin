package plugin.superplugin.stack;

import org.bukkit.entity.Player;
import plugin.superplugin.bossbar.JetBossBar;

import java.util.HashMap;

public class JetGauge {
    public static HashMap<Player, JetBossBar> jetBossBars = new HashMap<>();

    public static void addPlayer(Player player) {
        JetBossBar jetBossBar;
        if (jetBossBars.containsKey(player)) {
            jetBossBar = jetBossBars.get(player);
        }
        else {
            jetBossBar = new JetBossBar();
            jetBossBar.updateProgress(1);

            jetBossBars.put(player, jetBossBar);
        }
        jetBossBar.addPlayer(player);
    }

    public static void removePlayer(Player player) {
        if (jetBossBars.containsKey(player)) {
            jetBossBars.get(player).removePlayer(player);
        }
    }

    public static void updateGauge(Player player, double value) {
        if (jetBossBars.containsKey(player)) {
            if (value > 1) {
                jetBossBars.get(player).updateProgress(1);
            }
            else if (value < 0) {
                jetBossBars.get(player).updateProgress(0);
            }
            else {
                jetBossBars.get(player).updateProgress(value);
            }
        }
    }

    public static JetBossBar getGauge(Player player) {
        return jetBossBars.getOrDefault(player, null);
    }
}
