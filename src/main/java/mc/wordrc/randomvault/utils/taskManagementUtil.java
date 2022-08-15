package mc.wordrc.randomvault.utils;

import mc.wordrc.randomvault.RandomVault;
import mc.wordrc.randomvault.tasks.addItemsTask;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

public class taskManagementUtil {
    public static void loadVaultSettings() {

        mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getScheduler().cancelTask(
                mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getInt(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".taskid")
        );

        if (mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getBoolean(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".randItemsToggle", false)) {
            BukkitTask randTask = (BukkitTask) new addItemsTask(RandomVault.getPlugin()).runTaskTimer(RandomVault.getPlugin(),
                    ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay", 30))*20),
                    ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period", 30))*20));
            RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".taskid", Integer.valueOf(randTask.getTaskId()));}
        RandomVault.getPlugin().saveConfig();
    }

    public static void modifyTimings(Long delay, Long period) {
        mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set((mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay"), Long.valueOf(delay));
        mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set((mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period"), Long.valueOf(period));
        RandomVault.getPlugin().saveConfig();
        loadVaultSettings();
    }

    public static Boolean toggleVault() {
        if (!mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getBoolean(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".randItemsToggle")){
            System.out.println("vault is ON");
            mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".randItemsToggle", true);
            RandomVault.getPlugin().saveConfig();
            loadVaultSettings();
            return true;}
        else{
            System.out.println("vault is OFF");
            mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".randItemsToggle", false);
            RandomVault.getPlugin().saveConfig();
            loadVaultSettings();
            return false;}
    }
}
