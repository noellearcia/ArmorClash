import javax.swing.*;
import java.awt.*;

public class MetalWall {
    private final int x, y;
    private final int width = 36, height = 37;
    private final Image img;

    public MetalWall(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/metal_wall.png")).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
