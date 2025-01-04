package plugin.superplugin.customworld.darkworld;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.DarkMonster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DarkWorld {
    private final PotionEffect DARKNESS = new PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0, false, false);
    private final World world = SuperPlugin.darkWorld;
    private Random random = new Random();

    private HashMap<Player, ArrayList<DarkMonster>> playersDarkMonsters = new HashMap<>();

    public DarkWorld() {
        if (world == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> players = world.getPlayers();

                for (Player player : players) {
                    playersDarkMonsters.putIfAbsent(player, null);
                    ArrayList<DarkMonster> darkMonsters = playersDarkMonsters.get(player);
                    if (darkMonsters == null) {
                        playersDarkMonsters.put(player, new ArrayList<>());
                    }

                    playersDarkMonsters.get(player).add(new DarkMonster(player));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int eventRandom = random.nextInt(2);
                            player.playSound(Function.getCircleLocation(3, random.nextInt(360), player.getLocation()), Sound.AMBIENT_CAVE, 1, 1);

                            if (eventRandom == 0) {
                                player.addPotionEffect(DARKNESS);
                            }
                        }
                    }.runTaskLater(SuperPlugin.getInstance(), random.nextInt(12) * 20);
                }
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 20 * 5);
    }
}
