package plugin.superplugin.supers.superjihwan;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;

public class SuperJihwanItem {
    public static ItemStack SUPER_JIHWAN = null;
    public static ItemStack SUPER_JIHWAN_SKILL_1 = null;
    public static ItemStack SUPER_JIHWAN_SKILL_2 = null;
    public static ItemStack SUPER_JIHWAN_SKILL_3 = null;
    public static ItemStack SUPER_JIHWAN_ULTIMATE = null;

    public static ItemStack[] removeItems = null;
    public static ItemStack[] addItems = null;
    public static ItemStack[] superItems = null;

    public SuperJihwanItem() {
        SUPER_JIHWAN = Function.BuildItem(Material.CALCITE, 1, ChatColor.YELLOW + "슈퍼 지환");
        {
            ItemMeta meta = SUPER_JIHWAN.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JIHWAN, PersistentDataType.INTEGER, 0);
            SUPER_JIHWAN.setItemMeta(meta);
        }

        SUPER_JIHWAN_SKILL_1 = Function.BuildItem(Material.FEATHER, 1, ChatColor.YELLOW + "돌풍");
        {
            ItemMeta meta = SUPER_JIHWAN_SKILL_1.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JIHWAN, PersistentDataType.INTEGER, 1);
            SUPER_JIHWAN_SKILL_1.setItemMeta(meta);
        }

        SUPER_JIHWAN_SKILL_2 = Function.BuildItem(Material.WHITE_DYE, 1, ChatColor.YELLOW + "상승기류");
        {
            ItemMeta meta = SUPER_JIHWAN_SKILL_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JIHWAN, PersistentDataType.INTEGER, 2);
            SUPER_JIHWAN_SKILL_2.setItemMeta(meta);
        }

        SUPER_JIHWAN_SKILL_3 = Function.BuildItem(Material.LIGHT_GRAY_DYE, 1, ChatColor.YELLOW + "초열풍참");
        {
            ItemMeta meta = SUPER_JIHWAN_SKILL_3.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JIHWAN, PersistentDataType.INTEGER, 3);
            SUPER_JIHWAN_SKILL_3.setItemMeta(meta);
        }

        SUPER_JIHWAN_ULTIMATE = Function.BuildItem(Material.WIND_CHARGE, 1, ChatColor.YELLOW + "위타천 태풍");
        {
            ItemMeta meta = SUPER_JIHWAN_ULTIMATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JIHWAN, PersistentDataType.INTEGER, 4);
            SUPER_JIHWAN_ULTIMATE.setItemMeta(meta);
        }

        removeItems = new ItemStack[]{SUPER_JIHWAN_SKILL_1, SUPER_JIHWAN_SKILL_2, SUPER_JIHWAN_SKILL_3, SUPER_JIHWAN_ULTIMATE};
        addItems = new ItemStack[]{SUPER_JIHWAN_SKILL_1, SUPER_JIHWAN_SKILL_2, SUPER_JIHWAN_SKILL_3, SUPER_JIHWAN_ULTIMATE};
        superItems = new ItemStack[]{SUPER_JIHWAN_SKILL_1, SUPER_JIHWAN_SKILL_2, SUPER_JIHWAN_SKILL_3, SUPER_JIHWAN_ULTIMATE};
    }
}
