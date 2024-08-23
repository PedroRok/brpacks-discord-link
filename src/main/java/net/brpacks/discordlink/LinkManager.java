package net.brpacks.discordlink;

import lombok.Getter;
import net.brpacks.core.common.utils.Cooldown;
import net.brpacks.core.common.utils.StringUtils;
import net.brpacks.core.common.utils.Tuple;
import net.brpacks.discordlink.data.LinkDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    private Map<Long, Tuple<UUID, String>> pendingLinks;

    private final Random random;

    @Getter
    private final LinkDatabase database;

    public LinkManager() {
        this.pendingLinks = new HashMap<>();
        random = new Random();
        database = new LinkDatabase();
    }

    public boolean isPending(UUID uuid) {
        return Cooldown.isInCooldown(uuid, "discord-link");
    }

    public long addPending(UUID uuid, String name) {
        long key = generateCode();
        pendingLinks.put(key, new Tuple<>(uuid, name));
        Cooldown.setCooldownSec(uuid, 60 * 5L, "discord-link");
        return key;
    }

    public UUID getUUID(long code) {
        return pendingLinks.get(code).one();
    }

    public void confirmSync(UUID uuid, long userId, long code) {
        Tuple<UUID, String> remove = pendingLinks.remove(code);
        Cooldown.removeCooldown(uuid, "discord-link");
        database.savePlayer(uuid, remove.two(), userId);

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.sendMessage(StringUtils.text("<confirm>Conta vinculada com sucesso!"));
    }

    private long generateCode() {
        long l = random.nextLong(10000, 99999);
        if (pendingLinks.containsKey(l)) {
            return generateCode();
        }
        return l;
    }

    private boolean isLinked(UUID uuid) {
        return database.isPlayerSync(uuid);
    }

    private boolean isClientLinked(long clientId) {
        return database.isClientSync(clientId);
    }

    public static LinkManager get() {
        if (instance == null) {
            instance = new LinkManager();
        }
        return instance;
    }
}
