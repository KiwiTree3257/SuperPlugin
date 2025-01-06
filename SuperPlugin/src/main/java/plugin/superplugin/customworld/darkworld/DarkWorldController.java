package plugin.superplugin.customworld.darkworld;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.DarkMonster;
import plugin.superplugin.customentity.OHH;

import java.util.*;

public class DarkWorldController {
    private final PotionEffect DARKNESS = new PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0, false, false);
    private final World world = SuperPlugin.darkWorld;
    private Random random = new Random();

    private HashMap<Player, ArrayList<DarkMonster>> playersDarkMonsters = new HashMap<>();

    public DarkWorldController() {
        if (world == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> players = world.getPlayers();

                for (Player player : players) {
                    if (Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), "supereunhoo")) {
                        continue;
                    }

                    playersDarkMonsters.putIfAbsent(player, null);
                    ArrayList<DarkMonster> darkMonsters = playersDarkMonsters.get(player);
                    if (darkMonsters == null) {
                        playersDarkMonsters.put(player, new ArrayList<>());
                    }

                    playersDarkMonsters.get(player).add(new DarkMonster(player));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int eventRandom = random.nextInt(3);

                            if (eventRandom == 0) {
                                new OHH(player);
                            }
                            else {
                                player.playSound(Function.getCircleLocation(3, random.nextInt(360), player.getLocation()), Sound.AMBIENT_CAVE, 1, 1);
                                player.addPotionEffect(DARKNESS);
                            }
                        }
                    }.runTaskLater(SuperPlugin.getInstance(), random.nextInt(5) * 20);
                }
            }
        }.runTaskTimer(SuperPlugin.getInstance(), 0, 20 * 10);
    }

    public void AllEntityRemove() {
        List<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            entity.remove();
        }
    }
}
