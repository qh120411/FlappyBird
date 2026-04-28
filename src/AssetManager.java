import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * Asset manager for caching loaded images.
 * Prevents redundant file I/O by caching images in memory.
 */
public class AssetManager {

    private static final Map<String, Image> CACHE = new HashMap<String, Image>();

    private AssetManager() {
    }

    /**
     * Gets a cached image or loads it if not already cached.
     */
    public static Image getImage(String path) {
        Image image = CACHE.get(path);
        if (image != null) {
            return image;
        }

        image = Util.loadImage(path);
        if (image != null) {
            CACHE.put(path, image);
        }

        return image;
    }
}
