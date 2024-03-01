package renderer;

import game.GameState;
import util.DisplayInfo;
import util.InputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The window used by the game. Contains a static instance of itself used for the game.
 */
public class GameJFrame extends JFrame implements ActionListener, KeyListener {

    private static GameJFrame window;

    private JPanel screenMenu;
    private JButton buttonStart;
    private JButton buttonQuit;

    private JPanel screenGame;
    private JPanel panelGameLeft;
    private GameplayJPanel panelGameplay;
    private GameRightJPanel panelGameRight;

    private JLabel labelGameLevel;
    private JLabel labelGameScore;
    private JLabel labelGameHighscore;
    private JLabel labelGameTime;

    private Font gameFont;

    private boolean gameActive;

    /**
     * Creates a new static window for the game.
     * The window's constructor sets everything up and makes itself visible, this is the only method needed to begin the game.
     */
    public static void newWindow() {
        window = new GameJFrame();
    }

    /**
     * Returns the static instance of <code>GameJFrame</code> used by the game
     * @return the static <code>GameJFrame</code> instance
     */
    public static GameJFrame getWindow() {
        return window;
    }

    /**
     * Constructs a new <code>GameJFrame</code> instance.
     * Makes the window visible and sets up its components, no additional setting up is necessary.
     */
    public GameJFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("Tetris");
        this.setIconImage(new ImageIcon("assets/textures/logo.png").getImage());

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);

        this.addKeyListener(this);

        this.setVisible(true);
        while (!DisplayInfo.setResolution(this));

        initComponents();
        revalidate();
        repaint();
    }

    /**
     * Initializes and adds all components to the window.
     * Called by the constructor.
     */
    private void initComponents() {
        screenMenu = new JPanel();
        screenMenu.setBackground(Color.BLACK);
        screenMenu.setBounds(0, 0, DisplayInfo.getW(), DisplayInfo.getH());
        screenMenu.setLayout(null);
        screenMenu.setOpaque(true);

        int buttonCount = 2;
        int buttonPortionX = 10;
        int buttonPortionY = 20;

        JPanel panelMenuButtons = new JPanel();
        panelMenuButtons.setBounds(DisplayInfo.getW() / 2 - (DisplayInfo.getW() / buttonPortionX) / 2, DisplayInfo.getH() / 2 - (DisplayInfo.getH() / buttonPortionY) * buttonCount / 2, DisplayInfo.getW() / buttonPortionX, DisplayInfo.getH() * buttonCount / buttonPortionY);
        panelMenuButtons.setBackground(Color.BLACK);
        panelMenuButtons.setLayout(new GridLayout(buttonCount, 1));
        panelMenuButtons.setOpaque(true);

        Font buttonFont = new Font("Segoe UI", 3, (DisplayInfo.getH() / buttonPortionY) / 2);

        buttonStart = new JButton("Play Tetris!");
        buttonStart.setFont(buttonFont);
        buttonStart.setBackground(Color.BLACK);
        buttonStart.setForeground(Color.WHITE);
        buttonStart.setFocusable(false);
        buttonStart.setBorder(BorderFactory.createEmptyBorder());
        buttonStart.addActionListener(this);
        panelMenuButtons.add(buttonStart);

        buttonQuit = new JButton("Quit");
        buttonQuit.setFont(buttonFont);
        buttonQuit.setBackground(Color.BLACK);
        buttonQuit.setForeground(Color.WHITE);
        buttonQuit.setBorder(BorderFactory.createEmptyBorder());
        buttonQuit.setFocusable(false);
        buttonQuit.addActionListener(this);
        panelMenuButtons.add(buttonQuit);

        screenMenu.add(panelMenuButtons);

        BufferedImage bufferedImageLogo;
        try {
            bufferedImageLogo = ImageIO.read(new File("assets/textures/logo.png"));
            if (bufferedImageLogo == null) throw new IOException("Image not initialized");
            ImageIcon iconLogo = new ImageIcon(bufferedImageLogo.getScaledInstance(bufferedImageLogo.getWidth() * DisplayInfo.getW() / 5760, bufferedImageLogo.getHeight() * DisplayInfo.getH() / 3240, Image.SCALE_SMOOTH));
            JLabel labelLogo = new JLabel(iconLogo);
            labelLogo.setBounds(DisplayInfo.getW() / 2 - iconLogo.getIconWidth() / 2, panelMenuButtons.getY() - DisplayInfo.getH() / 10 - iconLogo.getIconHeight(), iconLogo.getIconWidth(), iconLogo.getIconHeight());
            screenMenu.add(labelLogo);
        } catch (IOException e) {

        }

        gameFont = new Font("Segoe UI", 3, DisplayInfo.getH() / 18);
        initGamePanelComponents();

        this.add(screenMenu);
    }

    /**
     * Initializes the components used for the gameplay screen.
     * Called by <code>initComponents()</code>.
     */
    private void initGamePanelComponents() {
        screenGame = new JPanel();
        screenGame.setBounds(0, 0, DisplayInfo.getW(), DisplayInfo.getH());
        screenGame.setOpaque(true);
        screenGame.setLayout(new BorderLayout());

        panelGameplay = new GameplayJPanel();

        panelGameLeft = new JPanel();
        panelGameLeft.setPreferredSize(new Dimension((DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH()));
        panelGameLeft.setBackground(Color.BLACK.brighter());
        panelGameLeft.setLayout(new GridLayout(4, 1));
        panelGameLeft.setSize((DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH());

        labelGameLevel = new JLabel();
        labelGameLevel.setFont(gameFont);
        labelGameLevel.setForeground(Color.WHITE);
        labelGameLevel.setHorizontalAlignment(JLabel.CENTER);

        labelGameScore = new JLabel();
        labelGameScore.setFont(gameFont);
        labelGameScore.setForeground(Color.WHITE);
        labelGameScore.setHorizontalAlignment(JLabel.CENTER);

        labelGameHighscore = new JLabel();
        labelGameHighscore.setFont(gameFont);
        labelGameHighscore.setForeground(Color.WHITE);
        labelGameHighscore.setHorizontalAlignment(JLabel.CENTER);

        labelGameTime = new JLabel();
        labelGameTime.setFont(gameFont);
        labelGameTime.setForeground(Color.WHITE);
        labelGameTime.setHorizontalAlignment(JLabel.CENTER);

        panelGameLeft.add(labelGameHighscore);
        panelGameLeft.add(labelGameScore);
        panelGameLeft.add(labelGameLevel);
        panelGameLeft.add(labelGameTime);

        panelGameRight = new GameRightJPanel();
        panelGameRight.setPreferredSize(new Dimension((DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH()));
        panelGameRight.setBackground(Color.BLACK.brighter());
        panelGameRight.setSize((DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH());

        screenGame.add(panelGameLeft, BorderLayout.WEST);
        screenGame.add(panelGameplay, BorderLayout.CENTER);
        screenGame.add(panelGameRight, BorderLayout.EAST);
    }

    /**
     * Begins the game, shows the game screen.
     * Creates a new static <code>GameState</code> instance.
     * Called when <code>buttonStart</code> is pressed.
     */
    private void startGame() {
        gameActive = true;
        GameState.newGame();
        this.remove(screenMenu);
        this.add(screenGame);
        revalidate();
        repaint();
        Renderer.setGraphics2D();
        GameState.getGame().start();
        this.requestFocus();
    }

    /**
     * Shows the menu screen, ends the game.
     */
    public void backToMenu() {
        this.remove(screenGame);
        this.add(screenMenu);
        revalidate();
        repaint();
        gameActive = false;
    }

    /**
     * Updates the level shown during the game.
     * @param level the level to be displayed
     */
    public void updateLevel(int level) {
        this.labelGameLevel.setText("<html>Level<br/>" + level + "<html>");
    }

    /**
     * Updates the score shown during the game.
     * @param score the score to be displayed
     */
    public void updateScore(int score) {
        this.labelGameScore.setText("<html>Score<br/>" + score + "<html>");
    }

    /**
     * Updates the highscore shown during the game.
     * Should only be called at the start of the game.
     * @param highscore the highscore to be shown
     */
    public void updateHighscore(int highscore) {
        this.labelGameHighscore.setText("<html>Highscore<br/>" + highscore + "<html>");
    }

    /**
     * Updates the time shown during the game.
     * Should be called every second using a timer.
     * @param time the time to be shown
     */
    public void updateTime(int time) {
        this.labelGameTime.setText("<html>Time<br/>" + time + "<html>");
    }

    public GameplayJPanel getGamePanel() {
        return panelGameplay;
    }

    public JPanel getPanelGameRight() {
        return panelGameRight;
    }

    /**
     * Updates the upcoming pieces shown.
     * Should be called whenever the upcoming pieces list is changed.
     */
    public void updateUpcomingPieceLabels() {
        panelGameRight.updatePanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonStart) {
            startGame();
        } else if (e.getSource() == buttonQuit) {
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameActive) return;
        if (InputHandler.getKeyPressed(e.getKeyCode())) return;
        InputHandler.pressKey(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameActive) return;
        InputHandler.releaseKey(e.getKeyCode());
    }
}
