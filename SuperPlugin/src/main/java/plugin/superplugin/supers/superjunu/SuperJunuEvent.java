package plugin.superplugin.supers.superjunu;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.FireArrow;

import java.util.List;
import java.util.Objects;

public class SuperJunuEvent implements Listener {
    private SuperPlugin plugin = SuperPlugin.getInstance();
    private String supername = "superjunu";

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            Player player = e.getPlayer();
            ItemStack item = e.getItem();
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (itemData.has(CustomKeys.SUPER_JUNU) || itemData.has(CustomKeys.FIRE_ARROW)) {
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

            if (itemData.has(CustomKeys.SUPER_JUNU) || itemData.has(CustomKeys.FIRE_ARROW)) {
                e.setCancelled(true);
                if (player.getInventory().getItemInMainHand().equals(item)) {
                    InteractEvent(itemData, player, item);
                }
            }
        }
    }

    private void InteractEvent(PersistentDataContainer itemData, Player player, ItemStack item) {
        Inventory playerInventory = player.getInventory();

        if (itemData.has(CustomKeys.SUPER_JUNU)) {
            switch (itemData.get(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER)) {
                case 0:
                    SuperJunuFunction.SuperTransformation(player);
                    break;
                case 1:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 1)) {
                        SuperJunuFunction.JunuSkill_1(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 1) + "초 남음");
                    }
                    break;
                case 2:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 2)) {
                        SuperJunuFunction.JunuSkill_2(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 2) + "초 남음");
                    }
                    break;
                case 3:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 3)) {
                        SuperJunuFunction.JunuSkill_3(player, item);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 3) + "초 남음");
                    }
                    break;
                case 4:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 4)) {
                        SuperJunuFunction.JunuSkill_4(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 4) + "초 남음");
                    }
                    break;
                case 5:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 5)) {
                        SuperJunuFunction.JunuSkill_5(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 5) + "초 남음");
                    }
                    break;
                case 6:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 6)) {
                        Block targetBlock = player.getTargetBlockExact(20);
                        if (targetBlock != null) {
                            SuperJunuFunction.JunuSkill_6(player, targetBlock, 2 * 20);
                        }
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 6) + "초 남음");
                    }
                    break;
                case 7:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 7)) {
                        SuperJunuFunction.JunuUltimate(player, 25 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 7) + "초 남음");
                    }
                    break;
                case 8:
                    if (CoolTimeManager.CheckCoolTime(player, supername, 8)) {
                        SuperJunuFunction.JunuUltimate_2(player, 2 * 20);
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 8) + "초 남음");
                    }
                    break;
            }
        } else if (itemData.has(CustomKeys.FIRE_ARROW)) {
            int delay = 2 * 20;
            new FireArrow(player);

            if (!player.getLocation().clone().add(0, -0.1, 0).getBlock().isCollidable()) {
                Vector velocity = player.getVelocity();
                velocity.add(player.getLocation().getDirection().normalize().multiply(-0.5));
                player.setVelocity(velocity);
            }

            if (item.getAmount() == 1) {
                int[] fireArrows = Function.FindItemPDIndexesAtPlayerInventory(player, SuperJunuItem.FIRE_ARROW, PersistentDataType.INTEGER);
                if (fireArrows != null) {
                    if (fireArrows.length > 1) {
                        item.setAmount(item.getAmount() - 1);
                    }
                    else {
                        playerInventory.setItem(fireArrows[0], SuperJunuItem.SUPER_JUNU_SKILL_3);
                        CoolTimeManager.SetCoolTime(player, supername, 3, delay);
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
        ItemStack[] cancelItemStacks = SuperJunuItem.removeItems.clone();
        Player player = e.getPlayer();

        if (Objects.equals(player.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            if (Function.CompareItemPersistentData(itemStack, SuperJunuItem.SUPER_JUNU, PersistentDataType.INTEGER))
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
            SuperJunuFunction.Untransformed(player);
        }
    }

    @EventHandler
    public void ItemSpawnEvent(ItemSpawnEvent e) {
        ItemStack[] cancelItemStacks = SuperJunuItem.removeItems.clone();
        ItemStack itemStack = e.getEntity().getItemStack();

        for (int i = 0; i < cancelItemStacks.length; i++) {
            if (Function.CompareItemPersistentData(itemStack, cancelItemStacks[i], PersistentDataType.INTEGER)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (e.getEntityType() == EntityType.ENDER_DRAGON && entity.getPersistentDataContainer().has(CustomKeys.FIRE_DRAGON)) {
            e.setDroppedExp(0);
        }
    }
}
