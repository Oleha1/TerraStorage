package ru.oleha.terrastorage;

import org.bukkit.plugin.java.JavaPlugin;
import ru.oleha.terrastorage.command.commandStorage;
import ru.oleha.terrastorage.config.configStorage;
import ru.oleha.terrastorage.events.event;

public final class main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new event(this),this);
        this.getCommand("storage").setExecutor(new commandStorage(this));
        configStorage configStorage = new configStorage(this);
        configStorage.save();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
