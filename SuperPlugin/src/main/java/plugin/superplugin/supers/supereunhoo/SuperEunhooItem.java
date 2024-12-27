package plugin.superplugin.supers.supereunhoo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;

public class SuperEunhooItem {
    public static ItemStack SUPER_EUNHOO = null;
    public static ItemStack SUPER_EUNHOO_SKILL_1 = null;
    public static ItemStack SUPER_EUNHOO_SKILL_2 = null;
    public static ItemStack SUPER_EUNHOO_SKILL_3 = null;
    public static ItemStack SUPER_EUNHOO_ULTIMATE = null;

    public static ItemStack[] removeItems = null;
    public static ItemStack[] addItems = null;
    public static ItemStack[] superItems = null;

    public SuperEunhooItem() {
        SUPER_EUNHOO = Function.BuildItem(Material.OBSIDIAN, 1, ChatColor.YELLOW + "슈퍼 은후");
        {
            ItemMeta meta = SUPER_EUNHOO.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER, 0);
            SUPER_EUNHOO.setItemMeta(meta);
        }

        SUPER_EUNHOO_SKILL_1 = Function.BuildItem(Material.BLACK_DYE, 1, ChatColor.YELLOW + "서리폭풍");
        {
            ItemMeta meta = SUPER_EUNHOO_SKILL_1.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER, 1);
            SUPER_EUNHOO_SKILL_1.setItemMeta(meta);
        }

        SUPER_EUNHOO_SKILL_2 = Function.BuildItem(Material.MUSIC_DISC_STAL, 1, ChatColor.YELLOW + "얼음장벽");
        {
            ItemMeta meta = SUPER_EUNHOO_SKILL_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER, 2);
            SUPER_EUNHOO_SKILL_2.setItemMeta(meta);
        }

        SUPER_EUNHOO_SKILL_3 = Function.BuildItem(Material.WITHER_ROSE, 1, ChatColor.YELLOW + "겨울바람");
        {
            ItemMeta meta = SUPER_EUNHOO_SKILL_3.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER, 3);
            SUPER_EUNHOO_SKILL_3.setItemMeta(meta);
        }

        SUPER_EUNHOO_ULTIMATE = Function.BuildItem(Material.WITHER_SKELETON_SKULL, 1, ChatColor.YELLOW + "아이스 돔");
        {
            ItemMeta meta = SUPER_EUNHOO_ULTIMATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_EUNHOO, PersistentDataType.INTEGER, 4);
            SUPER_EUNHOO_ULTIMATE.setItemMeta(meta);
        }

        removeItems = new ItemStack[]{SUPER_EUNHOO_SKILL_1, SUPER_EUNHOO_SKILL_2, SUPER_EUNHOO_SKILL_3, SUPER_EUNHOO_ULTIMATE};
        addItems = new ItemStack[]{SUPER_EUNHOO_SKILL_1, SUPER_EUNHOO_SKILL_2, SUPER_EUNHOO_SKILL_3, SUPER_EUNHOO_ULTIMATE};
        superItems = new ItemStack[]{SUPER_EUNHOO_SKILL_1, SUPER_EUNHOO_SKILL_2, SUPER_EUNHOO_SKILL_3, SUPER_EUNHOO_ULTIMATE};
    }
}
