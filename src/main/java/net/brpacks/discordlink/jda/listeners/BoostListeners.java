package net.brpacks.discordlink.jda.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BoostListeners extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        Role boosterRole = event.getGuild().getBoostRole();
        if(event.getRoles().contains(boosterRole)) {
            // trigger na ação -> membro boostou o servidor
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        Role boosterRole = event.getGuild().getBoostRole();
        if(event.getRoles().contains(boosterRole)) {
            // trigger na ação -> membro removeu o boost
        }
    }
}
