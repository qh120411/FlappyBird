import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainMenu extends JPanel {

    private final BiConsumer<String, Difficulty> startAction;
    private final Runnable leaderboardAction;
    private final Runnable adminAction;

    private JTextField nameField;
    private JComboBox<Difficulty> difficultyBox;
    private JLabel questionInfoLabel;

    public MainMenu(BiConsumer<String, Difficulty> startAction, Runnable leaderboardAction, Runnable adminAction) {
        this.startAction = startAction;
        this.leaderboardAction = leaderboardAction;
        this.adminAction = adminAction;

        setLayout(new BorderLayout());
        setOpaque(true);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(true);
        content.setBackground(ThemeConstants.COLOR_BACKGROUND);
        content.setBorder(ThemeConstants.createPanelBorder());

        JLabel title = new JLabel("Flappy Bird Hoc Tap");
        title.setFont(ThemeConstants.FONT_TITLE);
        title.setForeground(ThemeConstants.COLOR_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Vuot cot, tra loi cau hoi, leo bang xep hang");
        subtitle.setFont(ThemeConstants.FONT_SUBTITLE);
        subtitle.setForeground(ThemeConstants.COLOR_SECONDARY);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel playerLabel = new JLabel("Ten nguoi choi");
        playerLabel.setFont(ThemeConstants.FONT_LABEL);
        playerLabel.setForeground(ThemeConstants.COLOR_PRIMARY);
        playerLabel.setAlignmentX(CENTER_ALIGNMENT);

        nameField = new JTextField("Nguoi choi");
        nameField.setMaximumSize(ThemeConstants.INPUT_SIZE);
        nameField.setBorder(ThemeConstants.createInputBorder());

        JLabel difficultyLabel = new JLabel("Do kho");
        difficultyLabel.setFont(ThemeConstants.FONT_LABEL);
        difficultyLabel.setForeground(ThemeConstants.COLOR_PRIMARY);
        difficultyLabel.setAlignmentX(CENTER_ALIGNMENT);

        difficultyBox = new JComboBox<Difficulty>(Difficulty.values());
        difficultyBox.setMaximumSize(ThemeConstants.INPUT_SIZE);
        difficultyBox.setBackground(Color.WHITE);

        JButton startButton = createMenuButton("Bat dau", ThemeConstants.COLOR_START);
        startButton.addActionListener(
            e -> startAction.accept(nameField.getText(), (Difficulty) difficultyBox.getSelectedItem())
        );

        JButton leaderboardButton = createMenuButton("Bang xep hang", ThemeConstants.COLOR_LEADERBOARD);
        leaderboardButton.addActionListener(e -> leaderboardAction.run());

        JButton adminButton = createMenuButton("Quan ly cau hoi", ThemeConstants.COLOR_ADMIN);
        adminButton.addActionListener(e -> adminAction.run());

        JButton exitButton = createMenuButton("Thoat", ThemeConstants.COLOR_EXIT);
        exitButton.addActionListener(e -> System.exit(0));

        questionInfoLabel = new JLabel();
        questionInfoLabel.setFont(ThemeConstants.FONT_SUBTITLE);
        questionInfoLabel.setForeground(ThemeConstants.COLOR_SECONDARY);
        questionInfoLabel.setAlignmentX(CENTER_ALIGNMENT);
        refreshQuestionInfo();

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(20));
        content.add(playerLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(nameField);
        content.add(Box.createVerticalStrut(14));
        content.add(difficultyLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(difficultyBox);
        content.add(Box.createVerticalStrut(20));
        content.add(startButton);
        content.add(Box.createVerticalStrut(10));
        content.add(leaderboardButton);
        content.add(Box.createVerticalStrut(10));
        content.add(adminButton);
        content.add(Box.createVerticalStrut(14));
        content.add(questionInfoLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(exitButton);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 44));
        centerWrapper.setOpaque(false);
        centerWrapper.add(content);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JLabel footerText = new JLabel("SPACE: nhay | P: tam dung | R: choi lai nhanh");
        footerText.setForeground(ThemeConstants.COLOR_LIGHT_TEXT);
        footerText.setFont(ThemeConstants.FONT_LABEL);
        footer.add(footerText);

        add(centerWrapper, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void refresh() {
        refreshQuestionInfo();
        nameField.requestFocusInWindow();
    }

    public void refreshQuestionInfo() {
        questionInfoLabel.setText("So cau hoi da nap: " + QuizManager.getInstance().getAllQuestions().size());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
            0,
            0,
            new Color(61, 142, 214),
            0,
            getHeight(),
            new Color(245, 185, 95)
        );
        g2D.setPaint(gradient);
        g2D.fillRect(0, 0, getWidth(), getHeight());

        g2D.setColor(new Color(255, 255, 255, 70));
        g2D.fillOval(30, 40, 120, 44);
        g2D.fillOval(280, 70, 150, 50);
        g2D.fillOval(330, 140, 90, 36);
        g2D.dispose();
    }

    private JButton createMenuButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setMaximumSize(ThemeConstants.BUTTON_SIZE);
        button.setPreferredSize(new Dimension(ThemeConstants.BUTTON_SIZE.width, ThemeConstants.BUTTON_SIZE.height));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(ThemeConstants.FONT_BUTTON);
        return button;
    }

    // public static void main(String[] args) {
    //     MainMenu menu = new MainMenu(
    //         (name, difficulty) -> System.out.println("Start game for " + name + " with difficulty " + difficulty),
    //         () -> System.out.println("Show leaderboard"),
    //         () -> System.out.println("Open admin panel")
    //     );
    //     menu.setPreferredSize(new Dimension(480, 360));
    //     javax.swing.JFrame frame = new javax.swing.JFrame("Main Menu Test");
    //     frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    //     frame.getContentPane().add(menu);
    //     frame.pack();
    //     frame.setLocationRelativeTo(null);
    //     frame.setVisible(true);
    // }
}

