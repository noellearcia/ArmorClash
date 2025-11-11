import javax.swing.*;
import java.awt.*;

public class Wall {
    private final int x, y;
    private final int width = 21, height = 21;
    private final Image img;

    public Wall(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/wall.png")).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
