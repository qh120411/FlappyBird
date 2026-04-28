import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

/**
 * Represents a renderable object with position, image, and optional transform.
 * Used by GamePanel to draw game elements on screen.
 */
public class Render {
    public int x;
    public int y;
    public Image image;
    public AffineTransform transform;

    public Render() {
    }

    /**
     * Creates a render object with initial position and image.
     */
    public Render(int x, int y, String imagePath) {
        Toolkit.getDefaultToolkit().sync();
        this.x = x;
        this.y = y;
        this.image = AssetManager.getImage(imagePath);
    }
}
