package net.brpacks.discordlink;

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

    private Map<UUID, Long> pendingLinks;

    private Random random;

    public LinkManager() {
        this.pendingLinks = new HashMap<>();
        random = new Random();
    }

    public boolean isPending(UUID uuid) {
        return pendingLinks.containsKey(uuid);
    }

    public void addPending(UUID uuid) {
        pendingLinks.put(uuid, random.nextLong(10000, 99999));
    }

    public UUID getUUID(long code) {
        return pendingLinks.entrySet().stream().filter(entry -> entry.getValue() == code).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private long generateCode() {
        long l = random.nextLong(10000, 99999);
        if (pendingLinks.containsValue(l)) {
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
