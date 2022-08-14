package mc.wordrc.randomvault.tasks;

import mc.wordrc.randomvault.utils.addItemsUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class addItemsTask extends BukkitRunnable {
    private final mc.wordrc.randomvault.RandomVault plugin;

    public addItemsTask(mc.wordrc.randomvault.RandomVault plugin) {
        this.plugin = plugin;
    }





    @Override
    public void run() {
        if (mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getBoolean(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".randItemsToggle", false)) {
            addItemsUtil.addItems();
        }
    }
}
