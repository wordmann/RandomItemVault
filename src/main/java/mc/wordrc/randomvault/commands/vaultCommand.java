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
                if (!p.getPersistentDataContainer().has(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING)) {
                    p.getPersistentDataContainer().set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING, "");
                }
                HashMap<Integer, ItemStack> vaultItems = vaultUtils.getItems(p);

                int size = RandomVault.getPlugin().getConfig().getInt(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+ ".size", 27);
                size = (int) (Math.ceil((double) size /9)*9);
                p.sendMessage(String.valueOf(size));
                Inventory vault = Bukkit.createInventory(p, size, "Vault");

                for (int i = 0; i < Math.min(vaultItems.size(), size) ; i++) {
                    try {
                        vault.setItem(i, vaultItems.get(i));
                    } catch (NullPointerException ignored) {
                    }
                }

                p.getPersistentDataContainer().set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER, 1);
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
                if (args.length==2){
                    p.sendMessage(ChatColor.RED + "Please provide timings in seconds. /randvault timings <delay> <period>");
                }else {
                    try {
                        taskManagementUtil.modifyTimings(Long.valueOf(args[1]), Long.valueOf(args[2]));
                        p.sendMessage(ChatColor.GRAY + "Setting delay to " + ChatColor.DARK_GRAY + args[1] + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY + args[2] + ChatColor.GRAY + " seconds");
                    } catch (NumberFormatException exception) {
                        p.sendMessage(ChatColor.RED + "Please provide timings in seconds. /randvault timings <delay> <period>");
                    }
                }
            }else{
                p.sendMessage(ChatColor.GRAY + "Delay set to " + ChatColor.DARK_GRAY +
                    RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".delay")
                + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY +
                    RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".period")
                + ChatColor.GRAY + " seconds");
            }

        }else if (args[0].equalsIgnoreCase("size") && sender.hasPermission("randomvault.admin")){
            int size = -1;
            try {
                size = Integer.parseInt(args[1]);
                if (size < 1 | size > 54){
                    p.sendMessage(ChatColor.RED + "Size can only go from 1 to 54. /randvault size <size>");} else {
                    RandomVault.getPlugin().getConfig().set((RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+".size"), size);
                    RandomVault.getPlugin().saveConfig();
                    p.sendMessage(ChatColor.GRAY + "The vault will now contain " + ChatColor.DARK_GRAY + size + ChatColor.GRAY + " random items.");}
            }catch (NumberFormatException exception){
                p.sendMessage(ChatColor.RED + "Please provide size in a integral numeric value. /randvault size <size>");}
        }else{
            System.out.println("im so tired");
            sender.sendMessage(ChatColor.RED + "Invalid argument");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> argus = new ArrayList<>();
        if (sender.hasPermission("randomvault.admin")){
        argus.add("trigger");
        argus.add("timings");
        argus.add("size");
        argus.add("toggle");}
        return argus;
    }
}
