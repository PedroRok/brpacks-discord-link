package net.brpacks.discordlink.jda.listeners;

import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BoostListeners extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        Role boosterRole = event.getGuild().getBoostRole();
        if(event.getRoles().contains(boosterRole)) {
            UUID playerByDiscord = LinkManager.get().getDatabase().getPlayerByDiscord(event.getMember().getIdLong());
            if (playerByDiscord == null) return;

            CompletableFuture<User> userFuture = Main.get().getLuckPerms().getUserManager().loadUser(playerByDiscord);

            userFuture.thenAcceptAsync(user -> {
                LinkManager.get().modifyBoosterRole(user, true);
            });
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        Role boosterRole = event.getGuild().getBoostRole();
        if(event.getRoles().contains(boosterRole)) {
            UUID playerByDiscord = LinkManager.get().getDatabase().getPlayerByDiscord(event.getMember().getIdLong());
            if (playerByDiscord == null) return;

            CompletableFuture<User> userFuture = Main.get().getLuckPerms().getUserManager().loadUser(playerByDiscord);

            userFuture.thenAcceptAsync(user -> {
                LinkManager.get().modifyBoosterRole(user, false);
            });
        }
    }
}
