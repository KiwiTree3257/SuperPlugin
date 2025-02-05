package plugin.superplugin.supers.superkiwi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;

public class SuperKiwiItem {
    public static ItemStack SUPER_KIWI = null;
    public static ItemStack SUPER_KIWI_SKILL_1 = null;
    public static ItemStack SUPER_KIWI_SKILL_2 = null;
    public static ItemStack SUPER_KIWI_SKILL_3 = null;
    public static ItemStack SUPER_KIWI_ULTIMATE = null;
    public static ItemStack WORMHOLE = null;

    public static ItemStack[] removeItems = null;
    public static ItemStack[] addItems = null;
    public static ItemStack[] superItems = null;

    public SuperKiwiItem() {
        SUPER_KIWI = Function.BuildItem(Material.AMETHYST_BLOCK, 1, ChatColor.YELLOW + "슈퍼 키위");
        {
            ItemMeta meta = SUPER_KIWI.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER, 0);
            SUPER_KIWI.setItemMeta(meta);
        }

        SUPER_KIWI_SKILL_1 = Function.BuildItem(Material.ECHO_SHARD, 1, ChatColor.YELLOW + "차원 이동기");
        {
            ItemMeta meta = SUPER_KIWI_SKILL_1.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER, 1);
            SUPER_KIWI_SKILL_1.setItemMeta(meta);
        }

        SUPER_KIWI_SKILL_2 = Function.BuildItem(Material.ENDER_EYE, 1, ChatColor.YELLOW + "강착");
        {
            ItemMeta meta = SUPER_KIWI_SKILL_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER, 2);
            SUPER_KIWI_SKILL_2.setItemMeta(meta);
        }

        SUPER_KIWI_SKILL_3 = Function.BuildItem(Material.AMETHYST_SHARD, 1, ChatColor.YELLOW + "빅뱅");
        {
            ItemMeta meta = SUPER_KIWI_SKILL_3.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER, 3);
            SUPER_KIWI_SKILL_3.setItemMeta(meta);
        }

        SUPER_KIWI_ULTIMATE = Function.BuildItem(Material.NETHER_STAR, 1, ChatColor.YELLOW + "천상강림");
        {
            ItemMeta meta = SUPER_KIWI_ULTIMATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_KIWI, PersistentDataType.INTEGER, 4);
            SUPER_KIWI_ULTIMATE.setItemMeta(meta);
        }

        WORMHOLE = Function.BuildItem(Material.GLOW_INK_SAC, 3, ChatColor.YELLOW + "웜홀 가루");
        {
            ItemMeta meta = WORMHOLE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.WORM_HOLE, PersistentDataType.INTEGER, 1);
            WORMHOLE.setItemMeta(meta);
        }

        removeItems = new ItemStack[]{SUPER_KIWI_SKILL_1, SUPER_KIWI_SKILL_2, SUPER_KIWI_SKILL_3, SUPER_KIWI_ULTIMATE, WORMHOLE};
        addItems = new ItemStack[]{SUPER_KIWI_SKILL_1, SUPER_KIWI_SKILL_2, SUPER_KIWI_SKILL_3, SUPER_KIWI_ULTIMATE};
        superItems = new ItemStack[]{SUPER_KIWI_SKILL_1, SUPER_KIWI_SKILL_2, SUPER_KIWI_SKILL_3, SUPER_KIWI_ULTIMATE};
    }
}
