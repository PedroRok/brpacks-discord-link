package net.brpacks.discordlink.config;

import lombok.AccessLevel;
import lombok.Getter;
import net.brpacks.core.common.utils.XConfig;
import net.brpacks.discordlink.Main;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
@Getter
public class MainConfig extends XConfig {

    private String token;
    private long chatId;
    public MainConfig(Main main) {
        super("config.yml", main);
    }

    @Override
    public void init() {
        token = config.getString("discord.token");
        chatId = config.getLong("discord.chat_id");
    }

    @Override
    public void save() {

    }

    @Override
    public void reload() {

    }
}
