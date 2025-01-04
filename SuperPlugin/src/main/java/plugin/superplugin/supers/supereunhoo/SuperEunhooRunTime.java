package plugin.superplugin.supers.supereunhoo;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;

public class SuperEunhooRunTime {
    private String supername = "supereunhoo";

    SuperPlugin plugin = SuperPlugin.getInstance();
    PotionEffect SPEED;
    PotionEffect JUMP_BOOST;

    public SuperEunhooRunTime() {
        SPEED = new PotionEffect(PotionEffectType.SPEED, 1 * 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 1 * 20, 0, false, false, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> superEunhooPlayers = Function.GetSuperPlayers(supername);

                for (Player player : superEunhooPlayers) {
                    if (player.getPersistentDataContainer().has(CustomKeys.SKILL_STOP)) {
                        player.addPotionEffect(SPEED);
                        player.addPotionEffect(JUMP_BOOST);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
