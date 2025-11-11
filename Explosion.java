import javax.swing.*;
import java.awt.*;

public class Explosion {
    private final int x, y;
    private final Image img;
    private int life = 12; // frames

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/explosion.gif")).getImage();
    }

    public void draw(Graphics g) {
        if (life > 0) {
            g.drawImage(img, x, y, 40, 40, null);
        }
    }

    public void update() { life--; }

    public boolean isAlive() { return life > 0; }
}
