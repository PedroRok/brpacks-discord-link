package net.brpacks.discordlink.config;

import lombok.Getter;
import net.brpacks.core.common.utils.XConfig;
import net.brpacks.discordlink.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
@Getter
public class RolesConfig extends XConfig {

    private final Map<String, Long> groupToRoles;
    private long boosterRole;
    private List<String> boosterPermissions;
    public RolesConfig() {
        super("roles.yml", Main.get());
        groupToRoles = new HashMap<>();
    }

    @Override
    public void init() {
        groupToRoles.clear();
        for (String group : config.getSection("group-to-role").getKeys(false)) {
            groupToRoles.put(group, config.getLong("group-to-role." + group));
        }

        boosterRole = config.getLong("booster.role-id");

        boosterPermissions = config.getStringList("booster.perm-list");
    }

    @Override
    public void save() {

    }

    @Override
    public void reload() {
        config.reloadConfig();
        init();
    }
}
