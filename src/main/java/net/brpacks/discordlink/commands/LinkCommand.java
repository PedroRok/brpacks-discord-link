package net.brpacks.discordlink.commands;

import net.brpacks.core.common.commands.CommandManager;
import net.brpacks.core.common.utils.StringUtils;
import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import net.brpacks.discordlink.commands.sub.InfoCmd;
import net.brpacks.discordlink.commands.sub.ReloadCmd;
import net.brpacks.discordlink.commands.sub.RemoveCmd;
import net.brpacks.discordlink.commands.sub.UpdateCmd;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
public class LinkCommand extends CommandManager {

    private Component startMessagePart;
    private Component finalMessagePart;
    public LinkCommand() {
        super("vincular", "brpacks.discord", "<gray>[<blue>DISCORD<gray>] ");
        registerSubCommand("info", new InfoCmd(this));
        registerSubCommand("remove", new RemoveCmd(this));
        registerSubCommand("test", new UpdateCmd(this));
        registerSubCommand("reload", new ReloadCmd(this));
    }

    private void loadMessages() {
        TextChannel channelById = Main.get().getBot().getChannelById(Main.get().getMainConfig().getChatId());
        String channelName = channelById == null ? "#vincular-minecraft" : "#"+channelById.getName();

        startMessagePart = StringUtils.formatString("\n\n<gradient:#54cffe:#ccf9ff>Vinculando sua conta do Discord ao Minecraft</gradient>\n            ")
                .append(StringUtils.formatString("<gradient:#54cffe:#ccf9ff><u>discord.mundoalexey.com</gradient>\n")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, Main.get().getMainConfig().getDiscordLink()))
                        .hoverEvent(StringUtils.formatString("<gradient:#54cffe:#ccf9ff>Clique para abrir o discord")))
                .append(StringUtils.formatString("\n<blue>Para vincular sua conta, siga os passos abaixo:" +
                                                 "\n<#54cffe> 1 <blue>- <gray>No canal <gradient:#54cffe:#ccf9ff>" +
                                                 channelName
                                                 + "</gradient><gray>, digite <gradient:#54cffe:#ccf9ff>\"/vincular\"</gradient>\n" +
                                                 "<#8ee0fb> 2 <blue>- <gray>Seu código é: "));
        // key
        finalMessagePart = StringUtils.formatString("\n<#c9f5fb> 3 <blue>- <gradient:#54cffe:#ccf9ff>E Pronto!</gradient> <gray>Sua conta foi vinculada com sucesso!\n");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player p && (args.length == 0 || !sender.hasPermission("brpacks.mod"))) {
            if (LinkManager.get().isPending(p.getUniqueId())) {
                p.sendMessage(StringUtils.formatString("<negate>Você já possui um código pendente! <gray>Por favor, aguarde 5 minutos."));
                return true;
            }
            if (LinkManager.get().isLinked(p.getUniqueId())) {
                p.sendMessage(StringUtils.formatString("<negate>Você já está vinculado a uma conta!"));
                return true;
            }
            // load messages
            if (startMessagePart == null || finalMessagePart == null) loadMessages();

            long key = LinkManager.get().addPending(p.getUniqueId(), p.getName());
            Component code = StringUtils.formatString("<gradient:#54cffe:#ccf9ff><u>" + key + "</gradient>")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, key + ""))
                    .hoverEvent(StringUtils.formatString("<gradient:#54cffe:#ccf9ff>Clique para copiar o código"));
            p.sendMessage(startMessagePart.append(code).append(finalMessagePart));
            return true;
        }

        return super.onCommand(sender, cmd, label, args);
    }
}
