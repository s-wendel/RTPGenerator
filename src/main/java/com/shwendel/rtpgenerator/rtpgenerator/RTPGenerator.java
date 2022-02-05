package com.shwendel.rtpgenerator.rtpgenerator;

import com.shwendel.rtpgenerator.rtpgenerator.command.RandomTeleportCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class RTPGenerator extends JavaPlugin {

    private static RTPGenerator rtp;
    public HashMap<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        rtp = this;
        saveDefaultConfig();
        getCommand("rtp").setExecutor(new RandomTeleportCommand());
    }

    @Override
    public void onDisable() {
        rtp = null;
    }

    public static RTPGenerator getInstance() {
        return rtp;
    }

}
