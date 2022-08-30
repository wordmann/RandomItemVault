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





        if (args.length == 0) {

            Player p = null;
            try {p = vaultUtils.getPlayer(sender);}
            catch (Exception e){return true;}


            if (p.hasPermission("randomvault.use")|p.hasPermission("randomvault.admin")) {

                    if (!p.getPersistentDataContainer().has(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING)) {
                        p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING, "");
                    }
                    HashMap<Integer, ItemStack> vaultItems = vaultUtils.getItems(p);

                    int size = RandomVault.getPlugin().getConfig().getInt(RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".size", 3) * 9;

                    Inventory vault = Bukkit.createInventory(p, size, "Vault");

                    for (int i = 0; i < Math.min(vaultItems.size(), size); i++) {
                        try {
                            vault.setItem(i, vaultItems.get(i));
                        } catch (NullPointerException ignored) {
                        }
                    }

                    p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER, 1);
                    p.openInventory(vault);

                } else {
                    vaultUtils.sendMessage(ChatColor.RED + "Missing permission.", sender);
                }
        }

        //ARGUMENTS

        else if (args[0].equalsIgnoreCase("trigger") && sender.hasPermission("randomvault.admin")) {
            vaultUtils.sendMessage(ChatColor.GRAY + "Giving every player a new batch of items.", sender);
            addItemsUtil.addItems();

        } else if (args[0].equalsIgnoreCase("toggle") && sender.hasPermission("randomvault.admin")) {

            if (taskManagementUtil.toggleVault()) {
                vaultUtils.sendMessage(ChatColor.GRAY + "Random vault is now: " + ChatColor.DARK_GRAY + "ON", sender);
            } else {
                vaultUtils.sendMessage(ChatColor.GRAY + "Random vault is now: " + ChatColor.DARK_GRAY + "OFF", sender);
            }

        } else if (args[0].equalsIgnoreCase("timings") && sender.hasPermission("randomvault.admin")) {

            if (args.length > 1) {
                if (args.length == 2) {
                    vaultUtils.sendMessage(ChatColor.RED + "Please provide timings in seconds. /randvault timings <delay> <period>", sender);
                } else {
                    try {
                        taskManagementUtil.modifyTimings(Long.valueOf(args[1]), Long.valueOf(args[2]));
                        vaultUtils.sendMessage(ChatColor.GRAY + "Setting delay to " + ChatColor.DARK_GRAY + args[1] + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY + args[2] + ChatColor.GRAY + " seconds", sender);
                    } catch (NumberFormatException exception) {
                        vaultUtils.sendMessage(ChatColor.RED + "Please provide timings in seconds. /randvault timings <delay> <period>", sender);
                    }
                }
            } else {
                vaultUtils.sendMessage(ChatColor.GRAY + "Delay set to " + ChatColor.DARK_GRAY +
                        RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".delay")
                        + ChatColor.GRAY + " seconds and period to " + ChatColor.DARK_GRAY +
                        RandomVault.getPlugin().getConfig().getLong(RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".period")
                        + ChatColor.GRAY + " seconds", sender);
            }

        } else if (args[0].equalsIgnoreCase("itemsamount") && sender.hasPermission("randomvault.admin")) {
            int itemsamount = -1;
            try {
                itemsamount = Integer.parseInt(args[1]);
                if (itemsamount < 1 | itemsamount > 54) {
                    vaultUtils.sendMessage(ChatColor.RED + "The amount of items dropped can only go from 1 to 54. /randvault itemsamount <amount of items>", sender);
                } else {
                    RandomVault.getPlugin().getConfig().set((RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".itemsamount"), itemsamount);
                    RandomVault.getPlugin().saveConfig();
                    vaultUtils.sendMessage(ChatColor.GRAY + "The vault will now contain " + ChatColor.DARK_GRAY + itemsamount + ChatColor.GRAY + " random items.", sender);
                }
            } catch (NumberFormatException exception) {
                vaultUtils.sendMessage(ChatColor.RED + "Please provide size in a integral numeric value. /randvault itemsamount <ammount of items>", sender);
            }


        } else if (args[0].equalsIgnoreCase("size") && sender.hasPermission("randomvault.admin")) {
            int size = -1;
            try {
                size = Integer.parseInt(args[1]);
                if (size < 1 | size > 6) {
                    vaultUtils.sendMessage(ChatColor.RED + "The amount of rows in the vault can only go from 1 to 6. /randvault size <size>", sender);
                } else {
                    RandomVault.getPlugin().getConfig().set((RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".size"), size);
                    RandomVault.getPlugin().saveConfig();
                    vaultUtils.sendMessage(ChatColor.GRAY + "The vault will now hold up to " + ChatColor.DARK_GRAY + size + ChatColor.GRAY + " rows of items.", sender);
                }
            } catch (NumberFormatException exception) {
                vaultUtils.sendMessage(ChatColor.RED + "Please provide size in a integral numeric value. /randvault size <size>", sender);
            }


        } else if (args[0].equalsIgnoreCase("toggleoverflow") && sender.hasPermission("randomvault.admin")) {
            if (RandomVault.getPlugin().getConfig().getBoolean(RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".overflow")) {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault resetting on overflow is now " + ChatColor.DARK_GRAY + "OFF", sender);
                RandomVault.getPlugin().getConfig().set((RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".overflow"), false);
            } else {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault resetting on overflow is now " + ChatColor.DARK_GRAY + "ON", sender);
                RandomVault.getPlugin().getConfig().set((RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".overflow"), true);
            }
            RandomVault.getPlugin().saveConfig();



        } else if (args[0].equalsIgnoreCase("toggleitemsnotif")) {
            Player p = null;
            try {p = vaultUtils.getPlayer(sender);}
            catch (Exception e){return true;}

            if (vaultUtils.toggleNBT("RandomVault.itemsNotif", p)) {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault items notifications are now " + ChatColor.DARK_GRAY + "ON", sender);
            } else {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault items notifications are now " + ChatColor.DARK_GRAY + "OFF", sender);
            }

        } else if (args[0].equalsIgnoreCase("toggleoverflownotif")) {
            Player p = null;
            try {p = vaultUtils.getPlayer(sender);}
            catch (Exception e){return true;}

            if (vaultUtils.toggleNBT("RandomVault.overflowNotif", p)) {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault overflow notifications are now " + ChatColor.DARK_GRAY + "ON", sender);
            } else {
                vaultUtils.sendMessage(ChatColor.GRAY + "Vault overflow notifications are now " + ChatColor.DARK_GRAY + "OFF", sender);
            }


        } else if (args[0].equalsIgnoreCase("togglefullnotif")) {
            Player p = null;
            try {p = vaultUtils.getPlayer(sender);}
            catch (Exception e){return true;}

                if (vaultUtils.toggleNBT("RandomVault.fullNotif", p)) {
                    vaultUtils.sendMessage(ChatColor.GRAY + "Vault full notifications are now " + ChatColor.DARK_GRAY + "ON", sender);
                } else {
                    vaultUtils.sendMessage(ChatColor.GRAY + "Vault full notifications are now " + ChatColor.DARK_GRAY + "OFF", sender);
                }

        } else {
            System.out.println("im so tired");
            sender.sendMessage(ChatColor.RED + "Invalid argument or missing permission.");
        }
        return true;
    }






        @Override
        public List<String> onTabComplete (CommandSender sender, Command command, String label, String[]args){
            List<String> argus = new ArrayList<>();
            argus.add("toggleoverflownotif");
            argus.add("toggleitemflownotif");
            argus.add("togglefullnotif");
            if (sender.hasPermission("randomvault.admin")) {
                argus.add("trigger");
                argus.add("timings");
                argus.add("size");
                argus.add("itemsamount");
                argus.add("toggle");
                argus.add("toggleoverflow");
            }
            return argus;
        }



}
