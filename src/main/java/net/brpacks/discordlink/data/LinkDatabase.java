package net.brpacks.discordlink.data;

import lombok.Getter;
import net.brpacks.core.common.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/08/2024
 * @project BrPacksDiscordLink
 */
@Getter
public class LinkDatabase extends Database {

    @Override
    public void setupDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS brp_discord_link (
                    player_uuid VARCHAR(36) PRIMARY KEY,
                    player_name VARCHAR(32) NOT NULL,
                    discord_id LONG NOT NULL
                );
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isClientSync(long clientId) {
        String sql = """
                SELECT * FROM brp_discord_link WHERE discord_id = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayerSync(@NotNull UUID uuid) {
        String sql = """
                SELECT * FROM brp_discord_link WHERE player_uuid = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayerSync(@NotNull String playerName) {
        String sql = """
                SELECT * FROM brp_discord_link WHERE player_name = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getDiscordByPlayer(@NotNull UUID uuid) {
        String sql = """
                SELECT discord_id FROM brp_discord_link WHERE player_uuid = ?;
                """;

        long discordId = -1;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("discord_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discordId;
    }

    public long getDiscordByPlayer(@NotNull String playerName) {
        String sql = """
                SELECT discord_id FROM brp_discord_link WHERE player_name = ?;
                """;

        long discordId = -1;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("discord_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discordId;
    }

    @Nullable
    public UUID getPlayerByDiscord(long discordId) {
        String sql = """
                SELECT player_uuid FROM brp_discord_link WHERE discord_id = ?;
                """;

        UUID playerUuid = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, discordId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString("player_uuid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerUuid;
    }

    public void removeSync(long discordId) {
        String sql = """
                DELETE FROM brp_discord_link WHERE discord_id = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, discordId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(@NotNull UUID uuid, String playerName, long discordId) {
        String sql = """
                INSERT INTO brp_discord_link (player_uuid, player_name, discord_id) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE discord_id = values(discord_id);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, playerName);
            statement.setLong(3, discordId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
