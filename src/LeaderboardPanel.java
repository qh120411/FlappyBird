import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class LeaderboardPanel extends JPanel {

    public LeaderboardPanel(ScoreDAO scoreDAO) {
        setLayout(new BorderLayout());
        setBackground(ThemeConstants.COLOR_PANEL);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Bang xep hang", SwingConstants.CENTER);
        title.setFont(ThemeConstants.FONT_PANEL_TITLE);
        title.setForeground(ThemeConstants.COLOR_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] columns = new String[] { "Hang", "Nguoi choi", "Do kho", "Diem", "Do chinh xac", "Thoi gian" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<ScoreDAO.LeaderboardEntry> rows = scoreDAO.fetchTopScores(20);
        for (ScoreDAO.LeaderboardEntry row : rows) {
            model.addRow(new Object[] {
                row.rank,
                row.playerName,
                row.difficulty.getLabel(),
                row.score,
                row.getAccuracyLabel(),
                row.playedAt
            });
        }

        if (rows.isEmpty()) {
            model.addRow(new Object[] { "-", "Chua co du lieu", "-", 0, "0%", "-" });
        }

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setBackground(Color.WHITE);
        table.setGridColor(ThemeConstants.COLOR_TABLE_GRID);
        table.getTableHeader().setBackground(ThemeConstants.COLOR_HEADER);
        table.getTableHeader().setForeground(ThemeConstants.COLOR_PRIMARY);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public static void showDialog(Component parent, ScoreDAO scoreDAO) {
        Window ownerWindow = parent == null ? null : javax.swing.SwingUtilities.getWindowAncestor(parent);
        JDialog dialog;
        if (ownerWindow instanceof Frame) {
            dialog = new JDialog((Frame) ownerWindow, "Bang xep hang", Dialog.ModalityType.APPLICATION_MODAL);
        } else if (ownerWindow instanceof Dialog) {
            dialog = new JDialog((Dialog) ownerWindow, "Bang xep hang", Dialog.ModalityType.APPLICATION_MODAL);
        } else {
            dialog = new JDialog((Frame) null, "Bang xep hang", Dialog.ModalityType.APPLICATION_MODAL);
        }
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new LeaderboardPanel(scoreDAO));
        dialog.setPreferredSize(ThemeConstants.LEADERBOARD_DIALOG_SIZE);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
