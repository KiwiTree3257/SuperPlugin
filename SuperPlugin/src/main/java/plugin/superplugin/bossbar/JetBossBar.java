package plugin.superplugin.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class JetBossBar {
    private BossBar bossBar;

    public JetBossBar() {
        // 보스바 생성
        bossBar = Bukkit.createBossBar(
                "Jet Gauge",
                BarColor.WHITE,
                BarStyle.SOLID
        );
        bossBar.setVisible(true);
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }

    public void updateProgress(double progress) {
        bossBar.setProgress(progress);
    }

    public void updateTitle(String title) {
        bossBar.setTitle(title);
    }

    public void removeBossBar() {
        bossBar.removeAll();
        bossBar = null;
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
