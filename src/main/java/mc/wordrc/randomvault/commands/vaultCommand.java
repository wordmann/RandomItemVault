package mc.wordrc.randomvault.commands;

import mc.wordrc.randomvault.tasks.addItemsTask;
import mc.wordrc.randomvault.utils.addItemsUtil;
import mc.wordrc.randomvault.utils.vaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class vaultCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.print("i swear to god this plugin");
        Player p = (Player) sender;
        if (args.length==0) {
            if (sender instanceof Player) {
                if (!p.getPersistentDataContainer().has(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "vault"), PersistentDataType.STRING)) {
                    p.getPersistentDataContainer().set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "vault"), PersistentDataType.STRING, "");
                }
                HashMap<Integer, ItemStack> vaultItems = vaultUtils.getItems(p);

                Inventory vault = Bukkit.createInventory(p, 27, "Vault");

                for (int i = 0; i < vaultItems.size(); i++) {
                    try {
                        vault.setItem(i, vaultItems.get(i));
                    } catch (NullPointerException ignored) {
                    }
                }

                p.getPersistentDataContainer().set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "isVaultOpen"), PersistentDataType.INTEGER, 1);
                p.openInventory(vault);
            }
        }

        else if (args[0].equalsIgnoreCase("trigger")&&sender.hasPermission("randomvault.admin")) {
            p.sendMessage(ChatColor.GRAY + "Giving every player a new batch of items.");
            addItemsUtil.addItems();

        } else if (args[0].equalsIgnoreCase("toggle") && sender.hasPermission("randomvault.admin")) {

            if (!mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getBoolean(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".randItemsToggle")){
                p.sendMessage(ChatColor.GRAY + "Random vault is now: ON");
                System.out.println("vault is ON");
                mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".randItemsToggle", true);}

            else{
                p.sendMessage(ChatColor.GRAY + "Random vault is now: OFF");
                System.out.println("vault is OFF");
                mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".randItemsToggle", false);}

                mc.wordrc.randomvault.RandomVault.getPlugin().saveConfig();

        } else if (args[0].equalsIgnoreCase("timings") && sender.hasPermission("randomvault.admin")) {
            p.sendMessage(ChatColor.GRAY + "Setting delay to " + ChatColor.DARK_GRAY +  args[1] + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY + args[2] + ChatColor.GRAY + " seconds");
            mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set((mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay"), Long.valueOf(args[1]));
            mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set((mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period"), Long.valueOf(args[2]));



            mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getScheduler().cancelTask(
                    mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getInt(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".taskid")
            );


            BukkitTask randTask = (BukkitTask) new addItemsTask(mc.wordrc.randomvault.RandomVault.getPlugin()).runTaskTimer(mc.wordrc.randomvault.RandomVault.getPlugin(),
                    ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay"))* 20L),
                    ((mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().getLong(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period"))* 20L));

            mc.wordrc.randomvault.RandomVault.getPlugin().getConfig().set(mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".taskid", randTask.getTaskId());
            mc.wordrc.randomvault.RandomVault.getPlugin().saveConfig();
        }else{
            System.out.println("im so tired");
            sender.sendMessage("Invalid argument");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> argus = new ArrayList<>();
        if (sender.hasPermission("randomvault.admin")){
        argus.add("trigger");
        argus.add("timings");
        argus.add("toggle");}
        return argus;
    }
}
