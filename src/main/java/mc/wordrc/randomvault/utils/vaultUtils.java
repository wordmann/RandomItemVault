package mc.wordrc.randomvault.utils;

import mc.wordrc.randomvault.RandomVault;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class vaultUtils {

    public static Boolean toggleNBT (String key, Player p){

        if (p.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), key), PersistentDataType.INTEGER)==1){
            p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), key), PersistentDataType.INTEGER, 0);
            return false;
        }else if (p.getPersistentDataContainer().get(new NamespacedKey(RandomVault.getPlugin(), key), PersistentDataType.INTEGER)==0){
            p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), key), PersistentDataType.INTEGER, 1);
            return true;
        }else{
            p.getPersistentDataContainer().set(new NamespacedKey(RandomVault.getPlugin(), key), PersistentDataType.INTEGER, 1);
            return true;
        }

    }

    public static HashMap<Integer, ItemStack> getHashMap(Inventory inv){

        HashMap<Integer, ItemStack> hashMap = new HashMap<>();

        for (int i = 0; i < inv.getSize(); i++){
            try {
                hashMap.put(i, inv.getItem(i));
            } catch (NullPointerException ignored) {
            }
        }
        return hashMap;
    }


    public static Inventory getVault(Player p) {

        if (!p.getPersistentDataContainer().has(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING)) {
            p.getPersistentDataContainer().set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING, "");
        }
        HashMap<Integer, ItemStack> vaultItems = vaultUtils.getItems(p);

        int size = RandomVault.getPlugin().getConfig().getInt(RandomVault.getPlugin().getServer().getWorlds().get(0).toString()+ ".size", 3)*9;

        Inventory vault = Bukkit.createInventory(p, size, "Vault");

        for (int i = 0; i < Math.min(vaultItems.size(), size) ; i++) {
            try {
                vault.setItem(i, vaultItems.get(i));
            } catch (NullPointerException ignored) {
            }
        }

        return vault;

    }


    public static void storeItem(HashMap<Integer, ItemStack> vaultContents, Player p) throws IOException {

        PersistentDataContainer data = p.getPersistentDataContainer();

        if(vaultContents.size() == 0){
            data.set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING, "");
        }else{

            try {
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.writeObject(vaultContents);
                os.flush();

                byte[] rawData = io.toByteArray();

                String encodedData = Base64.getEncoder().encodeToString(rawData);

                data.set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING, encodedData);

                os.close();

            }catch (IOException ex){
                System.out.println(ex);
            }
        }
    }

    public static HashMap<Integer, ItemStack> getItems(Player p){

        PersistentDataContainer data = p.getPersistentDataContainer();
        HashMap<Integer, ItemStack> vaultContents = new HashMap<>();

        String encodedData = data.get(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "RandomVault.vault"), PersistentDataType.STRING);


        if (!encodedData.isEmpty()){

            byte[] rawData = Base64.getDecoder().decode(encodedData);


            try {

                ByteArrayInputStream io = new ByteArrayInputStream(rawData);
                BukkitObjectInputStream in = new BukkitObjectInputStream(io);

                try {
                    vaultContents = (HashMap<Integer, ItemStack>) in.readObject();
                }catch (NullPointerException ignored) {
                    }
                in.close();
            }catch (IOException | ClassNotFoundException ex){
                System.out.println(ex);
            }

        }
        return vaultContents;
    }


}
