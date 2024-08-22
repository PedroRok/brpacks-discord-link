package net.brpacks.discordlink.jda.listeners;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

public class ModalListeners extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(event.getId().equals("sync:token")) {
            ModalMapping inputTokenMapping = event.getValue("textinput:token");
            if(inputTokenMapping == null) return;
            String inputToken = inputTokenMapping.getAsString();

            // Checker se o inputToken digitado é válido:
            // Se for válido, vincular a conta do Discord com a conta do Minecraft.

        }
    }
}
