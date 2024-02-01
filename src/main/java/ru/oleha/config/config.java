package ru.oleha.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class config {
    private Plugin plugin;
    public config(Plugin plugin) {
        this.plugin = plugin;
    }
    public void addPlayer(String userName) {
        this.plugin.getConfig().set("players." + userName,null);
        for (int i = 1; i <= 9; i++) {
            this.plugin.getConfig().set("players." + userName + ".storage_" + i,false);
        }
        this.plugin.saveConfig();
    }
    public boolean isPlayer(String userName) {
        return this.plugin.getConfig().get("players." + userName) == null;
    }
    public boolean isBuySlot(String userName, int slot) {
        return this.plugin.getConfig().getBoolean("players." + userName +".storage_" + slot);
    }
    public void setBuySlot(String userName, int slot, boolean b) {
        this.plugin.getConfig().set("players." + userName +".storage_" + slot,b);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }
    public boolean getBuySlot(String userName,int slot) {
        return this.plugin.getConfig().getBoolean("players." + userName +".storage_" + slot);
    }
    public void addStorage(int id,int x,int y,int z) {
        this.plugin.getConfig().set("storage.storage_" + id +".id",id);
        this.plugin.getConfig().set("storage.storage_" + id +".posX",x);
        this.plugin.getConfig().set("storage.storage_" + id +".posY",y);
        this.plugin.getConfig().set("storage.storage_" + id +".posZ",z);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }
    public void removeStorage(int id) {
        this.plugin.getConfig().set("storage.storage_" + id,null);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }
    public int getStorageID(World world, Location location) {
        for (int i = 1; i <= 9; i++) {
            if (location.equals(getStorageLocation(world,i))) {
                return this.plugin.getConfig().getInt("storage.storage_" + i +".id");
            }
        }
        return -1;
    }
    public Location getStorageLocation(World world, int id) {
        int x = this.plugin.getConfig().getInt("storage.storage_" + id +".posX");
        int y = this.plugin.getConfig().getInt("storage.storage_" + id +".posY");
        int z =this.plugin.getConfig().getInt("storage.storage_" + id +".posZ");
        return new Location(world,x,y,z);
    }
    public List getBlockItems() {
        return this.plugin.getConfig().getList("settings.blockItem");
    }
    public int getStorageSize() {
        return this.plugin.getConfig().getInt("UI.storage.size");
    }
    public int getStorageSlotSize() {
        return this.plugin.getConfig().getInt("UI.storageSlot.size");
    }
    public String getStorageName() {
        return this.plugin.getConfig().getString("UI.storage.title");
    }
    public String getStorageSlotName() {
        return this.plugin.getConfig().getString("UI.storageSlot.title");
    }
    public List getStorageItemList() {
        return this.plugin.getConfig().getList("UI.storage.items");
    }
    public int getConfirmBuySize() {
        return this.plugin.getConfig().getInt("UI.confirmBuy.size");
    }
    public String getConfirmBuyName() {
        return this.plugin.getConfig().getString("UI.confirmBuy.title");
    }
    public List getConfirmBuyItemList() {
        return this.plugin.getConfig().getList("UI.confirmBuy.items");
    }
    public String getSuccessfullyBuy() {
        return this.plugin.getConfig().getString("settings.message.successfullyBuy");
    }
    public String getSuccessfullyAddStorage() {
        return this.plugin.getConfig().getString("settings.message.successfullyAddStorage");
    }
    public String getSuccessfullyRemoveStorage() {
        return this.plugin.getConfig().getString("settings.message.successfullyRemoveStorage");
    }
    public String getNotEnoughItem() {
        return this.plugin.getConfig().getString("settings.message.notEnoughItem");
    }
    public String getNoBuyStorage() {
        return this.plugin.getConfig().getString("settings.message.noBuyStorage");
    }
    public String getLockStorage() {
        return this.plugin.getConfig().getString("settings.message.lockStorage");
    }
    public String getUnLockStorage() {
        return this.plugin.getConfig().getString("settings.message.unLockStorage");
    }
    public String getLockOtherStorage() {
        return this.plugin.getConfig().getString("settings.message.lockOtherStorage");
    }
    public String getUnLockOtherStorage() {
        return this.plugin.getConfig().getString("settings.message.unLockOtherStorage");
    }
    public String getError() {
        return this.plugin.getConfig().getString("settings.message.error");
    }
    public String getReloadConfig() {
        return this.plugin.getConfig().getString("settings.message.reloadConfig");
    }
}