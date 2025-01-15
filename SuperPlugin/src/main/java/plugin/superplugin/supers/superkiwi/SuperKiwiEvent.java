package plugin.superplugin.supers.superkiwi;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.FireArrow;
import plugin.superplugin.customentity.WormHole;
import plugin.superplugin.supers.superjunu.SuperJunuItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SuperKiwiEvent implements Listener {
    private SuperPlugin plugin = SuperPlugin.getInstance();
    private static HashMap<Player, WormHole> playerWormHoles = new HashMap<>();
    private String supername = "superkiwi";

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            Player player = e.getPlayer();
            ItemStack item = e.getItem();
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (itemData.has(CustomKeys.SUPER_KIWI) || itemData.has(CustomKeys.WORM_HOLE)) {
                    e.setCancelled(true);
                    if (player.getInventory().getItemInMainHand().equals(item)) {
                        InteractEvent(itemData, player, item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand() != null) {
            Player player = e.getPlayer();
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            if (itemData.has(CustomKeys.SUPER_KIWI) || itemData.has(CustomKeys.WORM_HOLE)) {
                e.setCancelled(true);
                if (player.getInventory().getItemInMainHand().equals(item)) {
                    InteractEvent(itemData, player, item);
                }
            }
        }
    }

    private void InteractEvent(PersistentDataContainer itemData, Player player, ItemStack item) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (itemData.has(CustomKeys.SUPER_KIWI)) {
            switch (itemData.get(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER)) {
                case 0:
                    SuperKiwiFunction.SuperTransformation(player);
                    break;
                case 1:
                    if (Function.CheckSkillUse(player, supername, 1)) {
                        break;
                    }

                    SuperKiwiFunction.KiwiSkill_1(player);
                    break;
                case 2:
                    if (Function.CheckSkillUse(player, supername, 2)) {
                        break;
                    }

                    SuperKiwiFunction.KiwiSkill_2(player, 2 * 20);
                    break;
                case 3:
                    if (Function.CheckSkillUse(player, supername, 3)) {
                        break;
                    }

                    SuperKiwiFunction.KiwiSkill_3(player, 2 * 20);
                    break;
                case 4:
                    if (Function.CheckSkillUse(player, supername, 4)) {
                        break;
                    }

                    SuperKiwiFunction.KiwiUltimate(player, 20 * 20);
                    break;
            }
        } else if (itemData.has(CustomKeys.WORM_HOLE)) {
            int delay = 2 * 20;

            playerWormHoles.putIfAbsent(player, new WormHole(player.getUniqueId()));
            playerWormHoles.get(player).ThrowWormHole((SuperKiwiItem.WORMHOLE.getAmount() - item.getAmount()) % 2);

            if (item.getAmount() == 1) {
                int[] wormholes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperKiwiItem.WORMHOLE, PersistentDataType.INTEGER);
                if (wormholes != null) {
                    if (wormholes.length > 1) {
                        item.setAmount(item.getAmount() - 1);
                    }
                    else {
                        player.getInventory().setItem(wormholes[0], SuperKiwiItem.SUPER_KIWI_SKILL_1);
                        CoolTimeManager.SetCoolTime(player, supername, 1, delay);
                    }
                }
            }
            else {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        ItemStack[] cancelItemStacks = SuperKiwiItem.removeItems.clone();
        Player player = e.getPlayer();

        if (Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            if (Function.CompareItemPersistentData(itemStack, SuperKiwiItem.SUPER_KIWI, PersistentDataType.INTEGER))
                e.setCancelled(true);
        }

        for (int i = 0; i < cancelItemStacks.length; i++) {
            if (Function.CompareItemPersistentData(itemStack, cancelItemStacks[i], PersistentDataType.INTEGER)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            SuperKiwiFunction.Untransformed(player);
        }
    }

    @EventHandler
    public void ItemSpawnEvent(ItemSpawnEvent e) {
        ItemStack[] cancelItemStacks = SuperKiwiItem.removeItems.clone();
        ItemStack itemStack = e.getEntity().getItemStack();

        for (int i = 0; i < cancelItemStacks.length; i++) {
            if (Function.CompareItemPersistentData(itemStack, cancelItemStacks[i], PersistentDataType.INTEGER)) {
                e.setCancelled(true);
            }
        }
    }
}
