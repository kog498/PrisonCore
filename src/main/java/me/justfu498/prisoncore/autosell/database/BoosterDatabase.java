package me.justfu498.prisoncore.autosell.database;

import org.bukkit.entity.Player;

import java.sql.*;

public class BoosterDatabase {

    private final Connection connection;

    public BoosterDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS sboosterdata (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "booster DOUBLE NOT NULL DEFAULT -1, " +
                    "duration INTEGER NOT NULL DEFAULT -1)");
        }
    }

    public void addPlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sboosterdata (uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getDisplayName());
            preparedStatement.executeUpdate();
        }
    }

    public void removePlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sboosterdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sboosterdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerBooster(Player player, double booster, int duration) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE sboosterdata SET booster = ?, duration = ? WHERE uuid = ?")) {
            preparedStatement.setDouble(1, booster);
            preparedStatement.setInt(2, duration);
            preparedStatement.setString(3, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePlayerBooster(Player player, int duration) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE sboosterdata SET duration = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, duration);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public ResultSet getPlayerBoosterData() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sboosterdata")) {
            return preparedStatement.executeQuery();
        }
    }

    public double getPlayerBooster(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT booster FROM sboosterdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("booster");
            } else {
                return -1;
            }
        }
    }

    public int getPlayerBoosterDuration(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT duration FROM sboosterdata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("duration");
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
