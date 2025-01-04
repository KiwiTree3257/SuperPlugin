package plugin.superplugin.customentity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.memory.MemoryKey;
import plugin.superplugin.Function;

import java.util.Random;

public class DarkMonster {
    Zombie darkMonster;
    Player target;
    World world;
    double radius = 5;

    public DarkMonster(Player player) {
        Random random = new Random();
        target = player;
        world = player.getWorld();
        int degree = random.nextInt(360);
        Location spawnLoc = Function.getCircleLocation(radius, degree, player.getLocation());
        for (int i = 0; i < 360; i += 2) {
            if (!spawnLoc.getBlock().isCollidable()) {
                break;
            }
            else {
                spawnLoc = Function.getCircleLocation(radius, degree + i, player.getLocation());
            }
        }

        darkMonster = (Zombie) world.spawnEntity(spawnLoc, EntityType.ZOMBIE);
        darkMonster.setBaby();
        darkMonster.clearLootTable();
        darkMonster.setMemory(MemoryKey.ANGRY_AT, player.getUniqueId());
    }
}
