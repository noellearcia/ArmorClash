import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Tank {
    private int x, y;
    private int width = 36, height = 36;
    private boolean playerControlled;
    private Direction dir = Direction.UP;
    private final GamePanel panel;
    private int speed = 6;
    private boolean alive = true;
    private int life = 200;

    // input flags for player
    private boolean up, down, left, right;

    // images
    private Image imgUp, imgDown, imgLeft, imgRight;
    private final Random rnd = new Random();

    private int aiStep = 0;

    public Tank(int x, int y, boolean playerControlled, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.playerControlled = playerControlled;
        this.panel = panel;
        loadImages();
    }

    private void loadImages() {
        imgUp = new ImageIcon(getClass().getResource("/images/tank_up.png")).getImage();
        imgDown = new ImageIcon(getClass().getResource("/images/tank_down.png")).getImage();
        imgLeft = new ImageIcon(getClass().getResource("/images/tank_left.png")).getImage();
        imgRight = new ImageIcon(getClass().getResource("/images/tank_right.png")).getImage();
    }

    public void update() {
        if (!alive) return;

        if (playerControlled) {
            // move according to flags
            if (up) { dir = Direction.UP; y -= speed; }
            if (down) { dir = Direction.DOWN; y += speed; }
            if (left) { dir = Direction.LEFT; x -= speed; }
            if (right) { dir = Direction.RIGHT; x += speed; }
            // bounds
            clampPosition();
            // collisions with walls: simple check to keep inside panel bounds; more complex collision is in GamePanel
        } else {
            if (aiStep <= 0) {
                aiStep = rnd.nextInt(30) + 10;
                int r = rnd.nextInt(4);
                dir = Direction.values()[r];
            }
            aiStep--;
            switch (dir) {
                case UP -> y -= speed;
                case DOWN -> y += speed;
                case LEFT -> x -= speed;
                case RIGHT -> x += speed;
            }
            clampPosition();
            if (rnd.nextInt(40) > 36) {
                Bullet b = fire();
                if (b != null) panel.addBullet(b);
            }
        }
    }

    private void clampPosition() {
        if (x < 0) x = 0;
        if (y < 40) y = 40;
        if (x + width > GamePanel.WIDTH) x = GamePanel.WIDTH - width;
        if (y + height > GamePanel.HEIGHT) y = GamePanel.HEIGHT - height;
    }

    public Bullet fire() {
        if (!alive) return null;
        int bx = x + width/2 - 5;
        int by = y + height/2 - 5;
        Bullet b = new Bullet(bx, by, dir, panel, playerControlled);
        return b;
    }

    public void draw(Graphics g) {
        if (!alive) return;
        Image im = imgUp;
        switch (dir) {
            case UP -> im = imgUp;
            case DOWN -> im = imgDown;
            case LEFT -> im = imgLeft;
            case RIGHT -> im = imgRight;
        }
        g.drawImage(im, x, y, width, height, null);

        // small HP bar
        g.setColor(Color.RED);
        g.drawRect(x, y - 8, width, 5);
        int hpW = (int) ((width) * (life / 200.0));
        g.fillRect(x, y - 8, hpW, 5);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() { return alive; }

    public void takeDamage(int d) {
        life -= d;
        if (life <= 0) {
            alive = false;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLife() { return life; }

    // input flag setters (used by key bindings)
    public void setFlagUp(boolean v) { this.up = v; if (v) dir = Direction.UP; }
    public void setFlagDown(boolean v) { this.down = v; if (v) dir = Direction.DOWN; }
    public void setFlagLeft(boolean v) { this.left = v; if (v) dir = Direction.LEFT; }
    public void setFlagRight(boolean v) { this.right = v; if (v) dir = Direction.RIGHT; }
}
