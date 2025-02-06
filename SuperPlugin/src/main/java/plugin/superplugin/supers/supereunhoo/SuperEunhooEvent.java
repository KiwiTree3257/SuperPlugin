package plugin.superplugin.supers.supereunhoo;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.stack.DarkStack;

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
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (itemData.has(CustomKeys.SUPER_EUNHOO)) {
            switch (itemData.get(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER)) {
                case 0:
                    SuperEunhooFunction.SuperTransformation(player);
                    break;
                case 1:
                    if (playerData.has(CustomKeys.SKILL_STOP)) {
                        player.sendMessage("스킬을 사용할 수 없습니다");
                        break;
                    }

                    SuperEunhooFunction.EunhooSkill_1(player, 3 * 20);
                    break;
                case 2:
                    if (Function.CheckSkillUse(player, supername, 2)) {
                        break;
                    }

                    SuperEunhooFunction.EunhooSkill_2(player, 8 * 20);
                    break;
                case 3:
                    if (Function.CheckSkillUse(player, supername, 3)) {
                        break;
                    }

                    SuperEunhooFunction.EunhooSkill_3(player, 2 * 20);
                    break;
                case 4:
                    if (Function.CheckSkillUse(player, supername, 4)) {
                        break;
                    }
                    SuperEunhooFunction.EunhooUltimate(player, 35 * 20);
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
        Entity damager = e.getDamager();
        Entity entity = e.getEntity();

        if (
                e.getDamager() instanceof Player &&
                Objects.equals(e.getDamager().getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), supername) &&
                e.getEntity() instanceof LivingEntity) {

            DarkStack.DarkEntity((LivingEntity) entity);

            if (e.getDamager().getPersistentDataContainer().has(CustomKeys.NEXT_ATTACK_EUNHOO)) {
                entity.setVelocity(damager.getLocation().getDirection().normalize().multiply(4));
                ((LivingEntity) entity).damage(8);
                e.getDamager().getPersistentDataContainer().remove(CustomKeys.NEXT_ATTACK_EUNHOO);
            }
        }
    }
}
