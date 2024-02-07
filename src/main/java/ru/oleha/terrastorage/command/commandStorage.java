package ru.oleha.terrastorage.command;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.oleha.terrastorage.config.config;
import ru.oleha.terrastorage.utils.utils;

import java.util.ArrayList;
import java.util.List;

public class commandStorage implements TabExecutor {
    Plugin plugin;
    public commandStorage(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        config config = new config(this.plugin);
        utils utils = new utils();
        if (strings.length == 0) {
            utils.openStorage(player, config, plugin);
        }
        if (player.hasPermission("ru.oleha.storage.op")) {
            if (strings.length == 0) {
                utils.openStorage(player, config, plugin);
            } else {
                if (strings[0].equals("add")) {
                    if (!strings[1].isEmpty()) {
                        Block block = player.getTargetBlock(20);
                        if (block != null) {
                            config.addStorage(Integer.parseInt(strings[1]), block.getX(), block.getY(), block.getZ());
                            player.sendMessage(config.getSuccessfullyAddStorage().replace("%id%", strings[1]));
                        }
                    }
                } else if (strings[0].equals("storage")) {
                    Player otherPlayer = plugin.getServer().getPlayer(strings[1]);
                    if (otherPlayer != null) {
                        config.setBuySlot(otherPlayer.getName(), Integer.parseInt(strings[3]), !strings[2].equals("lock"));
                        if (!strings[2].equals("lock")) {
                            otherPlayer.sendMessage(config.getUnLockOtherStorage().replace("%id%", strings[3]));
                        } else {
                            otherPlayer.sendMessage(config.getLockOtherStorage().replace("%id%", strings[3]));
                        }
                        if (!strings[2].equals("lock")) {
                            player.sendMessage(config.getUnLockStorage().replace("%id%", strings[3]).replace("%player%", strings[1]));
                        } else {
                            player.sendMessage(config.getLockStorage().replace("%id%", strings[3]).replace("%player%", strings[1]));
                        }
                    } else {
                        player.sendMessage(config.getError());
                    }
                } else if (strings[0].equals("delete")) {
                    if (!strings[1].isEmpty()) {
                        config.removeStorage(Integer.parseInt(strings[1]));
                        player.sendMessage(config.getSuccessfullyRemoveStorage().replace("%id%", strings[1]));
                    }
                } else if (strings[0].equals("reload")) {
                    this.plugin.reloadConfig();
                    player.sendMessage(config.getReloadConfig());
                } else if (strings[0].equals("open")) {
                    Player otherPlayer = plugin.getServer().getPlayer(strings[1]);
                    if (otherPlayer != null) {
                        NamespacedKey key = new NamespacedKey(plugin, "otherPlayer");
                        PersistentDataContainer data = player.getPersistentDataContainer();
                        data.set(key, PersistentDataType.STRING, otherPlayer.getName());
                        utils.openOtherStorageSlot(player, otherPlayer, config, plugin, Integer.parseInt(strings[2]));
                    } else {
                        player.sendMessage(config.getError());
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (player.hasPermission("ru.oleha.storage.op")) {
            if (strings.length == 1) {
                List<String> text = new ArrayList<>();
                text.add("add");
                text.add("delete");
                text.add("storage");
                text.add("reload");
                text.add("open");
                return text;
            }
            if (strings.length == 2) {
                if (strings[0].equals("add") || strings[0].equals("delete")) {
                    List<String> text = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) {
                        text.add(String.valueOf(i));
                    }
                    return text;
                }
            }
            if (strings.length == 3) {
                if (strings[0].equals("storage")) {
                    List<String> text = new ArrayList<>();
                    text.add("lock");
                    text.add("unlock");
                    return text;
                }
                if (strings[0].equals("open")) {
                    List<String> text = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) {
                        text.add(String.valueOf(i));
                    }
                    return text;
                }
            }
            if (strings.length == 4) {
                if (strings[0].equals("storage")) {
                    List<String> text = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) {
                        text.add(String.valueOf(i));
                    }
                    return text;
                }
            }
        }
        return null;
    }
}
