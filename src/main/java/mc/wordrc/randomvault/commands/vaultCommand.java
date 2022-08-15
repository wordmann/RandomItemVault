package mc.wordrc.randomvault.commands;

import mc.wordrc.randomvault.RandomVault;
import mc.wordrc.randomvault.tasks.addItemsTask;
import mc.wordrc.randomvault.utils.ColorUtils;
import mc.wordrc.randomvault.utils.addItemsUtil;
import mc.wordrc.randomvault.utils.taskManagementUtil;
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

            if (taskManagementUtil.toggleVault()){
            p.sendMessage(ChatColor.GRAY + "Random vault is now: " + ChatColor.DARK_GRAY + "ON");}
            else{
            p.sendMessage(ChatColor.GRAY + "Random vault is now: " + ChatColor.DARK_GRAY + "OFF");}

        } else if (args[0].equalsIgnoreCase("timings") && sender.hasPermission("randomvault.admin")) {

            if (args.length>1) {
                p.sendMessage(ChatColor.GRAY + "Setting delay to " + ChatColor.DARK_GRAY + args[1] + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY + args[2] + ChatColor.GRAY + " seconds");
                try {
                taskManagementUtil.modifyTimings(Long.valueOf(args[1]), Long.valueOf(args[2]));}
                catch (NumberFormatException exception) {
                    p.sendMessage(ChatColor.RED + "Please provide periods in seconds. /randvault timings <delay> <period>");
                }
            }else{
                p.sendMessage(ChatColor.GRAY + "Delay set to " + ChatColor.DARK_GRAY +
                    RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay")
                + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY +
                    RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period")
                + ChatColor.GRAY + " seconds");
            }

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
