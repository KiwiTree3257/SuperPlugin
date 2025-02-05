package plugin.superplugin.supers.superkiwi;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.bossbar.StarBossBar;
import plugin.superplugin.customentity.*;
import plugin.superplugin.stack.StarCount;

import java.util.*;

public class SuperKiwiFunction {
    private static String supername = "superkiwi";
    public static HashMap<UUID, Double> entityWormHoleCoolTime = new HashMap<>();

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
            if (36 - playerItemCount >= SuperKiwiItem.addItems.length) {
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
        playerInventory.addItem(SuperKiwiItem.addItems);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        StarCount.addPlayer(player);
        StarCount.updateGauge(player, 0);

        player.sendMessage("변신!");
    }

    public static void Untransformed(Player player) {
        Inventory playerInventory = player.getInventory();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        playerData.remove(CustomKeys.Player_Super);
        for (int i = 0; i < SuperKiwiItem.removeItems.length; i++) {
            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperKiwiItem.removeItems[i], PersistentDataType.INTEGER);
            for (int index : indexes) {
                if (playerInventory.getItem(index) == null)
                    continue;
                playerInventory.remove(playerInventory.getItem(index));
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            for (int i = 0; i < SuperKiwiItem.removeItems.length; i++) {
                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), SuperKiwiItem.removeItems[i], PersistentDataType.INTEGER)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        StarCount.removePlayer(player);

        player.sendMessage("변신 해제!");
    }

    public static void KiwiSkill_1(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Inventory playerInventory = player.getInventory();
            for (int i = 0; i < playerInventory.getSize(); i++) {
                if (playerInventory.getItem(i) == null)
                    continue;

                if (playerInventory.getItem(i).equals(SuperKiwiItem.SUPER_KIWI_SKILL_1)) {
                    playerInventory.setItem(i, SuperKiwiItem.WORMHOLE);
                    break;
                }
            }
        }
    }
    public static void KiwiSkill_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Block targetBlock = player.getTargetBlockExact(20);
            if (targetBlock != null) {
                new BlackHole(player.getUniqueId(), targetBlock.getLocation());
                CoolTimeManager.SetCoolTime(player, supername, 2, delay);
            }
        }
    }
    public static void KiwiSkill_3(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Block targetBlock = player.getTargetBlockExact(10);
            if (targetBlock != null) {
                new Star(targetBlock.getLocation());
                CoolTimeManager.SetCoolTime(player, supername, 3, delay);
            }
        }
    }

    public static void KiwiUltimate(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Integer starPoopStack = playerData.get(CustomKeys.STAR_POOP_STACK, PersistentDataType.INTEGER);
            if (starPoopStack == null) {
                return;
            }
            World world = player.getWorld();

            if (starPoopStack > 0) {
                if (starPoopStack >= StarCount.max) {
                    //utimate

                    Location skillLoc = player.getLocation().clone().add(0, 20, 0);
                    Location[] starParticleLocations = new Location[5];
                    for (int i = 0; i < starParticleLocations.length; i++) {
                        starParticleLocations[i] = Function.getCircleLocation(5, 72 * i, skillLoc);
                    }
                    StarCount.updateGauge(player, 0);
                    new BukkitRunnable() {///////////발동
                        int timer = 0;
                        int starPoint = 0;

                        @Override
                        public void run() {
                            if (timer % 10 == 0) {
                                if (starParticleLocations.length >= starPoint) {
                                    for (int i = 0; i < starPoint; i++) {
                                        Function.LocToLocParticle(starParticleLocations[i], starParticleLocations[(i + 2) % starParticleLocations.length], 0.2, Particle.END_ROD, 1, new Vector(0, 0, 0), 0);
                                    }
                                    world.playSound(skillLoc, Sound.BLOCK_END_PORTAL_FRAME_FILL, 3, 1);

                                    starPoint++;
                                }
                                else {
                                    timer = 0;
                                    starPoint = starParticleLocations.length;
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (timer >= 5 * 20) {
                                                cancel();
                                            }

                                            Block targetBlock = player.getTargetBlockExact(100);

                                            for (int i = 0; i < starPoint; i++) {
                                                Function.LocToLocParticle(starParticleLocations[i], starParticleLocations[(i + 2) % starParticleLocations.length], 0.2, Particle.END_ROD, 1, new Vector(0, 0, 0), 0);
                                            }
                                            if (targetBlock != null) {
                                                Function.LocToLocParticle(skillLoc, targetBlock.getLocation(), 0.5, Particle.END_ROD, 1, new Vector(0.5, 0.5, 0.5), 0);
                                                Function.LocToLocParticle(skillLoc, targetBlock.getLocation(), 0.2, Particle.GLOW, 1, new Vector(0.1, 0.1, 0.1), 0);
                                                if (timer % 5 == 0) {
                                                    world.spawnParticle(Particle.EXPLOSION, targetBlock.getLocation(), 1, 1, 1, 1, 0);

                                                    Vector dir = skillLoc.clone().subtract(targetBlock.getLocation()).toVector().normalize();
                                                    double distance = targetBlock.getLocation().distance(skillLoc);
                                                    for (double i = 0; i < distance; i += 1) {
                                                        ArrayList<LivingEntity> entities = new ArrayList<>(targetBlock.getLocation().clone().add(dir.clone().multiply(i)).getNearbyLivingEntities(2));
                                                        for (LivingEntity entity : entities) {
                                                            if (entity.getUniqueId().equals(player.getUniqueId())) {
                                                                continue;
                                                            }
                                                            entity.damage(2);
                                                        }
                                                    }
                                                }
                                                world.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                            }

                                            CoolTimeManager.SetCoolTime(player, supername, 4, 2 * 20);
                                            timer++;
                                        }
                                    }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);

                                    cancel();
                                }
                            }

                            CoolTimeManager.SetCoolTime(player, supername, 4, delay);
                            timer++;
                        }
                    }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
                }
                else {
                    //skill
                    StarCount.updateGauge(player, starPoopStack - 1);
                    new StarArrow(player);
                    CoolTimeManager.SetCoolTime(player, supername, 4, 2 * 20);
                }
            }
        }
    }
}
