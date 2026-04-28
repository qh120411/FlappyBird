import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ThemeConstants {

    public static final Color COLOR_PRIMARY = new Color(72, 44, 13);
    public static final Color COLOR_SECONDARY = new Color(114, 84, 37);
    public static final Color COLOR_BACKGROUND = new Color(255, 248, 231);
    public static final Color COLOR_PANEL = new Color(255, 250, 239);
    public static final Color COLOR_BORDER = new Color(218, 176, 92);
    public static final Color COLOR_INPUT_BORDER = new Color(217, 181, 98);
    public static final Color COLOR_TABLE_GRID = new Color(229, 214, 186);
    public static final Color COLOR_HEADER = new Color(238, 200, 132);
    public static final Color COLOR_LIGHT_TEXT = new Color(255, 247, 232);

    public static final Color COLOR_START = new Color(238, 145, 35);
    public static final Color COLOR_LEADERBOARD = new Color(39, 104, 178);
    public static final Color COLOR_ADMIN = new Color(78, 128, 70);
    public static final Color COLOR_EXIT = new Color(103, 63, 34);

    public static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 30);
    public static final Font FONT_PANEL_TITLE = new Font("Serif", Font.BOLD, 24);
    public static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_DIALOG_TITLE = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_HUD_TITLE = new Font("SansSerif", Font.BOLD, 22);

    public static final Dimension BUTTON_SIZE = new Dimension(340, 40);
    public static final Dimension INPUT_SIZE = new Dimension(340, 36);
    public static final Dimension QUIZ_DIALOG_SIZE = new Dimension(420, 280);
    public static final Dimension ADMIN_DIALOG_SIZE = new Dimension(920, 420);
    public static final Dimension LEADERBOARD_DIALOG_SIZE = new Dimension(500, 360);

    private ThemeConstants() {
    }

    public static Border createPanelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 2),
            BorderFactory.createEmptyBorder(30, 34, 30, 34)
        );
    }

    public static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
    }
}
