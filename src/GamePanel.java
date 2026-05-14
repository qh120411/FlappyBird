import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    private static final int UPDATE_INTERVAL_MS = 16; // ~60 FPS
    private static final String LEADERBOARD_EXPORT_PATH = "data/leaderboard_export.csv";
    private static final int LEADERBOARD_TOP_ENTRIES = 20;

    private final Keyboard keyboard;
    private final Game game;
    private final String playerName;
    private final Difficulty difficulty;
    private final ScoreDAO scoreDAO;
    private final Runnable backToMenuAction;
    private final Runnable openLeaderboardAction;

    private final Timer timer;
    private boolean gameOverDialogShown;

    public GamePanel(
            String playerName,
            Difficulty difficulty,
            ScoreDAO scoreDAO,
            Runnable backToMenuAction,
            Runnable openLeaderboardAction) {
        this.keyboard = Keyboard.getInstance();
        this.difficulty = difficulty == null ? Difficulty.MEDIUM : difficulty;
        this.game = new Game(this.difficulty);
        this.playerName = normalizePlayerName(playerName);
        this.scoreDAO = scoreDAO;
        this.backToMenuAction = backToMenuAction;
        this.openLeaderboardAction = openLeaderboardAction;

        setBackground(Color.BLACK);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        GameInputBinder.bindGameKeys(this, keyboard);

        timer = new Timer(UPDATE_INTERVAL_MS, e -> tick());
        timer.start();

        SwingUtilities.invokeLater(this::requestGameFocus);
    }

    public void requestGameFocus() {
        keyboard.clear();
        requestFocusInWindow();
    }

    public void stop() {
        timer.stop();
    }

    private void tick() {
        game.update();

        if (game.consumeQuizTrigger()) {
            showQuiz();
        }

        if (game.consumeGameOverEvent() && !gameOverDialogShown) {
            gameOverDialogShown = true;
            persistScore();
            showGameOverDialog();
        }

        repaint();
    }

    private void showQuiz() {
        keyboard.clear();
        Question question = QuizManager.getInstance().nextQuestion(difficulty);
        boolean correct = QuizDialog.showQuiz(this, question);
        keyboard.clear();
        game.applyQuizResult(correct);
        requestGameFocus();
    }

    private void persistScore() {
        scoreDAO.saveScore(playerName, difficulty, game.score, game.getQuizzesCorrect(), game.getQuizzesAsked());
        scoreDAO.exportLeaderboardCsv(LEADERBOARD_EXPORT_PATH, LEADERBOARD_TOP_ENTRIES);
    }

    private void showGameOverDialog() {
        keyboard.clear();
        GameOverDialog.Result result = GameOverDialog.showDialog(
                this,
                difficulty,
                game.score,
                game.getQuizzesCorrect(),
                game.getQuizzesAsked());
        keyboard.clear();

        if (result == GameOverDialog.Result.RESTART) {
            game.restart();
            gameOverDialogShown = false;
            requestGameFocus();
            return;
        }

        if (result == GameOverDialog.Result.LEADERBOARD) {
            openLeaderboardAction.run();
            SwingUtilities.invokeLater(this::showGameOverDialog);
            return;
        }

        stop();
        backToMenuAction.run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        for (Render render : game.getRenders()) {
            if (render.transform != null) {
                g2D.drawImage(render.image, render.transform, null);
            } else {
                g2D.drawImage(render.image, render.x, render.y, null);
            }
        }

        drawHud(g2D);
    }

    private void drawHud(Graphics2D g2D) {
        g2D.setColor(Color.BLACK);
        g2D.setFont(ThemeConstants.FONT_HUD_TITLE);
        g2D.drawString("Diem: " + game.score, 12, 28);

        g2D.setFont(ThemeConstants.FONT_BODY);
        g2D.drawString("Nguoi choi: " + playerName, 12, 48);
        g2D.drawString("Do kho: " + difficulty.getLabel(), 12, 66);

        if (game.getState() == GameState.READY) {
            drawCenterText(g2D, "Nhan SPACE de bat dau", 240, ThemeConstants.FONT_HUD_TITLE);
        }

        if (game.getState() == GameState.PAUSED) {
            drawCenterText(g2D, "Dang tam dung - nhan P de tiep tuc", 240, new Font("SansSerif", Font.BOLD, 20));
        }

        if (game.getState() == GameState.QUIZ) {
            String quizText = difficulty == Difficulty.HARD
                    ? "Tra loi sai se bi tru " + difficulty.getWrongAnswerPenalty() + " diem"
                    : "Tra loi sai thi cot nay khong duoc cong diem";
            drawCenterText(g2D, quizText, 240, new Font("SansSerif", Font.BOLD, 18));
        }

        if (game.getState() == GameState.GAME_OVER) {
            drawCenterText(g2D, "Ket thuc luot choi", 240, new Font("SansSerif", Font.BOLD, 22));
            drawCenterText(g2D, "Nhan R de choi lai nhanh", 268, ThemeConstants.FONT_BODY);
        }
    }

    private void drawCenterText(Graphics2D g2D, String text, int y, Font font) {
        Font oldFont = g2D.getFont();
        g2D.setFont(font);
        int textWidth = g2D.getFontMetrics().stringWidth(text);
        int x = (App.WIDTH - textWidth) / 2;
        g2D.drawString(text, x, y);
        g2D.setFont(oldFont);
    }

    private String normalizePlayerName(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            return "Nguoi choi";
        }
        return playerName.trim();
    }
}
