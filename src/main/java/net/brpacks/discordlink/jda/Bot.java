package net.brpacks.discordlink.jda;

import lombok.Getter;
import net.brpacks.discordlink.Main;
import net.brpacks.discordlink.jda.commands.CommandVincular;
import net.brpacks.discordlink.jda.listeners.BoostListeners;
import net.brpacks.discordlink.jda.listeners.ModalListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.JDALogger;

@Getter
public class Bot {

    public JDA jda;

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

        Main.LOGGER.info("Bot iniciado com sucesso!");
    }

    public void removeUserSync(long userId) {
        for (User user : jda.getUsers()) {
            if (user.getIdLong() != userId) continue;
            Guild guild = user.getMutualGuilds().getFirst();
            Role roleById = guild.getRoleById(Main.get().getMainConfig().getVerifiedRoleId());
            if (roleById == null) return;
            guild.removeRoleFromMember(user, roleById).queue();
            return;
        }
    }

    public String getUserName(long userId) {
        for (User user : jda.getUsers()) {
            if (user.getIdLong() == userId) return user.getName();
        }
        return null;
    }



}
