import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class GameInputBinder {

    private GameInputBinder() {
    }

    public static void bindGameKeys(JComponent component, Keyboard keyboard) {
        bind(component, keyboard, KeyEvent.VK_SPACE, "space");
        bind(component, keyboard, KeyEvent.VK_P, "pause");
        bind(component, keyboard, KeyEvent.VK_R, "restart");
    }

    private static void bind(JComponent component, Keyboard keyboard, int keyCode, String actionName) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        String pressedAction = actionName + ".pressed";
        String releasedAction = actionName + ".released";

        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressedAction);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), releasedAction);

        actionMap.put(pressedAction, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                keyboard.setKeyPressed(keyCode);
            }
        });

        actionMap.put(releasedAction, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                keyboard.setKeyReleased(keyCode);
            }
        });
    }
}
