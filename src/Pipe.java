import java.awt.Image;

/**
 * Represents a pipe obstacle in the game.
 * Pipes come in pairs (north and south) and move horizontally across the
 * screen.
 */
public class Pipe {

    private static final int DEFAULT_WIDTH = 66;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_SPEED = 3;
    private static final int RESET_X = App.WIDTH + 2;

    public int x;
    public int y;
    public int width;
    public int height;
    public int speed;
    public boolean scored;

    private final String orientation;
    private Image image;

    public Pipe(String orientation) {
        this.orientation = orientation;
        this.speed = DEFAULT_SPEED;
        reset();
    }

    public void reset() {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        x = RESET_X;
        scored = false;

        if (isSouth()) {
            y = -(int) (Math.random() * 120) - height / 2;
        }
    }

    public void update() {
        x -= speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isSouth() {
        return "south".equals(orientation);
    }

    public boolean collides(int targetX, int targetY, int targetWidth, int targetHeight) {
        int margin = 2;

        if (targetX + targetWidth - margin > x && targetX + margin < x + width) {
            if (isSouth() && targetY < y + height) {
                return true;
            }

            if (!isSouth() && targetY + targetHeight > y) {
                return true;
            }
        }

        return false;
    }

    public Render getRender() {
        Render render = new Render();
        render.x = x;
        render.y = y;

        if (image == null) {
            image = AssetManager.getImage("lib/pipe-" + orientation + ".png");
        }
        render.image = image;
        return render;
    }
}
