package plugin.superplugin.supers.supersihoo;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.IceArrow;
import plugin.superplugin.customentity.IceSpike;
import plugin.superplugin.stack.FreezeStack;

import java.util.*;

public class SuperSihooFunction {
    private static String supername = "supersihoo";

    public static void SuperTransformation(Player player) {
        Inventory playerInventory = player.getInventory();
        ItemStack[] playerItemStacks = playerInventory.getStorageContents();
        int playerItemCount = 0;

        for (int i = 0; i < playerItemStacks.length; i++) {
            if (playerItemStacks[i] != null)
                playerItemCount++;
        }
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (playerData.has(CustomKeys.Player_Super)) {
            if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
                Untransformed(player);
            }
            else {
                player.sendMessage("변신 실패.");
            }
        }
        else {
            if (36 - playerItemCount >= SuperSihooItem.addItems.length) {
                playerData.set(CustomKeys.Player_Super, PersistentDataType.STRING, supername);
                Transformation(player);
            }
            else {
                player.sendMessage("변신을 위한 인벤토리 공간이 부족합니다.");
            }
        }
    }

    public static void Transformation(Player player) {
        Inventory playerInventory = player.getInventory();
        playerInventory.addItem(SuperSihooItem.addItems);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        player.sendMessage("변신!");
    }

    public static void Untransformed(Player player) {
        Inventory playerInventory = player.getInventory();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        playerData.remove(CustomKeys.Player_Super);
        for (int i = 0; i < SuperSihooItem.removeItems.length; i++) {
            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperSihooItem.removeItems[i], PersistentDataType.INTEGER);
            for (int index : indexes) {
                if (playerInventory.getItem(index) == null)
                    continue;
                playerInventory.remove(playerInventory.getItem(index));
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            for (int i = 0; i < SuperSihooItem.removeItems.length; i++) {
                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), SuperSihooItem.removeItems[i], PersistentDataType.INTEGER)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        player.sendMessage("변신 해제!");
    }

    public static void SihooSkill_1(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            ArrayList<Location> spawnLoc = new ArrayList<>();
            World world = player.getWorld();

            new BukkitRunnable() {
                int timer = 0;
                @Override
                public void run() {
                    if (timer >= 30) {
                        new BukkitRunnable() {
                            int timer = 0;
                            @Override
                            public void run() {
                                if (timer >= spawnLoc.size()) {
                                    cancel();
                                    return;
                                }

                                Location loc = spawnLoc.get(timer).clone();
                                loc.add(player.getLocation());

                                Block targetBlock = player.getTargetBlockExact(100);
                                if (targetBlock != null) {
                                    loc = Function.setLookAt(loc, targetBlock.getLocation());
                                }
                                else {
                                    loc.setDirection(player.getLocation().getDirection());
                                }

                                new IceArrow(player, loc);

                                CoolTimeManager.SetCoolTime(player, supername, 1, delay);
                                timer++;
                            }
                        }.runTaskTimer(SuperPlugin.getInstance(), 10, 3);

                        cancel();
                    }

                    Location randLoc = Function.RandomSphereLocation(2, new Location(world, 0 , 5 , 0));
                    spawnLoc.add(randLoc);
                    world.playSound(randLoc.clone().add(player.getLocation()), Sound.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, 1, 1);

                    for (int i = 0; i < spawnLoc.size(); i++) {
                        Location loc = spawnLoc.get(i).clone();
                        loc.add(player.getLocation());

                        world.spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0 , 0);
                    }

                    CoolTimeManager.SetCoolTime(player, supername, 1, delay);
                    timer++;
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }
    }
    public static void SihooSkill_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Location startLoc = player.getLocation().clone();
            startLoc.setPitch(0);
            Vector dir = startLoc.clone().getDirection().normalize();
            startLoc.add(dir.clone().multiply(3));
            Vector left = dir.clone().crossProduct(new Vector(0, 1, 0)).normalize();
            float playerYaw = player.getYaw();

            for (int j = -10; j <= 10; j++) {
                Location location = startLoc.clone();
                location.add(left.clone().multiply(j));

                Location highest = Function.GetHighestLocNear(location, 3);
                if (highest != null) {
                    location.setY(highest.getY());
                }
                else {
                    continue;
                }

                for (int i = 0; i < 3; i++) {
                    new IceSpike(playerYaw - 45, playerYaw + 45, -45, -90, location);
                }
                CoolTimeManager.SetCoolTime(player, supername, 2, delay);
            }
        }
    }
    public static void SihooSkill_3(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            player.setVelocity(new Vector(0, 2, 0));
            PotionEffect SLOW_FALLING = new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 4, false, false, false);
            World world = player.getWorld();
            int iceLineCount = 8;
            float angle = (float) 360 / iceLineCount;

            for (int i = 0; i < iceLineCount; i++) {
                Location startLoc = player.getLocation();
                startLoc.setYaw(angle * i);
                startLoc.setPitch(0);
                Vector dir = startLoc.getDirection().normalize();
                for (int j = 0; j < 5; j++) {
                    startLoc.add(dir);
                    startLoc = Function.GetHighestLocNear(startLoc, 2);
                    if (startLoc == null)
                        break;

                    startLoc.getBlock().setType(Material.BLUE_ICE);
                }
            }

            new BukkitRunnable() {
                int timer = 0;

                @Override
                public void run() {

                    if (timer >= 10 * 20 || (player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.AIR && timer >= 20)) {
                        cancel();
                        return;
                    }

                    player.addPotionEffect(SLOW_FALLING);
                    world.spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 5, 0, 0, 0, 0.1);
                    CoolTimeManager.SetCoolTime(player, supername, 3, delay);

                    timer++;
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }
    }

    public static void SihooUltimate(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            int radius = 20;
            Location bottomCenter = player.getLocation();
            bottomCenter = Function.GetHighestLocNear(bottomCenter, radius);

            if (bottomCenter == null) {
                return;
            }

            double bottomY = player.getLocation().getY() - bottomCenter.getY();

            int circleRadius = (int) Math.ceil(Math.sqrt((radius * radius) - (bottomY * bottomY)));
            int sampleCountSphere = 200;
            int sampleCountCircle = 360;
            int placeTime = 20 * 3;
            int skillTime = 15;
            int lanternCount = 30;

            Location startLoc = player.getLocation();
            World world = player.getWorld();
            Location center = player.getLocation();


            ArrayList<Location> sphereLocations = new ArrayList<>();
            ArrayList<Location> circleLocations = new ArrayList<>();

            for (int i = 0; i <= sampleCountSphere; i++) {
                // pitch: 위에서 아래로 (0°~180°)
                double pitch = Math.PI * (i / (double) sampleCountSphere);

                for (int j = 0; j <= sampleCountSphere; j++) {
                    // yaw: 수평 회전 (-180°~180°)
                    double yaw = 2 * Math.PI * (j / (double) sampleCountSphere);

                    // 구 좌표 계산
                    double x = radius * Math.sin(pitch) * Math.cos(yaw);
                    double y = radius * Math.cos(pitch);
                    double z = radius * Math.sin(pitch) * Math.sin(yaw);

                    // 중심 위치에 좌표 추가
                    Location blockLocation = center.clone().add(x, y, z);

                    // 블록 좌표로 정수화
                    blockLocation.setX(blockLocation.getBlockX());
                    blockLocation.setY(blockLocation.getBlockY());
                    blockLocation.setZ(blockLocation.getBlockZ());

                    // 중복된 좌표 방지
                    if (!sphereLocations.contains(blockLocation) && !blockLocation.getBlock().isCollidable()) {
                        sphereLocations.add(blockLocation);
                    }
                }
            }
            for (int i = 0; i <= sampleCountCircle; i++) {
                float yaw = (float) (360 / sampleCountCircle) * i;
                Location circleLoc = bottomCenter.clone();
                circleLoc.setPitch(0);
                circleLoc.setYaw(yaw);

                for (int j = 0; j < circleRadius; j++) {
                    Location snowLoc = circleLoc.toBlockLocation();
                    if (!circleLocations.contains(snowLoc)) {
                        Location highestSnowLoc = Function.GetHighestLocNear(snowLoc, 5);
                        if (highestSnowLoc == null || highestSnowLoc.getBlock().getType() == Material.BLUE_ICE) {
                            continue;
                        }

                        circleLoc.setY(highestSnowLoc.getY());
                        circleLocations.add(highestSnowLoc);
                    }

                    circleLoc.add(circleLoc.getDirection().normalize());
                }
            }

            new BukkitRunnable() {
                ArrayList<Location> sphereClone = (ArrayList<Location>) sphereLocations.clone();
                ArrayList<Location> circleClone = (ArrayList<Location>) circleLocations.clone();
                Random random = new Random();

                @Override
                public void run() {
                    if (sphereClone.isEmpty() && circleClone.isEmpty()) {
                        cancel();

                        for (int i = 0; i < lanternCount; i++) {
                            Location randLoc = Function.RandomSphereLocation(radius, center);
                            if (randLoc.getBlock().getType() != Material.AIR) {
                                continue;
                            }

                            randLoc.getBlock().setType(Material.SEA_LANTERN);
                        }

                        new BukkitRunnable() {
                            int timer = 0;

                            @Override
                            public void run() {
                                if (timer >= skillTime) {
                                    cancel();

                                    sphereClone = (ArrayList<Location>) sphereLocations.clone();

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (sphereClone.isEmpty()) {
                                                cancel();
                                            }

                                            for (int i = 0; i < sphereLocations.size() / placeTime; i++) {
                                                if (sphereClone.isEmpty()) {
                                                    break;
                                                }

                                                int randIndex = random.nextInt(sphereClone.size());

                                                Location randLoc = sphereClone.get(randIndex);
                                                sphereClone.remove(randIndex);

                                                randLoc.getBlock().setType(Material.AIR);
                                            }
                                            CoolTimeManager.SetCoolTime(player, supername, 4, delay);
                                        }
                                    }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
                                }

                                ArrayList<LivingEntity> entities = (ArrayList<LivingEntity>) world.getNearbyLivingEntities(startLoc, radius);
                                for (LivingEntity entity : entities) {
                                    if (entity.getUniqueId().equals(player.getUniqueId())) {
                                        continue;
                                    }

                                    FreezeStack.FreezeEntity(entity);
                                }

                                timer++;
                                CoolTimeManager.SetCoolTime(player, supername, 4, delay);
                            }
                        }.runTaskTimer(SuperPlugin.getInstance(), 0, 20);
                    }

                    for (int i = 0; i < sphereLocations.size() / placeTime; i++) {
                        if (sphereClone.isEmpty()) {
                            break;
                        }

                        int randIndex = random.nextInt(sphereClone.size());

                        Location randLoc = sphereClone.get(randIndex);
                        sphereClone.remove(randIndex);

                        randLoc.getBlock().setType(Material.BLUE_ICE);
                        world.spawnParticle(Particle.SNOWFLAKE, randLoc, 5, 0, 0, 0, 0.1);
                    }

                    for (int i = 0; i < circleLocations.size() / placeTime; i++) {
                        if (circleClone.isEmpty()) {
                            break;
                        }

                        int randIndex = random.nextInt(circleClone.size());

                        Location randLoc = circleClone.get(randIndex);
                        circleClone.remove(randIndex);

                        randLoc.getBlock().setType(Material.SNOW_BLOCK);
                    }

                    CoolTimeManager.SetCoolTime(player, supername, 4, delay);
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }
    }
}
