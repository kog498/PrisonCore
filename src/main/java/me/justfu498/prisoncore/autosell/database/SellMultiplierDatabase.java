package me.justfu498.prisoncore.autosell.database;

import org.bukkit.entity.Player;

import java.sql.*;

public class SellMultiplierDatabase {

    private final Connection connection;

    public SellMultiplierDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS smultidata (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "multiplier DOUBLE NOT NULL DEFAULT 1.0, " +
                    "booster DOUBLE NOT NULL DEFAULT -1, " +
                    "timestart INTEGER NOT NULL DEFAULT -1, " +
                    "duration INTEGER NOT NULL DEFAULT -1)");
        }
    }

    public void addPlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO smultidata (uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getDisplayName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM smultidata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerMultiplier(Player player, double multiplier) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE smultidata SET multiplier = ? WHERE uuid = ?")) {
            preparedStatement.setDouble(1, multiplier);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public double getPlayerMultiplier(Player player) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT multiplier FROM smultidata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("multiplier");
            } else {
                return 0.0;
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}