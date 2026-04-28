import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameOverDialog {

    public enum Result {
        RESTART,
        MENU,
        LEADERBOARD
    }

    private GameOverDialog() {
    }

    public static Result showDialog(Component parent, Difficulty difficulty, int score, int correctAnswers, int totalQuestions) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Ket thuc luot choi", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(ThemeConstants.COLOR_PANEL);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String summary = "<html><center><b>Diem cuoi: " + score + "</b><br/>"
            + "Do kho: " + difficulty.getLabel() + "<br/>"
            + "Cau dung: " + correctAnswers + "/" + totalQuestions + "</center></html>";
        JLabel label = new JLabel(summary, SwingConstants.CENTER);
        label.setFont(ThemeConstants.FONT_DIALOG_TITLE);
        label.setForeground(ThemeConstants.COLOR_PRIMARY);
        root.add(label, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setOpaque(false);

        JButton restartButton = new JButton("Choi lai");
        JButton leaderboardButton = new JButton("Bang xep hang");
        JButton menuButton = new JButton("Menu chinh");
        buttons.add(restartButton);
        buttons.add(leaderboardButton);
        buttons.add(menuButton);
        root.add(buttons, BorderLayout.SOUTH);

        final Result[] result = new Result[] { Result.MENU };

        restartButton.addActionListener(e -> {
            result[0] = Result.RESTART;
            dialog.dispose();
        });

        leaderboardButton.addActionListener(e -> {
            result[0] = Result.LEADERBOARD;
            dialog.dispose();
        });

        menuButton.addActionListener(e -> {
            result[0] = Result.MENU;
            dialog.dispose();
        });

        dialog.setContentPane(root);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return result[0];
    }
}
