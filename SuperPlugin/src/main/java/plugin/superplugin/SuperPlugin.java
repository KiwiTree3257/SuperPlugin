package plugin.superplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.superplugin.command.GiveSuperPluginItem;
import plugin.superplugin.supers.superjihwan.SuperJihwanEvent;
import plugin.superplugin.supers.superjihwan.SuperJihwanItem;
import plugin.superplugin.supers.superjihwan.SuperJihwanRunTime;
import plugin.superplugin.supers.supersihoo.SuperSihooEvent;
import plugin.superplugin.supers.supersihoo.SuperSihooItem;
import plugin.superplugin.supers.supersihoo.SuperSihooRunTime;
import plugin.superplugin.supers.superjunu.SuperJunuEvent;
import plugin.superplugin.supers.superjunu.SuperJunuItem;
import plugin.superplugin.supers.superjunu.SuperJunuRunTime;

public final class SuperPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("givesuperitem").setExecutor(new GiveSuperPluginItem());

        Bukkit.getPluginManager().registerEvents(new SuperJunuEvent(), this);
        new SuperJunuItem();
        new SuperJunuRunTime();

        Bukkit.getPluginManager().registerEvents(new SuperSihooEvent(), this);
        new SuperSihooItem();
        new SuperSihooRunTime();

        Bukkit.getPluginManager().registerEvents(new SuperJihwanEvent(), this);
        new SuperJihwanItem();
        new SuperJihwanRunTime();

        new CoolTimeManager();
    }

    public static SuperPlugin getInstance() {
        return getPlugin(SuperPlugin.class);
    }
}
