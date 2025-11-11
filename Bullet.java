import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private final int speed = 12;
    private final Direction dir;
    private boolean alive = true;
    private final Image img;
    private final GamePanel panel;
    private final boolean fromPlayer;

    public Bullet(int x, int y, Direction dir, GamePanel panel, boolean fromPlayer) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.panel = panel;
        this.fromPlayer = fromPlayer;
        img = new ImageIcon(getClass().getResource("/images/bullet.png")).getImage();
    }

    public void update() {
        switch (dir) {
            case UP -> y -= speed;
            case DOWN -> y += speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
            default -> {}
        }
        if (x < -10 || y < -10 || x > GamePanel.WIDTH + 10 || y > GamePanel.HEIGHT + 10) alive = false;
    }

    public void draw(Graphics g) {
        if (!alive) return;
        g.drawImage(img, x, y, 10, 10, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 10);
    }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean v) { this.alive = v; }
    public boolean isFromPlayer() { return fromPlayer; }
}
