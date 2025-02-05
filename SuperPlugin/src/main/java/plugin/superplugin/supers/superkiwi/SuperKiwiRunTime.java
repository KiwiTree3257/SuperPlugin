package plugin.superplugin.supers.superkiwi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.bossbar.JetBossBar;
import plugin.superplugin.customentity.Star;
import plugin.superplugin.customentity.StarPoop;
import plugin.superplugin.stack.JetGauge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class SuperKiwiRunTime {
    private String supername = "superkiwi";

    SuperPlugin plugin = SuperPlugin.getInstance();
    PotionEffect SPEED;
    PotionEffect JUMP_BOOST;
    int starPoopSpawnRange = 10;
    Random random = new Random();

    public SuperKiwiRunTime() {
        SPEED = new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 20, 0, false, false, false);

        new BukkitRunnable() {
            int starTimer = 0;
            int starTime = (random.nextInt(3 - 1) + 1) * 20;

            @Override
            public void run() {
                starTimer++;
                ArrayList<Player> superKiwiPlayers = Function.GetSuperPlayers(supername);
                for (Player player : superKiwiPlayers) {
                    if (!player.getPersistentDataContainer().has(CustomKeys.SKILL_STOP)) {
                        player.addPotionEffect(SPEED);
                        player.addPotionEffect(JUMP_BOOST);

                        if (starTimer >= starTime) {
                            Location starPoopSpawnLoc = player.getLocation().add(random.nextInt(starPoopSpawnRange) - starPoopSpawnRange / 2, 0, random.nextInt(starPoopSpawnRange) - starPoopSpawnRange / 2);
                            starPoopSpawnLoc = Function.GetHighestLocNear(starPoopSpawnLoc, 5);
                            if (starPoopSpawnLoc != null) {
                                new StarPoop(starPoopSpawnLoc.add(0, 20, 0));
                            }
                        }
                    }
                }

                if (starTimer >= starTime) {
                    starTimer = 0;
                    starTime = (random.nextInt(3 - 1) + 1) * 20;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
