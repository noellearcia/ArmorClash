import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Timer timer;
    private boolean paused = false;

    private Tank player1;
    private Tank player2; // optional for future
    private final List<Tank> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();
    private final List<MetalWall> metalWalls = new ArrayList<>();
    private final List<River> rivers = new ArrayList<>();
    private final List<Tree> trees = new ArrayList<>();
    private Home home;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GREEN.darker());
        setDoubleBuffered(true);
        setFocusable(true);

        initGameObjects();
        initKeyBindings();

        timer = new Timer(40, e -> {
            if (!paused) {
                updateGame();
            }
            repaint();
        });
        timer.start();
    }

    private void initGameObjects() {
        // Player spawn
        player1 = new Tank(300, 520, true, this);

        // Home/base
        home = new Home(360, 540, this);

        // Enemies
        enemies.clear();
        for (int i = 0; i < 10; i++) {
            // spread top region
            enemies.add(new Tank(80 + i * 60, 40, false, this));
        }

        // Walls
        walls.clear();
        for (int i = 0; i < 16; i++) {
            walls.add(new Wall(200 + 21 * i, 300, this));
            walls.add(new Wall(500 + 21 * i, 180, this));
        }
        // home wall
        for (int i = 0; i < 10; i++) {
            if (i < 4) walls.add(new Wall(350, 580 - 21 * i, this));
            else if (i < 7) walls.add(new Wall(372 + 22 * (i - 4), 517, this));
            else walls.add(new Wall(416, 538 + (i - 7) * 21, this));
        }

        // metal walls
        metalWalls.clear();
        for (int i = 0; i < 10; i++) {
            metalWalls.add(new MetalWall(140 + 30 * (i % 10), (i < 5) ? 150 : 180, this));
        }

        // river and trees
        rivers.clear();
        rivers.add(new River(85, 100, this));
        trees.clear();
        for (int i = 0; i < 4; i++) {
            trees.add(new Tree(0 + 30 * i, 360, this));
            trees.add(new Tree(220 + 30 * i, 360, this));
        }

        bullets.clear();
        explosions.clear();
    }

    private void initKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Player1 keys - pressed
        im.put(KeyStroke.getKeyStroke("pressed W"), "p1_up_pressed");
        im.put(KeyStroke.getKeyStroke("pressed S"), "p1_down_pressed");
        im.put(KeyStroke.getKeyStroke("pressed A"), "p1_left_pressed");
        im.put(KeyStroke.getKeyStroke("pressed D"), "p1_right_pressed");
        im.put(KeyStroke.getKeyStroke("pressed F"), "p1_fire");

        // Player1 keys - released
        im.put(KeyStroke.getKeyStroke("released W"), "p1_up_released");
        im.put(KeyStroke.getKeyStroke("released S"), "p1_down_released");
        im.put(KeyStroke.getKeyStroke("released A"), "p1_left_released");
        im.put(KeyStroke.getKeyStroke("released D"), "p1_right_released");

        am.put("p1_up_pressed", new KeyAction(() -> player1.setFlagUp(true)));
        am.put("p1_down_pressed", new KeyAction(() -> player1.setFlagDown(true)));
        am.put("p1_left_pressed", new KeyAction(() -> player1.setFlagLeft(true)));
        am.put("p1_right_pressed", new KeyAction(() -> player1.setFlagRight(true)));
        am.put("p1_fire", new KeyAction(() -> {
            Bullet b = player1.fire();
            if (b != null) bullets.add(b);
        }));

        am.put("p1_up_released", new KeyAction(() -> player1.setFlagUp(false)));
        am.put("p1_down_released", new KeyAction(() -> player1.setFlagDown(false)));
        am.put("p1_left_released", new KeyAction(() -> player1.setFlagLeft(false)));
        am.put("p1_right_released", new KeyAction(() -> player1.setFlagRight(false)));
    }

    private static class KeyAction extends AbstractAction {
        private final Runnable r;
        public KeyAction(Runnable r) { this.r = r; }
        @Override public void actionPerformed(java.awt.event.ActionEvent e) { r.run(); }
    }

    private void updateGame() {
        // update tanks
        player1.update();
        for (Iterator<Tank> it = enemies.iterator(); it.hasNext(); ) {
            Tank t = it.next();
            t.update();
            if (!t.isAlive()) {
                explosions.add(new Explosion(t.getX(), t.getY()));
                it.remove();
            }
        }

        // update bullets
        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext(); ) {
            Bullet b = it.next();
            b.update();
            // Check collisions: walls
            boolean removed = false;
            for (Iterator<Wall> wit = walls.iterator(); wit.hasNext(); ) {
                Wall w = wit.next();
                if (b.getBounds().intersects(w.getBounds())) {
                    // destroy common wall
                    wit.remove();
                    removed = true;
                    break;
                }
            }
            if (removed) { it.remove(); continue; }

            for (MetalWall mw : metalWalls) {
                if (b.getBounds().intersects(mw.getBounds())) {
                    removed = true;
                    break;
                }
            }
            if (removed) { it.remove(); continue; }

            // hit enemy tanks
            for (Tank t : enemies) {
                if (b.isAlive() && t.isAlive() && b.getBounds().intersects(t.getBounds()) && b.isFromPlayer()) {
                    t.takeDamage(100);
                    b.setAlive(false);
                    break;
                }
            }
            // hit player?
            if (b.isAlive() && player1.isAlive() && b.getBounds().intersects(player1.getBounds()) && !b.isFromPlayer()) {
                player1.takeDamage(50);
                b.setAlive(false);
            }

            // hit home
            if (b.isAlive() && home.isLive() && b.getBounds().intersects(home.getBounds())) {
                home.setLive(false);
                b.setAlive(false);
            }

            if (!b.isAlive()) it.remove();
        }

        // remove finished explosions (after a small counter)
        for (Iterator<Explosion> it = explosions.iterator(); it.hasNext(); ) {
            Explosion ex = it.next();
            ex.update();
            if (!ex.isAlive()) it.remove();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // background
        g.setColor(getBackground());
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw rivers (so they appear under tanks)
        for (River r : rivers) r.draw(g);

        // draw walls & metal walls
        for (Wall w : walls) w.draw(g);
        for (MetalWall mw : metalWalls) mw.draw(g);

        // draw home & trees
        home.draw(g);
        for (Tree t : trees) t.draw(g);

        // draw tanks
        player1.draw(g);
        for (Tank t : enemies) t.draw(g);

        // bullets
        for (Bullet b : bullets) b.draw(g);

        // explosions
        for (Explosion ex : explosions) ex.draw(g);

        // UI overlay
        g.setColor(Color.WHITE);
        g.drawString("Enemies: " + enemies.size(), 10, 18);
        g.drawString("Player HP: " + player1.getLife(), 600, 18);
        if (!home.isLive()) {
            g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
            g.setColor(Color.RED);
            g.drawString("HOME DESTROYED - GAME OVER", 80, 300);
        } else if (enemies.isEmpty()) {
            g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
            g.setColor(Color.GREEN);
            g.drawString("CONGRATULATIONS - YOU WIN!", 100, 300);
        }
    }

    public void togglePause() { paused = !paused; }

    public void resetGame() {
        paused = false;
        initGameObjects();
    }

    // helpers for other classes
    public List<Wall> getWalls() { return walls; }
    public List<MetalWall> getMetalWalls() { return metalWalls; }
    public Home getHome() { return home; }
    public void addBullet(Bullet b) { bullets.add(b); }
}
