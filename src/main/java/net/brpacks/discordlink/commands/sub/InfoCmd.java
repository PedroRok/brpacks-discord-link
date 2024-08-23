package net.brpacks.discordlink.commands.sub;

import net.brpacks.core.common.commands.CommandManager;
import net.brpacks.core.common.commands.SubCommand;
import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/08/2024
 * @project BrPacksDiscordLink
 */
public class InfoCmd extends SubCommand {
    public InfoCmd(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected boolean onCommand(CommandSender commandSender, String[] strings) {
        if (strings.length < 1) return false;

        String playerName = strings[0];
        // Remove vinculação
        long discordId = LinkManager.get().getDatabase().getDiscordByPlayer(playerName);
        if (discordId <= 0) {
            sendMsg(commandSender, "<error>Jogador não vinculado!");
            return true;
        }

        sendMsg(commandSender, "<info><gradient:#54cffe:#ccf9ff>Informações do jogador");
        sendMsg(commandSender, "<info>Jogador: <gray>" + playerName);
        sendMsg(commandSender, "<info>Discord: <gray>" + Main.get().getBot().getUserName(discordId));
        sendMsg(commandSender, "<info>Discord ID: <gray>" + discordId);
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public String getUsage() {
        return "info <player>";
    }

    @Override
    public String getPermission() {
        return "brpacks.discord.info";
    }

    @Override
    public String getDescription() {
        return "Mostra informações do vinculo do jogador";
    }
}
