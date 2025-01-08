package plugin.superplugin.supers.superjunu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SuperJunuRunTime {
    private String supername = "superjunu";
    SuperPlugin plugin = SuperPlugin.getInstance();
    PotionEffect FIRE_RESISTANCE;
    PotionEffect SPEED;
    PotionEffect JUMP_BOOST;
    HashMap<UUID, Integer> playerFireTicks;

    public SuperJunuRunTime() {
        FIRE_RESISTANCE = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1 * 20, 4, false, false, false);
        SPEED = new PotionEffect(PotionEffectType.SPEED, 1 * 20, 1, false, false, false);
        JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 1 * 20, 0, false, false, false);

        playerFireTicks = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> superJunuPlayers = Function.GetSuperPlayers(supername);

                for (Player player : superJunuPlayers) {
                    if (!player.getPersistentDataContainer().has(CustomKeys.SKILL_STOP)) {
                        player.setFireTicks(0);
                        player.addPotionEffect(FIRE_RESISTANCE);
                        player.addPotionEffect(SPEED);
                        player.addPotionEffect(JUMP_BOOST);
                        if (player.getLocation().getBlock().getType() == Material.LAVA) {
                            if (playerFireTicks.containsKey(player.getUniqueId())) {
                                playerFireTicks.put(player.getUniqueId(), playerFireTicks.get(player.getUniqueId()) + 1);

                                if (playerFireTicks.get(player.getUniqueId()) >= 20 * 2) {
                                    player.heal(2);
                                    playerFireTicks.remove(player.getUniqueId());
                                }
                            }
                            else {
                                playerFireTicks.put(player.getUniqueId(), 1);
                            }
                        }
                        else {
                            if (playerFireTicks.containsKey(player.getUniqueId())) {
                                playerFireTicks.remove(player.getUniqueId());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
