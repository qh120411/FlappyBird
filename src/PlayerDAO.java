import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {

    public int findOrCreatePlayer(String rawName, Connection connection) throws SQLException {
        String playerName = normalize(rawName);

        String selectSql = "SELECT id FROM players WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        String insertSql = "INSERT INTO players(name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        return -1;
    }

    private String normalize(String rawName) {
        if (rawName == null || rawName.trim().isEmpty()) {
            return "Nguoi choi";
        }

        String trimmed = rawName.trim();
        return trimmed.length() > 50 ? trimmed.substring(0, 50) : trimmed;
    }
}
