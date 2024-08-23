package net.brpacks.discordlink;

import net.brpacks.core.common.utils.Cooldown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/08/2024
 * @project BrPacksDiscordLink
 */
public class LinkEvents implements Listener {

    private final LinkManager manager;

    public LinkEvents(LinkManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAsynJoin(AsyncPlayerPreLoginEvent event) {
        if (Cooldown.isInCooldown(event.getUniqueId(), "discord-sync-roles")) return;
        if (!manager.getDatabase().isPlayerSync(event.getUniqueId())) return;
        Cooldown.setCooldownSec(event.getUniqueId(), 60 * 2L, "discord-sync-roles");
        manager.syncRoles(event.getUniqueId());
    }
}
