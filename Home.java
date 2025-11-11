import javax.swing.*;
import java.awt.*;

public class Home {
    private final int x, y;
    private final int width = 43, height = 43;
    private boolean live = true;
    private final Image img;

    public Home(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        img = new ImageIcon(getClass().getResource("/images/home.png")).getImage();
    }

    public void draw(Graphics g) {
        if (live) g.drawImage(img, x, y, width, height, null);
        else {
            // destroyed placeholder
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
            g.setColor(Color.RED);
            g.drawString("X", x + width/2 - 4, y + height/2 + 6);
        }
    }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public boolean isLive() { return live; }
    public void setLive(boolean v) { live = v; }
}
