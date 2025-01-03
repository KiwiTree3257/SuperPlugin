package plugin.superplugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.superplugin.command.GiveSuperPluginItem;
import plugin.superplugin.customworld.darkworld.DarkWorld;
import plugin.superplugin.customworld.darkworld.DarkWorldEvent;
import plugin.superplugin.customworld.darkworld.DarkWorldGenerator;
import plugin.superplugin.supers.supereunhoo.SuperEunhooEvent;
import plugin.superplugin.supers.supereunhoo.SuperEunhooItem;
import plugin.superplugin.supers.supereunhoo.SuperEunhooRunTime;
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
    public static World darkWorld;

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

        Bukkit.getPluginManager().registerEvents(new SuperEunhooEvent(), this);
        new SuperEunhooItem();
        new SuperEunhooRunTime();

        new CoolTimeManager();

        darkWorld = Bukkit.getWorld("darkworld");
        if (darkWorld == null) {
            darkWorld = DarkWorldGenerator.CreateDarkWorld("darkworld");
        }
        Bukkit.getPluginManager().registerEvents(new DarkWorldEvent(), this);
        new DarkWorld();
        Bukkit.getPluginManager().registerEvents(new Event(), this);
    }

    public static SuperPlugin getInstance() {
        return getPlugin(SuperPlugin.class);
    }
}
