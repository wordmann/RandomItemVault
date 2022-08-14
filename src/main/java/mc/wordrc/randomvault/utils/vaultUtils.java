package mc.wordrc.randomvault.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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

    public static void storeItem(HashMap<Integer, ItemStack> vaultContents, Player p) throws IOException {

        PersistentDataContainer data = p.getPersistentDataContainer();

        if(vaultContents.size() == 0){
            data.set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "vault"), PersistentDataType.STRING, "");
        }else{

            try {
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.writeObject(vaultContents);
                os.flush();

                byte[] rawData = io.toByteArray();

                String encodedData = Base64.getEncoder().encodeToString(rawData);

                data.set(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "vault"), PersistentDataType.STRING, encodedData);

                os.close();

            }catch (IOException ex){
                System.out.println(ex);
            }
        }
    }

    public static HashMap<Integer, ItemStack> getItems(Player p){

        PersistentDataContainer data = p.getPersistentDataContainer();
        HashMap<Integer, ItemStack> vaultContents = new HashMap<>();

        String encodedData = data.get(new NamespacedKey(mc.wordrc.randomvault.RandomVault.getPlugin(), "vault"), PersistentDataType.STRING);


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
