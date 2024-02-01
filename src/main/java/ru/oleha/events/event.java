package ru.oleha.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ru.oleha.config.config;
import ru.oleha.config.configStorage;
import ru.oleha.utils.utils;

import java.util.Arrays;
import java.util.Objects;


public class event implements Listener {
    Plugin plugin;
    config config;
    configStorage configStorage;
    utils utils;
    public event(Plugin plugin) {
        this.plugin = plugin;
        this.config = new config(plugin);
        this.utils = new utils();
        this.configStorage = new configStorage(plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        String userName = event.getPlayer().getName();
        if (config.isPlayer(userName)) {
            config.addPlayer(userName);
        }
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasMetadata(config.getStorageName())) {
            if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                event.setCancelled(true);
            }
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                ItemStack itemStack = event.getCurrentItem();
                NamespacedKey key = new NamespacedKey(plugin, "ID");
                try {
                    int id = itemStack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                    if (!config.isBuySlot(player.getName(), id)) {
                        utils.openConfirmBuy(player, config, plugin, id);
                    }
                } catch (NullPointerException ignored) {}
            }
        }
        if (player.hasMetadata(config.getConfirmBuyName())) {
            if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                event.setCancelled(true);
            }
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                ItemStack itemStack = event.getCurrentItem();
                NamespacedKey key = new NamespacedKey(plugin, "ID");
                NamespacedKey key2 = new NamespacedKey(plugin, "CLICK");
                NamespacedKey key3 = new NamespacedKey(plugin, "MATERIAL");
                NamespacedKey key4 = new NamespacedKey(plugin, "COUNT");
                try {
                    int id = itemStack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                    String click = itemStack.getItemMeta().getPersistentDataContainer().get(key2, PersistentDataType.STRING);
                    String material = itemStack.getItemMeta().getPersistentDataContainer().get(key3,PersistentDataType.STRING);
                    int count = itemStack.getItemMeta().getPersistentDataContainer().get(key4,PersistentDataType.INTEGER);
                    if (click != null) {
                        if (click.equals("acceptBuy")) {
                            ItemStack itemStack1 = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)),count);
                            if (player.getInventory().containsAtLeast(itemStack1,count)) {
                                player.getInventory().removeItem(itemStack1);
                                config.setBuySlot(player.getName(), id, true);
                                event.getInventory().close();
                                player.sendMessage(config.getSuccessfullyBuy());
                            } else {
                                player.sendMessage(config.getNotEnoughItem());
                            }
                        } else if (click.equals("denyBuy")) {
                            event.getInventory().close();
                        }
                    }
                } catch (NullPointerException ignored) {}
            }
        }
        NamespacedKey key = new NamespacedKey(plugin,"storageID");
        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            int id = data.get(key,PersistentDataType.INTEGER);
            if (player.hasMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)))) {
                if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                    event.setCancelled(true);
                }
                if (event.getCurrentItem() != null) {
                    if (!player.hasPermission("ru.oleha.storage.op")) {
                        if (!config.getBlockItems().isEmpty()) {
                            ItemStack itemStack = new ItemStack(event.getCurrentItem());
                            for (Object s : config.getBlockItems()) {
                                if (itemStack.getType().equals(Material.getMaterial(s.toString()))) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignore) {}
    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        NamespacedKey key = new NamespacedKey(plugin,"storageID");
        NamespacedKey key2 = new NamespacedKey(plugin,"otherPlayer");
        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            int id = data.get(key,PersistentDataType.INTEGER);
            String otherPlayer = data.get(key2,PersistentDataType.STRING);
            if (player.hasMetadata(config.getStorageName())) {
                player.removeMetadata(config.getStorageName(),plugin);
            }
            if (player.hasMetadata(config.getConfirmBuyName())) {
                player.removeMetadata(config.getConfirmBuyName(),plugin);
            }
            if (player.hasMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)))) {
                configStorage.clearItems(player.getName(),id);
                for (int i = 0; i < event.getInventory().getContents().length; i++) {
                    if (event.getInventory().getContents()[i] != null) {
                        configStorage.addItem(player.getName(), event.getInventory().getContents()[i],i,id);
                    }
                }
                player.removeMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)),plugin);
            }
            if (player.hasMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)) + " игрока: " + otherPlayer)) {
                configStorage.clearItems(otherPlayer,id);
                for (int i = 0; i < event.getInventory().getContents().length; i++) {
                    if (event.getInventory().getContents()[i] != null) {
                        configStorage.addItem(otherPlayer, event.getInventory().getContents()[i],i,id);
                    }
                }
                player.removeMetadata(config.getStorageSlotName().replace("%id%",String.valueOf(id)) + " игрока: " + otherPlayer,plugin);
            }
        } catch (NullPointerException ignore) {}
    }
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        World world = player.getWorld();
        if (event.getInventory().getType().equals(InventoryType.SHULKER_BOX)) {
            Location location = event.getInventory().getLocation();
            if (Objects.equals(location, config.getStorageLocation(world, config.getStorageID(world, location)))) {
                int id = config.getStorageID(world,location);
                if (config.getBuySlot(player.getName(),id)) {
                    event.setCancelled(true);
                    NamespacedKey key = new NamespacedKey(plugin,"storageID");
                    PersistentDataContainer data = player.getPersistentDataContainer();
                    data.set(key,PersistentDataType.INTEGER,id);
                    utils.openStorageSlot(player,config,plugin,id);
                } else {
                    event.setCancelled(true);
                    player.sendMessage(config.getNoBuyStorage());
                }
            }
        }
    }
}
