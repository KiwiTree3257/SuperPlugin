package plugin.superplugin.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import plugin.superplugin.supers.superjihwan.SuperJihwanItem;
import plugin.superplugin.supers.supersihoo.SuperSihooItem;
import plugin.superplugin.supers.superjunu.SuperJunuItem;

public class GiveSuperPluginItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ItemStack give = new ItemStack(Material.AIR);
        Player player = (Player) commandSender;
        int amount = 1;

        if (strings.length == 0){
            return false;
        }
        else if (strings[0].equals("SUPER_JUNU")) {
            give = SuperJunuItem.SUPER_JUNU;
        }
        else if (strings[0].equals("SUPER_SIHOO")) {
            give = SuperSihooItem.SUPER_SIHOO;
        } else if (strings[0].equals("SUPER_JIHWAN")) {
            give = SuperJihwanItem.SUPER_JIHWAN;
        }

        if (strings.length == 2){
            amount = Integer.parseInt(strings[1]);
        }

        if (amount / give.getMaxStackSize() > 0) {
            give.setAmount(give.getMaxStackSize());

            for (int i = 0; i < amount / give.getMaxStackSize(); i++) {
                player.getInventory().addItem(give);
            }
        }

        give.setAmount(amount % give.getMaxStackSize());
        player.getInventory().addItem(give);

        return true;
    }
}