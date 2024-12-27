package plugin.superplugin.supers.supereunhoo;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.stack.FreezeStack;

import java.util.Objects;

public class SuperEunhooEvent implements Listener {
    private SuperPlugin plugin = SuperPlugin.getInstance();
    private String supername = "supereunhoo";

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            Player player = e.getPlayer();
            ItemStack item = e.getItem();
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (itemData.has(CustomKeys.SUPER_EUNHOO)) {
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

            if (itemData.has(CustomKeys.SUPER_EUNHOO)) {
                e.setCancelled(true);
                if (player.getInventory().getItemInMainHand().equals(item)) {
                    InteractEvent(itemData, player, item);
                }
            }
        }
    }

    private void InteractEvent(PersistentDataContainer itemData, Player player, ItemStack item) {
        if (itemData.has(CustomKeys.SUPER_EUNHOO)) {
            switch (itemData.get(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER)) {
                case 0:
                    SuperEunhooFunction.SuperTransformation(player);
                    break;
                case 1:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 1)) {
                        SuperEunhooFunction.EunhooSkill_1(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 1) + "초 남음");
                    }
                    break;
                case 2:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 2)) {
                        SuperEunhooFunction.EunhooSkill_2(player, 8 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 2) + "초 남음");
                    }
                    break;
                case 3:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 3)) {
                        SuperEunhooFunction.EunhooSkill_3(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 3) + "초 남음");
                    }
                    break;
                case 4:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 4)) {
                        SuperEunhooFunction.EunhooUltimate(player, 20 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 4) + "초 남음");
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        ItemStack[] cancelItemStacks = SuperEunhooItem.removeItems.clone();
        Player player = e.getPlayer();

        if (Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            if (Function.CompareItemPersistentData(itemStack, SuperEunhooItem.SUPER_EUNHOO, PersistentDataType.INTEGER))
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
            SuperEunhooFunction.Untransformed(player);
        }
    }

    @EventHandler
    public void ItemSpawnEvent(ItemSpawnEvent e) {
        ItemStack[] cancelItemStacks = SuperEunhooItem.removeItems.clone();
        ItemStack itemStack = e.getEntity().getItemStack();

        for (int i = 0; i < cancelItemStacks.length; i++) {
            if (Function.CompareItemPersistentData(itemStack, cancelItemStacks[i], PersistentDataType.INTEGER)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (
                e.getDamager() instanceof Player &&
                Objects.equals(e.getDamager().getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), supername) &&
                e.getEntity() instanceof LivingEntity) {

            FreezeStack.FreezeEntity((LivingEntity) e.getEntity());
        }
    }
}
