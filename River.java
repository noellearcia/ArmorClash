import javax.swing.*;
import java.awt.*;

public class River {
    private final int x, y;
    private final int width = 154, height = 55;
    private final Image img;

    public River(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/river.png")).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}
