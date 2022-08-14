package mc.wordrc.randomvault;

import mc.wordrc.randomvault.commands.*;
import mc.wordrc.randomvault.listeners.*;
import mc.wordrc.randomvault.tasks.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public final class RandomVault extends JavaPlugin implements Listener {

    private static mc.wordrc.randomvault.RandomVault plugin;



    @Override
    public void onEnable() {
        System.out.println("It's funky time!");

        plugin = this;

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new vaultListener(), this);

        Objects.requireNonNull(getCommand("randvault")).setExecutor(new vaultCommand());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        BukkitTask randTask = (BukkitTask) new addItemsTask(plugin).runTaskTimer(this,
                ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period", 30))*20),
                ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay", 30))*20));
        getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".taskid", Integer.valueOf(randTask.getTaskId()));
        saveConfig();
    }

    public static mc.wordrc.randomvault.RandomVault getPlugin() {
        return plugin;
    }
}
