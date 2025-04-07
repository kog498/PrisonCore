package me.justfu498.prisoncore.prestige.database;

import org.bukkit.entity.Player;

import java.sql.*;

public class PrestigeDatabase {

    private final Connection connection;

    public PrestigeDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS prestigedata (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "prestige INTEGER NOT NULL DEFAULT 0)");
        }
    }

    public void addPlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prestigedata (uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getDisplayName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM prestigedata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerPrestige(Player player, int prestige) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE prestigedata SET prestige = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, prestige);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public int getPlayerPrestige(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT prestige FROM prestigedata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("prestige");
            } else {
                return 0;
            }
        }
    }

    public String getTopPlayerName(int index) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM prestigedata ORDER BY prestige DESC LIMIT 1 OFFSET " + (index-1))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                return "N/A";
            }
        }
    }

    public int getTopPlayerPrestige(int index) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT prestige FROM prestigedata ORDER BY prestige DESC LIMIT 1 OFFSET " + (index-1))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("prestige");
            } else {
                return 0;
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
