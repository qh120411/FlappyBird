import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 520;

    private static final String CARD_MENU = "MENU";
    private static final String CARD_GAME = "GAME";

    private final JFrame frame;
    private final JPanel root;
    private final CardLayout cardLayout;
    private final ScoreDAO scoreDAO;

    private MainMenu mainMenu;
    private GamePanel gamePanel;

    public App() {
        frame = new JFrame("Flappy Bird Hoc Tap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        scoreDAO = new ScoreDAO();
        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        mainMenu = new MainMenu(this::startGame, this::openLeaderboard, this::openQuestionAdmin);
        root.add(mainMenu, CARD_MENU);

        frame.setContentPane(root);
        cardLayout.show(root, CARD_MENU);
        frame.setVisible(true);
    }

    private void startGame(String playerName, Difficulty difficulty) {
        if (gamePanel != null) {
            gamePanel.stop();
            root.remove(gamePanel);
        }

        gamePanel = new GamePanel(playerName, difficulty, scoreDAO, this::showMenu, this::openLeaderboard);
        root.add(gamePanel, CARD_GAME);
        cardLayout.show(root, CARD_GAME);

        frame.revalidate();
        frame.repaint();
        gamePanel.requestGameFocus();
    }

    private void showMenu() {
        if (gamePanel != null) {
            gamePanel.stop();
            root.remove(gamePanel);
            gamePanel = null;
        }

        Keyboard.getInstance().clear();
        mainMenu.refresh();
        cardLayout.show(root, CARD_MENU);
        frame.revalidate();
        frame.repaint();
        frame.requestFocusInWindow();
    }

    private void openLeaderboard() {
        LeaderboardPanel.showDialog(frame, scoreDAO);
        if (gamePanel != null) {
            gamePanel.requestGameFocus();
        } else {
            frame.requestFocusInWindow();
        }
    }

    private void openQuestionAdmin() {
        QuestionAdminDialog.showDialog(frame, mainMenu::refreshQuestionInfo);
        frame.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
