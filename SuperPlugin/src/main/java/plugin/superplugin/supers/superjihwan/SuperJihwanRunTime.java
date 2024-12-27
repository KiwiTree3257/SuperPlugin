package plugin.superplugin.supers.superjihwan;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.bossbar.JetBossBar;
import plugin.superplugin.stack.JetGauge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SuperJihwanRunTime {
    private String supername = "superjihwan";

    SuperPlugin plugin = SuperPlugin.getInstance();
    PotionEffect SPEED;
    PotionEffect JUMP_BOOST;

    final double jetPlus = 0.01;
    final double jetMinus = 0.01;
    HashMap<UUID, Double> playerJetTicks;

    public SuperJihwanRunTime() {
        SPEED = new PotionEffect(PotionEffectType.SPEED, 1 * 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 1 * 20, 0, false, false, false);
        playerJetTicks = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> superJihwanPlayers = Function.GetSuperPlayers(supername);

                for (Player player : superJihwanPlayers) {
                    player.addPotionEffect(SPEED);
                    player.addPotionEffect(JUMP_BOOST);

                    if (player.isSneaking() && player.getLocation().clone().add(0, -0.1, 0).getBlock().getType() == Material.AIR) {
                        JetBossBar jetBossBar = JetGauge.getGauge(player);
                        if (jetBossBar != null && jetBossBar.getBossBar().getProgress() - jetMinus > 0) {
                            JetGauge.updateGauge(player, jetBossBar.getBossBar().getProgress() - jetMinus);
                            playerJetTicks.put(player.getUniqueId(), (double) System.currentTimeMillis());
                        }
                    }

                    if (playerJetTicks.containsKey(player.getUniqueId()) && System.currentTimeMillis() - playerJetTicks.get(player.getUniqueId()) > 1000) {
                        JetBossBar jetBossBar = JetGauge.getGauge(player);
                        if (jetBossBar != null) {
                            if (jetBossBar.getBossBar().getProgress() + jetPlus < 1) {
                                JetGauge.updateGauge(player, jetBossBar.getBossBar().getProgress() + jetPlus);
                            }
                            else {
                                JetGauge.updateGauge(player, 1);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
