package me.justfu498.prisoncore.token.database;

import org.bukkit.entity.Player;

import java.sql.*;

public class TokenDatabase {

    private final Connection connection;

    public TokenDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()){
            statement.execute("CREATE TABLE IF NOT EXISTS tokendata (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "token DOUBLE NOT NULL DEFAULT 0)");
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tokendata (uuid, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getDisplayName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tokendata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerToken(Player player, double value) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tokendata SET token = ? WHERE uuid = ?")) {
            preparedStatement.setDouble(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public double getPlayerToken(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM tokendata WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("token");
            } else {
                return 0;
            }
        }
    }

    public String getTopPlayerName(int index) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM tokendata ORDER BY token DESC LIMIT 1 OFFSET " + (index-1))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                return "N/A";
            }
        }
    }

    public double getTopPlayerToken(int index) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM tokendata ORDER BY token DESC LIMIT 1 OFFSET " + (index-1))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("token");
            } else {
                return 0;
            }
        }
    }
}
