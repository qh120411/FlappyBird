import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class QuizDialog {

    private QuizDialog() {
    }

    public static boolean showQuiz(Component parent, Question question) {
        if (question == null) {
            return false;
        }

        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Cau hoi", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(ThemeConstants.COLOR_PANEL);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel prompt = new JLabel(
            "<html><body style='width:340px'>" + escape(question.getPrompt()) + "</body></html>"
        );
        prompt.setFont(ThemeConstants.FONT_DIALOG_TITLE);
        prompt.setForeground(ThemeConstants.COLOR_PRIMARY);
        content.add(prompt, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        List<String> options = question.getOptions();
        JRadioButton[] buttons = new JRadioButton[options.size()];
        for (int i = 0; i < options.size(); i++) {
            buttons[i] = new JRadioButton(options.get(i));
            buttons[i].setOpaque(false);
            buttons[i].setFont(ThemeConstants.FONT_BODY);
            group.add(buttons[i]);
            optionsPanel.add(buttons[i]);
        }
        content.add(optionsPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton skipButton = new JButton("Bo qua");
        JButton submitButton = new JButton("Tra loi");
        actionPanel.add(skipButton);
        actionPanel.add(submitButton);
        content.add(actionPanel, BorderLayout.SOUTH);

        final boolean[] result = new boolean[] { false };

        submitButton.addActionListener(e -> {
            int selected = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    selected = i;
                    break;
                }
            }

            if (selected < 0) {
                JOptionPane.showMessageDialog(dialog, "Hay chon mot dap an.", "Cau hoi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            result[0] = selected == question.getCorrectIndex();
            String message = result[0] ? "Chinh xac" : "Chua dung";
            if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
                message += "\n" + question.getExplanation();
            }
            JOptionPane.showMessageDialog(dialog, message, "Cau hoi", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        skipButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        dialog.setContentPane(content);
        dialog.setPreferredSize(ThemeConstants.QUIZ_DIALOG_SIZE);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return result[0];
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
