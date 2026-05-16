import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private static final Map<String, Image> CACHE = new HashMap<String, Image>();

    private AssetManager() {
    }

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
