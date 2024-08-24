package net.brpacks.discordlink.commands.sub;

import net.brpacks.core.common.commands.CommandManager;
import net.brpacks.core.common.commands.SubCommand;
import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/08/2024
 * @project BrPacksDiscordLink
 */
public class RemoverCmd extends SubCommand {
    public RemoverCmd(CommandManager commandManager) {
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
        LinkManager.get().removeSync(discordId);
        sendMsg(commandSender, "<confirm>Vinculação removida com sucesso!");
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public String getUsage() {
        return "remover <player>";
    }

    @Override
    public String getPermission() {
        return "brpacks.discord.remover";
    }

    @Override
    public String getDescription() {
        return "Remove a vinculação de uma conta de Minecraft";
    }
}
