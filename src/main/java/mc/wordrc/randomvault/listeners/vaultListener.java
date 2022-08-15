package mc.wordrc.randomvault.listeners;

import mc.wordrc.randomvault.*;
import mc.wordrc.randomvault.RandomVault;
import mc.wordrc.randomvault.utils.vaultUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.HashMap;

public class vaultListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER, 0);
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) throws IOException {

        Player p = (Player) e.getPlayer();
        Inventory vaultInv = (Inventory) e.getInventory();

        if (p.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER)==1){

            HashMap<Integer, ItemStack> vaultContents = new HashMap<>();

            for (int i = 0; i < vaultInv.getSize(); i++){
                try {
                    vaultContents.put(i, vaultInv.getItem(i));
                } catch (NullPointerException ignored) {
                }
            }

            vaultUtils.storeItem(vaultContents, p);
            p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), "RandomVault.openInvId"), PersistentDataType.INTEGER, 0);
        }

    }



}
