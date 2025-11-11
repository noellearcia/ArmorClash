import javax.swing.*;

public class MenuFactory {
    public static JMenuBar createMenuBar(GamePanel game) {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem pause = new JMenuItem("Pause/Resume");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(e -> game.resetGame());
        pause.addActionListener(e -> game.togglePause());
        exit.addActionListener(e -> System.exit(0));

        gameMenu.add(newGame);
        gameMenu.add(pause);
        gameMenu.add(exit);

        menuBar.add(gameMenu);
        return menuBar;
    }
}
