package net.brpacks.discordlink.jda;

import lombok.Getter;
import net.brpacks.discordlink.Main;
import net.brpacks.discordlink.jda.commands.CommandVincular;
import net.brpacks.discordlink.jda.listeners.ModalListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;

@Getter
public class Bot {

    public JDA jda;

    public void login() {
        JDALogger.setFallbackLoggerEnabled(false);
        jda = JDABuilder.createDefault(Main.get().getMainConfig().getToken())
                .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .build();

        jda.upsertCommand("vincular", "Vincula sua conta do Minecraft com a do Discord").queue();
        jda.addEventListener(new ModalListeners());
        jda.addEventListener(new CommandVincular());

        System.out.println("Bot iniciado com sucesso!");

    }

}
