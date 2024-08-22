package net.brpacks.discordlink;

import net.brpacks.core.common.utils.Cooldown;
import net.brpacks.discordlink.data.LinkDatabase;

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

    private final LinkDatabase linkDatabase;

    public LinkManager() {
        this.pendingLinks = new HashMap<>();
        random = new Random();
        linkDatabase = new LinkDatabase();
    }

    public boolean isPending(UUID uuid) {
        return Cooldown.isInCooldown(uuid, "discord-link");
    }

    public long addPending(UUID uuid) {
        long key = generateCode();
        pendingLinks.put(key, uuid);
        Cooldown.setCooldownSec(uuid, 60 * 5L, "discord-link");
        return key;
    }

    public UUID getUUID(long code) {
        return pendingLinks.get(code);
    }

    public void confirmSync(UUID uuid, long userId, long code) {
        pendingLinks.remove(code);
        Cooldown.removeCooldown(uuid, "discord-link");
        linkDatabase.savePlayer(uuid, userId);
    }

    private long generateCode() {
        long l = random.nextLong(10000, 99999);
        if (pendingLinks.containsKey(l)) {
            return generateCode();
        }
        return l;
    }

    private boolean isLinked(UUID uuid) {
        return linkDatabase.isPlayerSync(uuid);
    }

    private boolean isClientLinked(long clientId) {
        return linkDatabase.isClientSync(clientId);
    }

    public static LinkManager get() {
        if (instance == null) {
            instance = new LinkManager();
        }
        return instance;
    }
}
