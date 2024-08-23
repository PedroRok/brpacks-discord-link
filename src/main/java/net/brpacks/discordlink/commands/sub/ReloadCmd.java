package net.brpacks.discordlink.commands.sub;

import net.brpacks.core.common.commands.CommandManager;
import net.brpacks.core.common.commands.SubCommand;
import net.brpacks.core.common.utils.StringUtils;
import net.brpacks.discordlink.LinkManager;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/08/2024
 * @project BrPacksDiscordLink
 */
public class ReloadCmd extends SubCommand {
    public ReloadCmd(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected boolean onCommand(CommandSender commandSender, String[] strings) {
        LinkManager.get().getRolesConfig().reload();
        commandSender.sendMessage(StringUtils.text("<confirm>Configurações de cargos recarregadas com sucesso!"));
        return false;
    }

    @Override
    protected List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return List.of();
    }

    @Override
    public String getUsage() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "brpacks.discord.reload";
    }

    @Override
    public String getDescription() {
        return "Recarrega a config";
    }
}
