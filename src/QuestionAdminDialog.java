import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class QuestionAdminDialog extends JDialog {

    private static final String DEFAULT_PATH = "data/questions.csv";

    private final QuestionFileManager questionFileManager;
    private final Runnable afterSaveAction;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public QuestionAdminDialog(Window owner, Runnable afterSaveAction) {
        super(owner, "Quan ly cau hoi", Dialog.ModalityType.APPLICATION_MODAL);
        this.questionFileManager = new QuestionFileManager();
        this.afterSaveAction = afterSaveAction;

        tableModel = new DefaultTableModel(new String[] {
            "Do kho", "Cau hoi", "Lua chon A", "Lua chon B", "Lua chon C", "Lua chon D", "Dap an", "Giai thich"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(ThemeConstants.COLOR_HEADER);
        table.getTableHeader().setForeground(ThemeConstants.COLOR_PRIMARY);

        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(ThemeConstants.COLOR_PANEL);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JLabel note = new JLabel(
            "Dinh dang CSV: difficulty,question,option_a,option_b,option_c,option_d,correct,explanation"
        );
        note.setFont(ThemeConstants.FONT_SUBTITLE);
        note.setForeground(ThemeConstants.COLOR_PRIMARY);
        top.add(note);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        loadIntoTable(questionFileManager.loadQuestions(DEFAULT_PATH));
        setPreferredSize(ThemeConstants.ADMIN_DIALOG_SIZE);
        pack();
    }

    private JPanel buildActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        JButton importButton = new JButton("Nhap file");
        JButton exportButton = new JButton("Xuat file");
        JButton addButton = new JButton("Them dong");
        JButton deleteButton = new JButton("Xoa dong");
        JButton saveButton = new JButton("Luu mac dinh");

        importButton.addActionListener(e -> importFromFile());
        exportButton.addActionListener(e -> exportToFile());
        addButton.addActionListener(e -> tableModel.addRow(new Object[] { Difficulty.EASY.name(), "", "", "", "", "", "1", "" }));
        deleteButton.addActionListener(e -> deleteSelectedRow());
        saveButton.addActionListener(e -> saveDefaultFile());

        actions.add(importButton);
        actions.add(exportButton);
        actions.add(addButton);
        actions.add(deleteButton);
        actions.add(saveButton);
        return actions;
    }

    private void importFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            loadIntoTable(questionFileManager.loadQuestions(file.getAbsolutePath()));
        }
    }

    private void exportToFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            questionFileManager.saveQuestions(file.getAbsolutePath(), readQuestionsFromTable());
        }
    }

    private void deleteSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            tableModel.removeRow(row);
        }
    }

    private void saveDefaultFile() {
        List<Question> questions = readQuestionsFromTable();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sach cau hoi dang rong.", "Quan ly cau hoi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        questionFileManager.saveQuestions(DEFAULT_PATH, questions);
        QuizManager.getInstance().reload();
        if (afterSaveAction != null) {
            afterSaveAction.run();
        }

        JOptionPane.showMessageDialog(this, "Da luu vao " + DEFAULT_PATH, "Quan ly cau hoi", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadIntoTable(List<Question> questions) {
        tableModel.setRowCount(0);
        for (Question question : questions) {
            tableModel.addRow(new Object[] {
                question.getDifficulty().name(),
                question.getPrompt(),
                question.getOptions().get(0),
                question.getOptions().get(1),
                question.getOptions().get(2),
                question.getOptions().get(3),
                Integer.toString(question.getCorrectIndex() + 1),
                question.getExplanation()
            });
        }
    }

    private List<Question> readQuestionsFromTable() {
        List<Question> questions = new ArrayList<Question>();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Difficulty difficulty = Difficulty.fromString(valueAt(row, 0));
            String prompt = valueAt(row, 1);
            String[] options = new String[] {
                valueAt(row, 2),
                valueAt(row, 3),
                valueAt(row, 4),
                valueAt(row, 5)
            };
            int correctIndex = parseCorrectIndex(valueAt(row, 6));
            String explanation = valueAt(row, 7);

            if (!prompt.isEmpty() && correctIndex >= 0 && correctIndex < 4) {
                questions.add(new Question(difficulty, prompt, options, correctIndex, explanation));
            }
        }
        return questions;
    }

    private String valueAt(int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString().trim();
    }

    private int parseCorrectIndex(String value) {
        try {
            int numeric = Integer.parseInt(value);
            return numeric >= 1 && numeric <= 4 ? numeric - 1 : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void showDialog(Component parent, Runnable afterSaveAction) {
        Window ownerWindow = parent == null ? null : javax.swing.SwingUtilities.getWindowAncestor(parent);
        QuestionAdminDialog dialog;
        if (ownerWindow instanceof Frame) {
            dialog = new QuestionAdminDialog((Frame) ownerWindow, afterSaveAction);
        } else if (ownerWindow instanceof Dialog) {
            dialog = new QuestionAdminDialog((Dialog) ownerWindow, afterSaveAction);
        } else {
            dialog = new QuestionAdminDialog((Window) null, afterSaveAction);
        }
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    public QuestionAdminDialog(Frame owner, Runnable afterSaveAction) {
        this((Window) owner, afterSaveAction);
    }

    public QuestionAdminDialog(Dialog owner, Runnable afterSaveAction) {
        this((Window) owner, afterSaveAction);
    }
}
