package ru.oleha.terrastorage.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ru.oleha.terrastorage.config.config;
import ru.oleha.terrastorage.config.configStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class utils {
    public utils() {

    }
    public void openStorage(Player player, config config, Plugin plugin) {
        List items = config.getStorageItemList();
        Inventory inventory = Bukkit.createInventory(player, config.getStorageSize(), config.getStorageName());
        for (Object item : items) {
            String material = getMaterial(item.toString());
            int slot = getSlot(item.toString());
            int id = getId(item.toString());
            String name = getDisplayName(item.toString()).replace("%id%",String.valueOf(id));
            String isBuyItem = getIsBuyItem(item.toString());
            String click = getClick(item.toString());
            String buyItem = getBuyItem(item.toString());
            int count = getCount(item.toString());
            String isBuySlotText = getBuySlotText(item.toString());
            ArrayList<String> oldLors = getLors(item.toString());
            ArrayList<String> newLors = new ArrayList<>();
            for (String lor : oldLors) {
                newLors.add(color.col(lor.replace("%buyItem%", new ItemStack(Material.getMaterial(buyItem)).getI18NDisplayName()).replace("%count%",String.valueOf(count))));
            }
            if (Material.getMaterial(material) != null) {
                ItemStack itemStack;
                if (config.isBuySlot(player.getName(),id)) {
                    itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(isBuyItem)));
                } else {
                    itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "ID");
                NamespacedKey key2 = new NamespacedKey(plugin, "CLICK");
                NamespacedKey key3 = new NamespacedKey(plugin, "MATERIAL");
                NamespacedKey key4 = new NamespacedKey(plugin, "COUNT");
                itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, id);
                itemMeta.getPersistentDataContainer().set(key2, PersistentDataType.STRING, click);
                itemMeta.getPersistentDataContainer().set(key3, PersistentDataType.STRING, buyItem);
                itemMeta.getPersistentDataContainer().set(key4, PersistentDataType.INTEGER, count);
                if (!config.isBuySlot(player.getName(),id)) {
                    if (!name.equals("null")) {
                        itemMeta.setDisplayName(name);
                    }
                } else {
                    itemMeta.setDisplayName(isBuySlotText);
                }
                if (!config.isBuySlot(player.getName(),id)) {
                    if (!newLors.isEmpty()) {
                        itemMeta.setLore(newLors);
                    }
                }
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(slot, itemStack);
            }
        }
        player.openInventory(inventory);
        player.setMetadata(config.getStorageName(),new FixedMetadataValue(plugin,config.getStorageName()));
    }
    public void openConfirmBuy(Player player, config config, Plugin plugin,int id,String buyItem, int count) {
        List items = config.getConfirmBuyItemList();
        Inventory inventory = Bukkit.createInventory(player, config.getConfirmBuySize(), config.getConfirmBuyName());
        for (Object item : items) {
            String material = getMaterial(item.toString());
            int slot = getSlot(item.toString());
            String name = getDisplayName(item.toString());
            String isBuyItem = getIsBuyItem(item.toString());
            String click = getClick(item.toString());
            if (Material.getMaterial(material) != null) {
                ItemStack itemStack;
                if (config.isBuySlot(player.getName(),id)) {
                    itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(isBuyItem)));
                } else {
                    itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "ID");
                NamespacedKey key2 = new NamespacedKey(plugin, "CLICK");
                NamespacedKey key3 = new NamespacedKey(plugin, "MATERIAL");
                NamespacedKey key4 = new NamespacedKey(plugin, "COUNT");
                itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, id);
                itemMeta.getPersistentDataContainer().set(key2, PersistentDataType.STRING, click);
                itemMeta.getPersistentDataContainer().set(key3, PersistentDataType.STRING, buyItem);
                itemMeta.getPersistentDataContainer().set(key4, PersistentDataType.INTEGER, count);
                if (!name.equals("null")) {
                    itemMeta.setDisplayName(name.replace("%id%",String.valueOf(id)));
                }
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(slot, itemStack);
            }
        }
        player.openInventory(inventory);
        player.setMetadata(config.getConfirmBuyName(),new FixedMetadataValue(plugin,config.getConfirmBuyName()));
    }
    public void openStorageSlot(Player player, config config, Plugin plugin,int id) {
        configStorage configStorage = new configStorage(plugin);
        Inventory inventory = Bukkit.createInventory(player, config.getStorageSlotSize(), config.getStorageSlotName().replace("%id%",String.valueOf(id)));
        for (int i = 0; i < config.getStorageSlotSize(); i++) {
            inventory.setItem(i,configStorage.getItem(player.getName(),i,id));
        }
        player.openInventory(inventory);
        player.setMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)),new FixedMetadataValue(plugin,config.getStorageSlotName().replace("%id%",String.valueOf(id))));
    }
    public void openOtherStorageSlot(Player player,Player otherPlayer, config config, Plugin plugin,int id) {
        configStorage configStorage = new configStorage(plugin);
        Inventory inventory = Bukkit.createInventory(player, config.getStorageSlotSize(), config.getStorageSlotName().replace("%id%",String.valueOf(id)) + " игрока: " + otherPlayer.getName());
        for (int i = 0; i < config.getStorageSlotSize(); i++) {
            inventory.setItem(i,configStorage.getItem(otherPlayer.getName(),i,id));
        }
        player.openInventory(inventory);
        player.setMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)) + " игрока: " + otherPlayer.getName(),new FixedMetadataValue(plugin,config.getStorageSlotName().replace("%id%",String.valueOf(id)) + " игрока: " + otherPlayer.getName()));
    }
    private String getMaterial(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(stringBuilder.indexOf("="),stringBuilder.length());
        return color.col(stringBuilder.toString());
    }
    private int getSlot(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf("=") + 1);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return Integer.parseInt(stringBuilder.toString().replace("{slot=","")) - 1;
    }
    private String getDisplayName(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf("=") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return color.col(stringBuilder.toString().replace("displayName=",""));
    }
    private String getIsBuyItem(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf("=") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return color.col(stringBuilder.toString().replace("isBuyItem=",""));
    }
    private int getId(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf("=") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return Integer.parseInt(stringBuilder.toString().replace("id=",""));
    }
    private String getClick(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return color.col(stringBuilder.toString().replace("click=",""));
    }
    private String getBuyItem(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return color.col(stringBuilder.toString().replace("buyItem=",""));
    }
    private int getCount(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return Integer.parseInt(stringBuilder.toString().replace("count=",""));
    }
    private String getBuySlotText(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(stringBuilder.indexOf(","),stringBuilder.length());
        return color.col(stringBuilder.toString().replace("isBuySlotText=",""));
    }
    private ArrayList<String> getLors(String s) {
        StringBuilder stringBuilder = new StringBuilder(s.substring(1));
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 1);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        stringBuilder.delete(0,stringBuilder.indexOf(",") + 2);
        String[] arr = stringBuilder.toString().replace("Lors=","").replace("[","").replace("}}","").replace("]","").split(", ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(arr));
        if (arrayList.get(0).equals("null")) {
            arrayList.clear();
        }
        return arrayList;
    }
}
