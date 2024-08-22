package net.brpacks.discordlink;

import lombok.Getter;
import net.brpacks.discordlink.commands.LinkCommand;
import net.brpacks.discordlink.config.MainConfig;
import net.brpacks.discordlink.jda.Bot;
import org.apache.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("BrPacksDiscordLink");

    private static Main instance;
    private MainConfig mainConfig;

    @Override
    public void onEnable() {
        instance = this;
        mainConfig = new MainConfig(this);
        mainConfig.init();

        Bot bot = new Bot();
        bot.login();

        getServer().getPluginCommand("vincular").setExecutor(new LinkCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main get() {
        return instance;
    }
}
