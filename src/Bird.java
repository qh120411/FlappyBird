import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Bird {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean dead;

    public double yVelocity;
    public double gravity;
    public double flapVelocity;
    public double maxFallSpeed;

    private double rotation;
    private Image image;

    public Bird() {
        x = 100;
        y = 150;
        width = 45;
        height = 32;
        dead = false;

        yVelocity = 0;
        gravity = 0.45;
        flapVelocity = -8.8;
        maxFallSpeed = 8.5;
        rotation = 0.0;
    }

    public void update() {
        yVelocity += gravity;
        if (yVelocity > maxFallSpeed) {
            yVelocity = maxFallSpeed;
        }

        y += (int) yVelocity;
    }

    public void flap() {
        if (dead) {
            return;
        }

        yVelocity = flapVelocity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setFlapVelocity(double flapVelocity) {
        this.flapVelocity = flapVelocity;
    }

    public void setMaxFallSpeed(double maxFallSpeed) {
        this.maxFallSpeed = maxFallSpeed;
    }

    public Render getRender() {
        Render render = new Render();
        render.x = x;
        render.y = y;

        if (image == null) {
            image = AssetManager.getImage("lib/bird.png");
        }
        render.image = image;

        rotation = (90 * (yVelocity + 20) / 20) - 90;
        rotation = rotation * Math.PI / 180;
        if (rotation > Math.PI / 2) {
            rotation = Math.PI / 2;
        }

        render.transform = new AffineTransform();
        render.transform.translate(x + width / 2, y + height / 2);
        render.transform.rotate(rotation);
        render.transform.translate(-width / 2, -height / 2);
        return render;
    }
}
