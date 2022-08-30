package mc.wordrc.randomvault.utils;

import mc.wordrc.randomvault.RandomVault;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class addItemsUtil {


    public static void addItems() {

            List<ItemStack> itemBatch = new ArrayList<>();
            Material[] mats = Material.values(); //array with all materials
            int itemsamount = RandomVault.getPlugin().getConfig().getInt(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+ ".itemsamount", 27);
            for (int i = 0; i < itemsamount; i++) {
                ItemStack item = new ItemStack(mats[new Random().nextInt(mats.length)]);
                while (item.getType().isAir() | !item.getType().isItem()) {
                    item = new ItemStack(mats[new Random().nextInt(mats.length)]);
                }
                itemBatch.add(item);
            }





            RandomVault.getPlugin().getServer().getOnlinePlayers().forEach(player -> {

                if (player.hasPermission("randomvault.use"))

                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5 , 5);

                if (player.hasPermission("randomvault.use")|player.hasPermission("randomvault.admin")) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " [\"\",{\"text\":\"You have received a new batch of items! \",\"color\":\"green\"},{\"text\":\"/randvault\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/randvault\"}}]");
                }

                if (player.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER) == 1) {
                    player.closeInventory();
                }

                Inventory tempVault = vaultUtils.getVault(player);
                AtomicBoolean isOverflowed = new AtomicBoolean(false);
                AtomicBoolean isFull = new AtomicBoolean(false);

                itemBatch.forEach(itemStack -> {
                    if (tempVault.firstEmpty()==-1){
                        if ((RandomVault.getPlugin().getConfig().getBoolean(RandomVault.getPlugin().getServer().getWorlds().get(0).toString() + ".overflow"))&&!(isOverflowed.get())){
                        isOverflowed.set(true);
                            if (player.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.overflowNotif"), PersistentDataType.INTEGER)==1) {
                                player.performCommand("tellraw @s [\"\",{\"text\":\"Your vault overflowed! Items reset. \",\"color\":\"red\"},{\"text\":\"[Don't show again]\",\"color\":\"dark_red\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/randvault toggleoverflownotif\"}}]");
                            }
                        } else if ((player.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.fullNotif"), PersistentDataType.INTEGER)==1)&&!(isFull.get())) {
                            player.performCommand("tellraw @s [\"\",{\"text\":\"Your vault is full!! Clear it to receive new items. \",\"color\":\"red\"},{\"text\":\"[Don't show again]\",\"color\":\"dark_red\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/randvault togglefullnotif\"}}]");
                            isFull.set(true);
                        }
                    }

                    tempVault.addItem(itemStack);

                });

                if (isOverflowed.get()){
                    tempVault.clear();
                    itemBatch.forEach(tempVault::addItem);}



                try {
                    vaultUtils.storeItem(vaultUtils.getHashMap(tempVault), player);
                } catch (IOException e) {
                    player.sendMessage("something just went very fucking wrong, i'm deeply sorry");
                }

            });




    }
}
