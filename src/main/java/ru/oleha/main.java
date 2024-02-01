package ru.oleha;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.oleha.command.commandStorage;
import ru.oleha.config.configStorage;
import ru.oleha.events.event;

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
