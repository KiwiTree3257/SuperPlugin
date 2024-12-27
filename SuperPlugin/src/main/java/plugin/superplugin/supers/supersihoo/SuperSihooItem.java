package plugin.superplugin.supers.supersihoo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;

public class SuperSihooItem {
    public static ItemStack SUPER_SIHOO = null;
    public static ItemStack SUPER_SIHOO_SKILL_1 = null;
    public static ItemStack SUPER_SIHOO_SKILL_2 = null;
    public static ItemStack SUPER_SIHOO_SKILL_3 = null;
    public static ItemStack SUPER_SIHOO_ULTIMATE = null;

    public static ItemStack[] removeItems = null;
    public static ItemStack[] addItems = null;
    public static ItemStack[] superItems = null;

    public SuperSihooItem() {
        SUPER_SIHOO = Function.BuildItem(Material.BLUE_ICE, 1, ChatColor.YELLOW + "슈퍼 시후");
        {
            ItemMeta meta = SUPER_SIHOO.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_SIHOO, PersistentDataType.INTEGER, 0);
            SUPER_SIHOO.setItemMeta(meta);
        }

        SUPER_SIHOO_SKILL_1 = Function.BuildItem(Material.BLUE_DYE, 1, ChatColor.YELLOW + "서리폭풍");
        {
            ItemMeta meta = SUPER_SIHOO_SKILL_1.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_SIHOO, PersistentDataType.INTEGER, 1);
            SUPER_SIHOO_SKILL_1.setItemMeta(meta);
        }

        SUPER_SIHOO_SKILL_2 = Function.BuildItem(Material.CYAN_DYE, 1, ChatColor.YELLOW + "얼음장벽");
        {
            ItemMeta meta = SUPER_SIHOO_SKILL_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_SIHOO, PersistentDataType.INTEGER, 2);
            SUPER_SIHOO_SKILL_2.setItemMeta(meta);
        }

        SUPER_SIHOO_SKILL_3 = Function.BuildItem(Material.LIGHT_BLUE_DYE, 1, ChatColor.YELLOW + "겨울바람");
        {
            ItemMeta meta = SUPER_SIHOO_SKILL_3.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_SIHOO, PersistentDataType.INTEGER, 3);
            SUPER_SIHOO_SKILL_3.setItemMeta(meta);
        }

        SUPER_SIHOO_ULTIMATE = Function.BuildItem(Material.GRAY_DYE, 1, ChatColor.YELLOW + "아이스 돔");
        {
            ItemMeta meta = SUPER_SIHOO_ULTIMATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_SIHOO, PersistentDataType.INTEGER, 4);
            SUPER_SIHOO_ULTIMATE.setItemMeta(meta);
        }

        removeItems = new ItemStack[]{SUPER_SIHOO_SKILL_1, SUPER_SIHOO_SKILL_2, SUPER_SIHOO_SKILL_3, SUPER_SIHOO_ULTIMATE};
        addItems = new ItemStack[]{SUPER_SIHOO_SKILL_1, SUPER_SIHOO_SKILL_2, SUPER_SIHOO_SKILL_3, SUPER_SIHOO_ULTIMATE};
        superItems = new ItemStack[]{SUPER_SIHOO_SKILL_1, SUPER_SIHOO_SKILL_2, SUPER_SIHOO_SKILL_3, SUPER_SIHOO_ULTIMATE};
    }
}
