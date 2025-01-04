package plugin.superplugin.supers.superjihwan;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
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
    PotionEffect SLOW_FALLING;

    final double jetPlus = 0.01;
    final double jetMinus = 0.01;
    final double jetAddY = 0.1;
    private HashMap<UUID, Double> playerJetTicks;

    public SuperJihwanRunTime() {
        SPEED = new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 20, 0, false, false, false);
        SLOW_FALLING = new PotionEffect(PotionEffectType.SLOW_FALLING, 5, 0, false, false, false);
        playerJetTicks = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> superJihwanPlayers = Function.GetSuperPlayers(supername);

                for (Player player : superJihwanPlayers) {
                    if (player.getPersistentDataContainer().has(CustomKeys.SKILL_STOP)) {
                        player.addPotionEffect(SPEED);
                        player.addPotionEffect(JUMP_BOOST);

                        if (player.isSneaking() && player.getLocation().clone().add(0, -0.1, 0).getBlock().getType() == Material.AIR) {
                            JetBossBar jetBossBar = JetGauge.getGauge(player);
                            if (jetBossBar != null && jetBossBar.getBossBar().getProgress() > 0) {
                                JetGauge.updateGauge(player, jetBossBar.getBossBar().getProgress() - jetMinus);
                                playerJetTicks.put(player.getUniqueId(), (double) System.currentTimeMillis());

                                player.addPotionEffect(SLOW_FALLING);
                                Vector newVelocity = player.getVelocity();
                                newVelocity.setY(newVelocity.getY() + (newVelocity.getY() + jetAddY < 0 ? jetAddY : 0));
                                player.setVelocity(newVelocity);
                            }
                        }

                        if (playerJetTicks.containsKey(player.getUniqueId()) && System.currentTimeMillis() - playerJetTicks.get(player.getUniqueId()) > 10000) {
                            JetBossBar jetBossBar = JetGauge.getGauge(player);
                            if (jetBossBar != null) {
                                JetGauge.updateGauge(player, jetBossBar.getBossBar().getProgress() + jetPlus);
                            }
                        }
                    }

                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
