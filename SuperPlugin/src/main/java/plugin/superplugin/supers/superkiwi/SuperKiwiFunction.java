package plugin.superplugin.supers.superkiwi;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
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
            }
        }
    }
    public static void KiwiSkill_3(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Block targetBlock = player.getTargetBlockExact(10);
            if (targetBlock != null) {
                new Star(targetBlock.getLocation());
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

            if (starPoopStack > 0) {
                if (starPoopStack >= StarCount.max) {
                    //utimate

                    StarCount.updateGauge(player, 0);
                }
                else {
                    //skill
                    StarCount.updateGauge(player, starPoopStack - 1);
                    new StarArrow(player);
                }
            }
        }
    }
}
