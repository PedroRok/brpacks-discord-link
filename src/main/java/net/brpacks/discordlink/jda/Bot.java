package net.brpacks.discordlink.jda;

import lombok.Getter;
import net.brpacks.discordlink.Main;
import net.brpacks.discordlink.jda.commands.CommandVincular;
import net.brpacks.discordlink.jda.listeners.BoostListeners;
import net.brpacks.discordlink.jda.listeners.ModalListeners;
import net.brpacks.discordlink.jda.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.JDALogger;
import xyz.xenondevs.invui.gui.Gui;

import java.util.function.Consumer;

@Getter
public class Bot {

    public JDA jda;
    public Guild guild;

    public void login() {
        JDALogger.setFallbackLoggerEnabled(false);
        jda = JDABuilder.createDefault(Main.get().getMainConfig().getToken())
                .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        jda.upsertCommand("vincular", "Vincula sua conta do Minecraft com a do Discord").queue();
        jda.addEventListener(new BoostListeners());
        jda.addEventListener(new ModalListeners());
        jda.addEventListener(new CommandVincular());
        jda.addEventListener(new ReadyListener(this));

        Main.LOGGER.info("Bot iniciado com sucesso!");
    }


    public void removeUserSync(long userId) {
        getMemberById(userId, member -> {
            Role roleById = member.getGuild().getRoleById(Main.get().getMainConfig().getVerifiedRoleId());
            if (roleById == null) return;
            member.getGuild().removeRoleFromMember(member, roleById).queue();
        });
    }

    public TextChannel getChannelById(long channelId) {
        for (Guild guild : jda.getGuilds()) {
            for (TextChannel textChannel : guild.getTextChannels()) {
                if (textChannel.getIdLong() == channelId) return textChannel;
            }
        }
        return null;
    }

    public void getMemberById(long userId, Consumer<Member> consumer) {
        guild.retrieveMemberById(userId).queue(member -> {
                if (member != null) {
                    consumer.accept(member);
                }
            }, throwable -> {
                Main.LOGGER.error("Erro ao obter o membro: " + throwable.getMessage());
            });
    }

    public void modifyPlayerRole(long memberId, long roleId, boolean add) {
        getMemberById(memberId, member -> {
            if (member.getRoles().stream().anyMatch(role -> role.getIdLong() == roleId) == add) return;
            Role roleById = guild.getRoleById(roleId);
            if (roleById == null) return;
            if (add) {
                guild.addRoleToMember(member, roleById).queue();
            } else {
                guild.removeRoleFromMember(member, roleById).queue();
            }
            if (!member.getUser().hasPrivateChannel()) return;

            member.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(":white_check_mark: Você recebeu o cargo " + roleById.getName() + " no servidor " + guild.getName()).queue();
            });
        });
    }

}
