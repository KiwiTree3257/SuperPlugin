package plugin.superplugin.supers.superjunu;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;

public class SuperJunuItem {
    public static ItemStack SUPER_JUNU = null;
    public static ItemStack SUPER_JUNU_SKILL_1 = null;
    public static ItemStack SUPER_JUNU_SKILL_2 = null;
    public static ItemStack SUPER_JUNU_SKILL_3 = null;
    public static ItemStack SUPER_JUNU_SKILL_4 = null;
    public static ItemStack SUPER_JUNU_SKILL_5 = null;
    public static ItemStack SUPER_JUNU_SKILL_6 = null;
    public static ItemStack SUPER_JUNU_ULTIMATE = null;
    public static ItemStack SUPER_JUNU_ULTIMATE_2 = null;

    public static ItemStack FIRE_ARROW = null;

    public static ItemStack JUNU_ARMOR_BOOTS = null;
    public static ItemStack JUNU_ARMOR_LEGGINGS = null;
    public static ItemStack JUNU_ARMOR_CHESTPLATE = null;
    public static ItemStack JUNU_ARMOR_HELMET = null;

    public static ItemStack[] removeItems = null;
    public static ItemStack[] addItems = null;
    public static ItemStack[] addUltimateItems = null;
    public static ItemStack[] armorItems = null;
    public static ItemStack[] superItems = null;

    public SuperJunuItem() {
        SUPER_JUNU = Function.BuildItem(Material.MAGMA_BLOCK, 1, ChatColor.YELLOW + "슈퍼 준후");
        {
            ItemMeta meta = SUPER_JUNU.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 0);
            meta.setFireResistant(true);
            SUPER_JUNU.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_1 = Function.BuildItem(Material.IRON_SWORD, 1, ChatColor.YELLOW + "불의 검");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_1.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 1);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
            SUPER_JUNU_SKILL_1.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_2 = Function.BuildItem(Material.ORANGE_DYE, 1, ChatColor.YELLOW + "용암 분출");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 2);
            SUPER_JUNU_SKILL_2.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_3 = Function.BuildItem(Material.YELLOW_DYE, 1, ChatColor.YELLOW + "불화살");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_3.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 3);
            SUPER_JUNU_SKILL_3.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_4 = Function.BuildItem(Material.NETHERITE_SWORD, 1, ChatColor.YELLOW + "불의 검");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_4.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 4);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
            SUPER_JUNU_SKILL_4.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_5 = Function.BuildItem(Material.ORANGE_DYE, 1, ChatColor.YELLOW + "메테오");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_5.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 5);
            SUPER_JUNU_SKILL_5.setItemMeta(meta);
        }

        SUPER_JUNU_SKILL_6 = Function.BuildItem(Material.YELLOW_DYE, 1, ChatColor.YELLOW + "뿌슝빠슝");
        {
            ItemMeta meta = SUPER_JUNU_SKILL_6.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 6);
            SUPER_JUNU_SKILL_6.setItemMeta(meta);
        }

        SUPER_JUNU_ULTIMATE = Function.BuildItem(Material.FIRE_CHARGE, 1, ChatColor.YELLOW + "각성");
        {
            ItemMeta meta = SUPER_JUNU_ULTIMATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 7);
            SUPER_JUNU_ULTIMATE.setItemMeta(meta);
        }

        SUPER_JUNU_ULTIMATE_2 = Function.BuildItem(Material.FIRE_CHARGE, 1, ChatColor.YELLOW + "라이딩드래곤");
        {
            ItemMeta meta = SUPER_JUNU_ULTIMATE_2.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.SUPER_JUNU, PersistentDataType.INTEGER, 8);
            SUPER_JUNU_ULTIMATE_2.setItemMeta(meta);
        }

        FIRE_ARROW = Function.BuildItem(Material.ARROW, 10, ChatColor.YELLOW + "불화살");
        {
            ItemMeta meta = FIRE_ARROW.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.FIRE_ARROW, PersistentDataType.INTEGER, 1);
            FIRE_ARROW.setItemMeta(meta);
        }

        JUNU_ARMOR_BOOTS = Function.BuildItem(Material.NETHERITE_BOOTS, 1, ChatColor.DARK_RED + "불의 신발");
        {
            ArmorMeta meta = (ArmorMeta) JUNU_ARMOR_BOOTS.getItemMeta();
            meta.setTrim(new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE));
            meta.getPersistentDataContainer().set(CustomKeys.JUNU_ARMOR, PersistentDataType.INTEGER, 0);
            meta.setUnbreakable(true);
            JUNU_ARMOR_BOOTS.setItemMeta(meta);
        }

        JUNU_ARMOR_LEGGINGS = Function.BuildItem(Material.NETHERITE_LEGGINGS, 1, ChatColor.DARK_RED + "불의 바지");
        {
            ArmorMeta meta = (ArmorMeta) JUNU_ARMOR_LEGGINGS.getItemMeta();
            meta.setTrim(new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE));
            meta.getPersistentDataContainer().set(CustomKeys.JUNU_ARMOR, PersistentDataType.INTEGER, 1);
            meta.setUnbreakable(true);
            JUNU_ARMOR_LEGGINGS.setItemMeta(meta);
        }

        JUNU_ARMOR_CHESTPLATE = Function.BuildItem(Material.ELYTRA, 1, ChatColor.DARK_RED + "불의 날개");
        {
            ItemMeta meta = JUNU_ARMOR_CHESTPLATE.getItemMeta();
            meta.getPersistentDataContainer().set(CustomKeys.JUNU_ARMOR, PersistentDataType.INTEGER, 2);
            meta.setUnbreakable(true);
            JUNU_ARMOR_CHESTPLATE.setItemMeta(meta);
        }

        JUNU_ARMOR_HELMET = Function.BuildItem(Material.NETHERITE_HELMET, 1, ChatColor.DARK_RED + "불의 투구");
        {
            ArmorMeta meta = (ArmorMeta) JUNU_ARMOR_HELMET.getItemMeta();
            meta.setTrim(new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE));
            meta.getPersistentDataContainer().set(CustomKeys.JUNU_ARMOR, PersistentDataType.INTEGER, 3);
            meta.setUnbreakable(true);
            JUNU_ARMOR_HELMET.setItemMeta(meta);
        }

        removeItems = new ItemStack[]{
                SUPER_JUNU_SKILL_1, SUPER_JUNU_SKILL_2, SUPER_JUNU_SKILL_3, SUPER_JUNU_SKILL_4,
                SUPER_JUNU_SKILL_5, SUPER_JUNU_SKILL_6, SUPER_JUNU_ULTIMATE, SUPER_JUNU_ULTIMATE_2, FIRE_ARROW,
                JUNU_ARMOR_BOOTS, JUNU_ARMOR_LEGGINGS, JUNU_ARMOR_CHESTPLATE, JUNU_ARMOR_HELMET};

        addItems = new ItemStack[]{SUPER_JUNU_SKILL_1, SUPER_JUNU_SKILL_2, SUPER_JUNU_SKILL_3, SUPER_JUNU_ULTIMATE};
        addUltimateItems = new ItemStack[]{SUPER_JUNU_SKILL_4, SUPER_JUNU_SKILL_5, SUPER_JUNU_SKILL_6, SUPER_JUNU_ULTIMATE_2};
        superItems = new ItemStack[]{SUPER_JUNU_SKILL_1, SUPER_JUNU_SKILL_2, SUPER_JUNU_SKILL_3,
                SUPER_JUNU_SKILL_4, SUPER_JUNU_SKILL_5, SUPER_JUNU_SKILL_6, SUPER_JUNU_ULTIMATE, SUPER_JUNU_ULTIMATE_2};
        armorItems =  new ItemStack[]{JUNU_ARMOR_BOOTS, JUNU_ARMOR_LEGGINGS, JUNU_ARMOR_CHESTPLATE, JUNU_ARMOR_HELMET};


    }
}
