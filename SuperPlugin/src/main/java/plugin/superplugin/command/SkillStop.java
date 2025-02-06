package plugin.superplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import plugin.superplugin.Function;
import plugin.superplugin.supers.supereunhoo.SuperEunhooItem;
import plugin.superplugin.supers.superjihwan.SuperJihwanItem;
import plugin.superplugin.supers.superjunu.SuperJunuItem;
import plugin.superplugin.supers.superkiwi.SuperKiwiItem;
import plugin.superplugin.supers.supersihoo.SuperSihooItem;

public class SkillStop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Bukkit.getLogger().info("스킬멍춤ㅋ");

        Function.AllPlayerSkillStop();

        return true;
    }
}
