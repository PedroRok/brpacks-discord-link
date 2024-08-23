package net.brpacks.discordlink.config;

import lombok.Getter;
import net.brpacks.core.common.utils.XConfig;
import net.brpacks.discordlink.Main;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
@Getter
public class MainConfig extends XConfig {

    private String token;
    private long chatId;
    private long verifiedRoleId;
    private String discordLink;
    private long guildId;
    public MainConfig(Main main) {
        super("config.yml", main);
    }

    @Override
    public void init() {
        token = config.getString("discord.token");
        chatId = config.getLong("discord.sync-channel");
        verifiedRoleId = config.getLong("discord.sync-role-id");
        discordLink = config.getString("discord.invite-link");
        guildId = config.getLong("discord.guild-id");
    }

    @Override
    public void save() {

    }

    @Override
    public void reload() {

    }
}
