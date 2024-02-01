package ru.oleha.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class configStorage {
    private File file;
    private FileConfiguration fileConfiguration;
    public configStorage(Plugin plugin) {
        this(plugin.getDataFolder().getAbsolutePath() + "/storage.yml");
    }
    private configStorage(String path) {
        this.file = new File(path);
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }
    public boolean save() {
        try {
            this.fileConfiguration.save(this.file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void addItem(String userName, ItemStack itemStack,int slot,int id) {
        getConfig().set(userName + "." + id + "." + slot, itemStack);
        save();
    }
    public ItemStack getItem(String userName,int slot,int id) {
        return getConfig().getItemStack(userName + "." + id + "." + slot);
    }
    public void clearItems(String userName,int id) {
        getConfig().set(userName + "." + id,null);
        save();
    }
    public  FileConfiguration getConfig() {
        return this.fileConfiguration;
    }
}
