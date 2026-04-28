import java.util.Arrays;

/**
 * Singleton keyboard input handler.
 * Provides event-based key input with consumption mechanism to prevent key
 * repeat.
 */
public class Keyboard {

    private static Keyboard instance;

    private final boolean[] keys;
    private final boolean[] consumed;

    private Keyboard() {
        keys = new boolean[256];
        consumed = new boolean[256];
    }

    public static Keyboard getInstance() {
        if (instance == null) {
            instance = new Keyboard();
        }
        return instance;
    }

    public void setKeyPressed(int keyCode) {
        if (isValidKey(keyCode)) {
            keys[keyCode] = true;
        }
    }

    public void setKeyReleased(int keyCode) {
        if (isValidKey(keyCode)) {
            keys[keyCode] = false;
            consumed[keyCode] = false;
        }
    }

    public boolean consumePress(int keyCode) {
        if (isValidKey(keyCode) && keys[keyCode] && !consumed[keyCode]) {
            consumed[keyCode] = true;
            return true;
        }
        return false;
    }

    public void clear() {
        Arrays.fill(keys, false);
        Arrays.fill(consumed, false);
    }

    private boolean isValidKey(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length;
    }
}
