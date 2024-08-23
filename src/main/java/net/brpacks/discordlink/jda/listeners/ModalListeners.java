package net.brpacks.discordlink.jda.listeners;

import net.brpacks.discordlink.LinkManager;
import net.brpacks.discordlink.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class ModalListeners extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(event.getModalId().equals("sync:token")) {
            if(event.getGuild() == null) return;
            if(event.getMember() == null) return;
            ModalMapping inputTokenMapping = event.getValue("textinput:token");
            if(inputTokenMapping == null) return;
            String inputToken = inputTokenMapping.getAsString();
            long inputLong = 0;

            try {
                inputLong = Long.parseLong(inputToken);
            } catch (NumberFormatException ignore) {
                return;
            }

            LinkManager linkManager = LinkManager.get();

            UUID uuid = linkManager.getUUID(inputLong);
            if(uuid == null || inputLong == 0L) {
                event.reply(":x: O código digitado está inválido, por favor tente novamente!").setEphemeral(true).queue();
                return;
            }

            Role role = event.getGuild().getRoleById(Main.get().getMainConfig().getVerifiedRoleId());
            if(role == null) {
                Main.LOGGER.error("O cargo de verificado não está configurado corretamente.");
                event.reply("Houve um erro ao se verificar, tente novamente em outro momento")
                        .setEphemeral(true).queue();
                return;
            }

            // associar o membro ao banco de dados
            LinkManager.get().confirmSync(uuid, event.getMember().getIdLong(), inputLong);

            event.getGuild().addRoleToMember(event.getMember(), role)
                    .queue(then -> event.reply(":white_check_mark: Você foi verificado com sucesso!").setEphemeral(true).queue());

        }
    }
}
