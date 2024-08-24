package net.brpacks.discordlink;

import lombok.Getter;
import net.brpacks.core.common.utils.Cooldown;
import net.brpacks.core.common.utils.StringUtils;
import net.brpacks.core.common.utils.Tuple;
import net.brpacks.discordlink.config.RolesConfig;
import net.brpacks.discordlink.jda.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
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
    private final Map<Long, Tuple<UUID, String>> pendingLinks;
    private final Random random;
    private final LuckPerms luckPerms;

    @Getter
    private final LinkDatabase database;
    @Getter
    private final RolesConfig rolesConfig;


    public LinkManager() {
        this.pendingLinks = new HashMap<>();
        random = new Random();
        database = new LinkDatabase();
        luckPerms = Main.get().getLuckPerms();
        rolesConfig = new RolesConfig();
        rolesConfig.init();
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
        syncRoles(uuid);

        Main.get().getBot().getMemberById(userId, member -> {
            Main.LOGGER.info("Player " + uuid + " (" + remove.two() + ")" + " linked with " + userId + " (" + member.getUser().getName() + ")");
        });
        if (player == null) return;
        player.sendMessage(StringUtils.text("<confirm>Conta vinculada com sucesso!"));
    }

    private long generateCode() {
        long l;
        do {
            l = random.nextLong(10000, 99999);
        } while (pendingLinks.containsKey(l));
        return l;
    }

    public boolean isLinked(UUID uuid) {
        return database.isPlayerSync(uuid);
    }

    public boolean isClientLinked(long clientId) {
        return database.isClientSync(clientId);
    }

    public void syncRoles(UUID uuid) {
        long discordByPlayer = database.getDiscordByPlayer(uuid);
        if (discordByPlayer <= 0) return;
        Bot bot = Main.get().getBot();

        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) return;
        String primaryGroup = user.getPrimaryGroup();

        bot.getMemberById(discordByPlayer, member -> {
            for (Map.Entry<String, Long> entry : rolesConfig.getGroupToRoles().entrySet()) {
                bot.modifyPlayerRole(member, entry.getValue(), primaryGroup.equals(entry.getKey()));
            }
            boolean hasRole = member.getRoles().stream().anyMatch(role -> role.getIdLong() == rolesConfig.getBoosterRole());
            modifyBoosterRole(user, hasRole);
        });
    }


    public void modifyBoosterRole(User user, boolean hasRole) {
        boolean hasPermission = user.data().contains(Node.builder(rolesConfig.getBoosterPermissions().getFirst()).build(), NodeEqualityPredicate.IGNORE_VALUE).asBoolean();
        if (hasRole && !hasPermission) {
            for (String permission : rolesConfig.getBoosterPermissions()) {
                user.data().add(Node.builder(permission).build());
            }
            luckPerms.getUserManager().saveUser(user);
        } else if (!hasRole && hasPermission) {
            for (String permission : rolesConfig.getBoosterPermissions()) {
                user.data().remove(Node.builder(permission).build());
            }
            luckPerms.getUserManager().saveUser(user);
        }
    }

    public void removeSync(long discordId) {
        Bot bot = Main.get().getBot();
        User user = luckPerms.getUserManager().getUser(database.getPlayerByDiscord(discordId));
        if (user != null) {
            modifyBoosterRole(user, false);
        }
        database.removeSync(discordId);
        bot.getMemberById(discordId, member -> {
            for (Map.Entry<String, Long> entry : rolesConfig.getGroupToRoles().entrySet()) {
                bot.modifyPlayerRole(member, entry.getValue(), false);
            }
            Role roleById = member.getGuild().getRoleById(Main.get().getMainConfig().getVerifiedRoleId());
            if (roleById == null) return;
            member.getGuild().removeRoleFromMember(member, roleById).queue();
        });
    }

    public static LinkManager get() {
        if (instance == null) {
            instance = new LinkManager();
        }
        return instance;
    }
}
