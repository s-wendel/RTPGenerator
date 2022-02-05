package com.shwendel.rtpgenerator.rtpgenerator.command;

import com.shwendel.rtpgenerator.rtpgenerator.RTPGenerator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RandomTeleportCommand implements CommandExecutor {

    private Random random = new Random();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("rtp")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RTPGenerator.getInstance().getConfig().getString("messages.non_player")));
                return true;
            }

            Player player = (Player) sender;
            HashMap<UUID, Long> cooldowns = RTPGenerator.getInstance().cooldowns;
            FileConfiguration config = RTPGenerator.getInstance().getConfig();

            if(!player.hasPermission(config.getString("permission"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RTPGenerator.getInstance().getConfig().getString("messages.permission")));
                return true;
            }

            if(cooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) >= config.getInt("cooldown") * 1000) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RTPGenerator.getInstance().getConfig().getString("messages.cooldown")));
                return true;
            }

            World world = Bukkit.getWorld(config.getString("world"));

            int maxX = config.getInt("maxX"), minX = config.getInt("minX");
            double x = random.nextInt(maxX - minX) + minX;
            x += 0.5;

            int maxZ = config.getInt("maxZ"), minZ = config.getInt("minZ");
            double z = random.nextInt(maxZ - minZ) + minZ;
            z += 0.5;

            Location location = new Location(world, x, 0, z);
            location.setY(world.getHighestBlockYAt(location));

            player.teleport(location);
            player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RTPGenerator.getInstance().getConfig().getString("messages.teleported")));

            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        }
        return true;
    }

}
