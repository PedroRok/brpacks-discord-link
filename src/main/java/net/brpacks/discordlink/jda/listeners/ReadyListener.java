package net.brpacks.discordlink.jda.listeners;

import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import net.brpacks.discordlink.jda.Bot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/08/2024
 * @project BrPacksDiscordLink
 */
public class ReadyListener extends ListenerAdapter {

    private final Bot bot;

    public ReadyListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        bot.guild = event.getJDA().getGuildById(Main.get().getMainConfig().getGuildId());
    }
}