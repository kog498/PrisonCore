package me.justfu498.prisoncore.rank.database;

import org.bukkit.entity.Player;

import java.sql.*;

public class RankDatabase {

    private final Connection connection;

    public RankDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS rankdata (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "rank INTEGER NOT NULL DEFAULT 1)");
        }
    }

    public void addPlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO rankdata (uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getDisplayName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rankdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerRank(Player player, int rank) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE rankdata SET rank = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, rank);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public int getPlayerRank(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT rank FROM rankdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("rank");
            } else {
                return 1;
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
