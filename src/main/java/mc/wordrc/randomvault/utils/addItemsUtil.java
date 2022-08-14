package mc.wordrc.randomvault.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;




public class addItemsUtil {


    public static void addItems() {

            HashMap<Integer, ItemStack> itemBatch = new HashMap<>();
            Material[] mats = Material.values(); //array with all materials
            for (int i = 0; i < 27; i++) {
                ItemStack item = new ItemStack(mats[new Random().nextInt(mats.length)]);
                while (item.getType().equals(Material.AIR) | !item.getType().isItem()) {
                    item = new ItemStack(mats[new Random().nextInt(mats.length)]);
                }

                itemBatch.put(i, item);
            }


            mc.wordrc.randomvault.RandomVault.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
                TextComponent message = new TextComponent(ChatColor.YELLOW + "You have received a new batch of items! /randvault");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "vault"));
                player.spigot().sendMessage(message);

                if (player.getPersistentDataContainer().get(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "isVaultOpen"), PersistentDataType.INTEGER) == 1) {
                    player.closeInventory();
                }


                try {
                    vaultUtils.storeItem(itemBatch, player);
                } catch (IOException e) {
                    player.sendMessage("something just went very fucking wrong, i'm deeply sorry");
                }

            });




    }
}
