package net.brpacks.discordlink.jda.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class CommandVincular extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("vincular")) {

            if(event.getMember() == null) return;
            if(event.getGuild() == null) return;

            TextInput textInput = TextInput.create("textinput:token", "Digite o código", TextInputStyle.SHORT)
                    .setPlaceholder("Digite o código informado no chat.")
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("sync:token", "Sincronização de Contas")
                    .addActionRow(textInput)
                    .build();

            event.replyModal(modal).queue();
        }
    }
}
