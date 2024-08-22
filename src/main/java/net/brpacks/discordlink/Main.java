package net.brpacks.discordlink;

import lombok.Getter;
import net.brpacks.discordlink.config.MainConfig;
import net.brpacks.discordlink.jda.Bot;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    @Getter
    private MainConfig mainConfig;

    @Override
    public void onEnable() {
        instance = this;
        mainConfig = new MainConfig(this);
        mainConfig.init();

        Bot bot = new Bot();
        bot.login();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main get() {
        return instance;
    }
}
