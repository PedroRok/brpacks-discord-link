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
public class UpdateCmd extends SubCommand {
    public UpdateCmd(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected boolean onCommand(CommandSender commandSender, String[] strings) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.get(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                LinkManager.get().syncRoles(player.getUniqueId());
            });
            commandSender.sendMessage("<confirm>Permissões atualizadas com sucesso!");
        });
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return List.of();
    }

    @Override
    public String getUsage() {
        return "update";
    }

    @Override
    public String getPermission() {
        return "brpacks.discord.update";
    }

    @Override
    public String getDescription() {
        return "Atualiza as permissões do jogador";
    }
}
