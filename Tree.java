import javax.swing.*;
import java.awt.*;

public class Tree {
    private final int x, y;
    private final int width = 30, height = 30;
    private final Image img;

    public Tree(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/tree.png")).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }
}
