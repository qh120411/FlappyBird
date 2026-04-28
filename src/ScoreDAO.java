import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    private final PlayerDAO playerDAO = new PlayerDAO();

    public void saveScore(String playerName, Difficulty difficulty, int score, int correctAnswers, int totalQuestions) {
        String insertSql =
            "INSERT INTO scores(player_id, difficulty, score, correct_answers, total_questions) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            int playerId = playerDAO.findOrCreatePlayer(playerName, connection);
            if (playerId < 0) {
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setInt(1, playerId);
                statement.setString(2, difficulty == null ? Difficulty.MEDIUM.name() : difficulty.name());
                statement.setInt(3, score);
                statement.setInt(4, Math.max(correctAnswers, 0));
                statement.setInt(5, Math.max(totalQuestions, 0));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Khong luu duoc diem: " + e.getMessage());
        }
    }

    public List<LeaderboardEntry> fetchTopScores(int limit) {
        List<LeaderboardEntry> rows = new ArrayList<LeaderboardEntry>();
        String sql = "SELECT p.name, s.difficulty, s.score, s.correct_answers, s.total_questions, s.played_at "
            + "FROM scores s JOIN players p ON p.id = s.player_id "
            + "ORDER BY s.score DESC, s.played_at ASC LIMIT ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.max(1, limit));

            try (ResultSet resultSet = statement.executeQuery()) {
                int rank = 1;
                while (resultSet.next()) {
                    rows.add(new LeaderboardEntry(
                        rank++,
                        resultSet.getString("name"),
                        Difficulty.fromString(resultSet.getString("difficulty")),
                        resultSet.getInt("score"),
                        resultSet.getInt("correct_answers"),
                        resultSet.getInt("total_questions"),
                        resultSet.getString("played_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Khong tai duoc bang xep hang: " + e.getMessage());
        }

        return rows;
    }

    public void exportLeaderboardCsv(String outputPath, int limit) {
        List<LeaderboardEntry> rows = fetchTopScores(limit);
        List<String> lines = new ArrayList<String>();
        lines.add("rank,player,difficulty,score,accuracy,played_at");

        for (LeaderboardEntry row : rows) {
            String line = row.rank + "," + CsvUtil.escapeCsv(row.playerName) + "," + row.difficulty.name() + ","
                + row.score + "," + row.getAccuracyLabel() + "," + row.playedAt;
            lines.add(line);
        }

        try {
            Path target = Paths.get(outputPath);
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.write(target, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Khong xuat duoc file CSV bang xep hang: " + e.getMessage());
        }
    }

    public static class LeaderboardEntry {
        public final int rank;
        public final String playerName;
        public final Difficulty difficulty;
        public final int score;
        public final int correctAnswers;
        public final int totalQuestions;
        public final String playedAt;

        public LeaderboardEntry(
            int rank,
            String playerName,
            Difficulty difficulty,
            int score,
            int correctAnswers,
            int totalQuestions,
            String playedAt
        ) {
            this.rank = rank;
            this.playerName = playerName;
            this.difficulty = difficulty;
            this.score = score;
            this.correctAnswers = correctAnswers;
            this.totalQuestions = totalQuestions;
            this.playedAt = playedAt;
        }

        public String getAccuracyLabel() {
            if (totalQuestions <= 0) {
                return "0%";
            }
            double accuracy = (correctAnswers * 100.0) / totalQuestions;
            return String.format(java.util.Locale.US, "%.0f%%", accuracy);
        }
    }
}
