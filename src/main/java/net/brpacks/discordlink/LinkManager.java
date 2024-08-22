package net.brpacks.discordlink;

import net.brpacks.core.common.utils.Cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
public class LinkManager {

    private static LinkManager instance;

    private Map<Long, UUID> pendingLinks;

    private Random random;

    public LinkManager() {
        this.pendingLinks = new HashMap<>();
        random = new Random();
    }

    public boolean isPending(UUID uuid) {
        return Cooldown.isInCooldown(uuid, "discord-link");
    }

    public long addPending(UUID uuid) {
        long key = random.nextLong(10000, 99999);
        pendingLinks.put(key, uuid);
        Cooldown.setCooldownSec(uuid, 60 * 5L, "discord-link");
        return key;
    }

    public UUID getUUID(long code) {
        return pendingLinks.get(code);
    }

    private long generateCode() {
        long l = random.nextLong(10000, 99999);
        if (pendingLinks.containsKey(l)) {
            return generateCode();
        }
        return l;
    }

    public static LinkManager get() {
        if (instance == null) {
            instance = new LinkManager();
        }
        return instance;
    }
}
