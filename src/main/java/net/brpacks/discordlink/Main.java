package net.brpacks.discordlink;

import lombok.Getter;
import net.brpacks.core.MainCore;
import net.brpacks.discordlink.commands.LinkCommand;
import net.brpacks.discordlink.config.MainConfig;
import net.brpacks.discordlink.jda.Bot;
import net.luckperms.api.LuckPerms;
import org.apache.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("BrPacksDiscordLink");

    private static Main instance;
    private MainConfig mainConfig;

    @Getter
    private Bot bot;

    @Getter
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        mainConfig = new MainConfig(this);
        mainConfig.init();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        } else {
            LOGGER.error("LuckPerms not found! Disabling plugin...");
            getServer().shutdown();
        }

        getServer().getPluginManager().registerEvents(new LinkEvents(LinkManager.get()), this);

        bot = new Bot();
        bot.login();

        getServer().getPluginCommand("vincular").setExecutor(new LinkCommand());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LinkManager.get().getDatabase().close();
    }

    public static Main get() {
        return instance;
    }
}
