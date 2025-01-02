package plugin.superplugin.supers.superjunu;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
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
import plugin.superplugin.customentity.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SuperJunuFunction {
    private static String supername = "superjunu";

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
            if (36 - playerItemCount >= SuperJunuItem.addItems.length) {
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
        playerInventory.addItem(SuperJunuItem.addItems);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        player.sendMessage("변신!");
    }

    public static void Untransformed(Player player) {
        Inventory playerInventory = player.getInventory();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        playerData.remove(CustomKeys.Player_Super);
        for (int i = 0; i < SuperJunuItem.removeItems.length; i++) {
            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperJunuItem.removeItems[i], PersistentDataType.INTEGER);
            for (int index : indexes) {
                if (playerInventory.getItem(index) == null)
                    continue;
                playerInventory.remove(playerInventory.getItem(index));
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            for (int i = 0; i < SuperJunuItem.removeItems.length; i++) {
                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), SuperJunuItem.removeItems[i], PersistentDataType.INTEGER)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        player.sendMessage("변신 해제!");
    }

    public static void JunuSkill_1(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            new FireStorm(player);
            CoolTimeManager.SetCoolTime(player, supername, 1, delay);
        }
    }
    public static void JunuSkill_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            World world = player.getWorld();
            ArrayList<LivingEntity> entities = new ArrayList<>(player.getLocation().getNearbyLivingEntities(20));
            entities.remove(player);
            Random random = new Random();
            int bandong = 4;

            if (!entities.isEmpty()) {
                world.playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY_LAVA, 1, 1);

                for (int i = 0; i < 10; i++) {
                    Location endLoc;
                    if (entities.size() <= i) {
                        endLoc = entities.get(i % entities.size()).getLocation();
                    }
                    else {
                        endLoc = entities.get(i).getLocation();
                    }

                    endLoc.add(random.nextDouble() * bandong - bandong / 2f, 0, random.nextDouble() * bandong - bandong / 2f);
                    endLoc = Function.GetHighestLocNear(endLoc, 5);
                    if (endLoc == null)
                        continue;
                    Location startLoc = Function.setLookAt(player.getLocation(), endLoc);
                    startLoc.add(startLoc.getDirection().normalize().multiply(2));
                    startLoc = Function.GetHighestLocNear(startLoc, 5);
                    if (startLoc == null)
                        continue;

                    Location spawnLoc = startLoc.clone().add(0, 2, 0);


                    new FallingLava(player, spawnLoc, endLoc, 7, 30);

                    startLoc.getBlock().setType(Material.MAGMA_BLOCK);
                    world.spawnParticle(Particle.LAVA, spawnLoc, 20);
                    CoolTimeManager.SetCoolTime(player, supername, 2, delay);
                }
            }
        }
    }
    public static void JunuSkill_3(Player player, ItemStack item) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Inventory playerInventory = player.getInventory();
            for (int i = 0; i < playerInventory.getSize(); i++) {
                if (playerInventory.getItem(i) == null)
                    continue;

                if (playerInventory.getItem(i).equals(item)) {
                    playerInventory.setItem(i, SuperJunuItem.FIRE_ARROW);
                    break;
                }
            }
        }
    }

    public static void JunuSkill_4(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            new FireExplosion(player);
            CoolTimeManager.SetCoolTime(player, supername, 4, delay);
        }
    }

    public static void JunuSkill_5(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Block endBlock = player.getTargetBlockExact(50);
            if (endBlock != null) {
                new FallingMeteo(player, player.getLocation().add(0, 20, 0), endBlock.getLocation());
                CoolTimeManager.SetCoolTime(player, supername, 5, delay);
            }
        }
    }

    public static void JunuSkill_6(Player player, Block targetBlock, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            if (targetBlock != null) {
                SuperPlugin plugin = SuperPlugin.getInstance();
                int totalTime = 10;
                double peakHeight = 5;
                Location startLocation = player.getLocation().clone();
                Location endLocation = targetBlock.getLocation();
                World world = player.getWorld();

                // x, z축에서의 선형 거리 및 이동 속도
                Vector directionXZ = endLocation.clone().subtract(startLocation).toVector();
                directionXZ.setY(0); // x, z 방향만 남기기
                double distanceXZ = directionXZ.length();
                directionXZ.normalize(); // 단위 벡터로 만들기

                // y축에서의 최고점 계산 (최대 높이와 현재 위치 기반)
                double startY = startLocation.getY();
                double endY = endLocation.getY();
                double maxY = Math.max(startY, endY); // 최고점 y 좌표

                double incrementXZ = distanceXZ / totalTime;

                world.playSound(startLocation, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 1);

                new BukkitRunnable() {
                    int timer = 0;

                    @Override
                    public void run() {
                        Location playerLoc = player.getLocation().clone();

                        double progress = (double) timer / totalTime;
                        double nextProgress = (double) (timer + 1) / totalTime;
                        double y = 0;
                        double nextY = 0;
                        if (progress < 0.5) {
                            y = maxY + peakHeight * (1 - Math.pow(1 - progress * 2, 2));
                            nextY = maxY + peakHeight * (1 - Math.pow(1 - nextProgress * 2, 2));
                        } else {
                            y = maxY + peakHeight * (1 - Math.pow((progress - 0.5) * 2, 2));
                            nextY = maxY + peakHeight * (1 - Math.pow((nextProgress - 0.5) * 2, 2));
                        }

                        double x = startLocation.getX() + directionXZ.getX() * incrementXZ * timer;
                        double z = startLocation.getZ() + directionXZ.getZ() * incrementXZ * timer;
                        double nextX = startLocation.getX() + directionXZ.getX() * incrementXZ * timer + 1;
                        double nextZ = startLocation.getZ() + directionXZ.getZ() * incrementXZ * timer + 1;
                        Location newLocation = new Location(world, x, y, z);
                        Location nextNewLocation = new Location(world, nextX, nextY, nextZ);
                        newLocation.setDirection(player.getLocation().getDirection());

                        if (nextNewLocation.getBlock().getType() != Material.AIR) {
                            Location highestLoc = Function.GetHighestLocNear(playerLoc, 10);
                            if (highestLoc != null) {
                                highestLoc.add(0, 1, 0);
                                world.playSound(highestLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                world.spawnParticle(Particle.FLAME, highestLoc, 50, 0.5, 0.5, 0.5, 0.4);
                                ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(playerLoc.getNearbyLivingEntities(4));
                                PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 2 * 20, 3, false, false, false);
                                for (LivingEntity entity : nearbyLivingEntities) {
                                    if (entity.getUniqueId() == player.getUniqueId())
                                        continue;

                                    entity.addPotionEffect(SLOWNESS);
                                    entity.damage(2);
                                }
                            }

                            player.setFallDistance(0);
                            cancel();
                            return;
                        }

                        world.spawnParticle(Particle.FLAME, newLocation, 10, 0, 0, 0, 0.1);

                        player.teleport(newLocation);
                        CoolTimeManager.SetCoolTime(player, supername, 6, delay);
                        timer++;
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }

    public static void JunuUltimate(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            int skillTime = 20 * 20;
            ItemStack[] setArmorContents = SuperJunuItem.armorItems.clone();
            ItemStack[] armorContents = player.getInventory().getArmorContents();
            for (int i = 0; i < armorContents.length; i++) {
                if (armorContents[i] != null && armorContents[i].getItemMeta().getPersistentDataContainer().has(CustomKeys.JUNU_ARMOR)) {
                    armorContents[i] = null;
                }
            }
            ItemStack[] itemstack_1 = SuperJunuItem.addItems.clone();
            ItemStack[] itemstack_2 = SuperJunuItem.addUltimateItems.clone();
            Inventory playerInventory = player.getInventory();

            player.getInventory().setArmorContents(setArmorContents);

            Particle.DustOptions redDust = new Particle.DustOptions(Color.RED, 3);
            player.getWorld().spawnParticle(Particle.DUST, player.getLocation(), 100, 1, 1, 1, 0, redDust);
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 50, 0, 0, 0, 0.4);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 3, 1);

            for (int i = 0; i < playerInventory.getSize(); i++) {
                if (playerInventory.getItem(i) == null)
                    continue;

                if (Function.CompareItemPersistentData(playerInventory.getItem(i), SuperJunuItem.FIRE_ARROW, PersistentDataType.INTEGER)) {
                    playerInventory.setItem(i, SuperJunuItem.SUPER_JUNU_SKILL_3);
                    break;
                }
            }//firearrow

            for (int i = 0; i < itemstack_1.length; i++) {
                int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, itemstack_1[i], PersistentDataType.INTEGER);
                for (int index : indexes) {
                    if (playerInventory.getItem(index) == null)
                        continue;

                    playerInventory.setItem(index, itemstack_2[i]);
                }
            }

            new BukkitRunnable() {
                int timer = 0;

                @Override
                public void run() {
                    if (timer >= skillTime || !Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername) || playerData.has(CustomKeys.FIRE_RIDER)) {
                        player.getInventory().setArmorContents(armorContents);
                        for (int i = 0; i < setArmorContents.length; i++) {
                            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, setArmorContents[i], PersistentDataType.INTEGER);
                            for (int index : indexes) {
                                if (playerInventory.getItem(index) == null)
                                    continue;

                                playerInventory.remove(playerInventory.getItem(index));
                            }
                        }
                        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
                            for (int i = 0; i < setArmorContents.length; i++) {
                                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), setArmorContents[i], PersistentDataType.INTEGER)) {
                                    player.getInventory().setItemInOffHand(null);
                                }
                            }
                        }

                        for (int i = 0; i < itemstack_2.length; i++) {
                            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, itemstack_2[i], PersistentDataType.INTEGER);
                            for (int index : indexes) {
                                if (playerInventory.getItem(index) == null)
                                    continue;

                                playerInventory.setItem(index, itemstack_1[i]);
                            }
                        }

                        playerData.remove(CustomKeys.FIRE_RIDER);

                        cancel();
                        return;
                    }

                    CoolTimeManager.SetCoolTime(player, supername, 7, delay);
                    timer++;
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }
    }

    public static void JunuUltimate_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            new FireDragon(player, player.getLocation());
            playerData.set(CustomKeys.FIRE_RIDER, PersistentDataType.BOOLEAN, true);

            CoolTimeManager.SetCoolTime(player, supername, 8, delay);
        }
    }
}
