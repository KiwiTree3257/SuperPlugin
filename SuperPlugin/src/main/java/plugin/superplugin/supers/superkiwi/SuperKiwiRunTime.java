package plugin.superplugin.supers.superkiwi;

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

public class SuperKiwiRunTime {
    private String supername = "superkiwi";

    SuperPlugin plugin = SuperPlugin.getInstance();
    PotionEffect SPEED;
    PotionEffect JUMP_BOOST;

    public SuperKiwiRunTime() {
        SPEED = new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 20, 0, false, false, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> superKiwiPlayers = Function.GetSuperPlayers(supername);

                for (Player player : superKiwiPlayers) {
                    if (!player.getPersistentDataContainer().has(CustomKeys.SKILL_STOP)) {
                        player.addPotionEffect(SPEED);
                        player.addPotionEffect(JUMP_BOOST);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
