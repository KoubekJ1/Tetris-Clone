package game;

import game.pieces.Piece;
import game.pieces.PieceOrder;
import renderer.GameJFrame;
import renderer.Renderer;
import util.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * A <code>GameState</code> instance serves as a way to manage and run the game.
 * A static instance of <code>GameState</code> is used for the game.
 */
public class GameState implements ActionListener {

    public static final int TILE_COUNT_X = 10;
    public static final int TILE_COUNT_Y = 20;
    public static final int PIECE_COUNT = 7;

    private static GameState game;

    private Timer gameLengthTimer;
    private Timer gravityTimer;

    private PieceOrder pieceOrder;
    private Piece fallingPiece;

    private Tile[][] tiles;
    private Level level;
    private int score;
    private int time;
    private int highscore;

    /**
     * Creates a new, static <code>GameState</code> instance.
     */
    public static void newGame() {
        game = new GameState();
    }

    /**
     * Starts the game.
     * The game timers start when this method is called, so it is important to show the game screen as soon as this method is called.
     */
    public void start() {
        this.gameLengthTimer = new Timer(1000, this);
        this.gravityTimer = new Timer(level.getGravityDelay(), this);
        this.gravityTimer.setInitialDelay(0);

        pieceOrder = new PieceOrder();
        nextPiece();

        GameJFrame.getWindow().updateUpcomingPieceLabels();

        GameJFrame.getWindow().updateLevel(level.getLevel());
        GameJFrame.getWindow().updateScore(score);
        GameJFrame.getWindow().updateHighscore(highscore);
        GameJFrame.getWindow().updateTime(time);

        gameLengthTimer.start();
        gravityTimer.start();
    }

    /**
     * Constructs a new <code>GameState</code> instance and assigns its initial values.
     */
    public GameState() {
        this.level = new Level();
        this.score = 0;
        this.time = 0;

        try {
            File highscoreFile = new File("save/highscore.txt");
            highscoreFile.getParentFile().mkdirs();
            BufferedReader reader = new BufferedReader(new FileReader(highscoreFile));
            this.highscore = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException e) {
            this.highscore = 0;
        } catch (NumberFormatException e) {
            this.highscore = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.tiles = new Tile[TILE_COUNT_X][TILE_COUNT_Y];
        for (int i = 0; i < TILE_COUNT_X; i++) {
            for (int j = 0; j < TILE_COUNT_Y; j++) {
                this.tiles[i][j] = new Tile();
            }
        }

    }

    /**
     * Places down the currently falling piece.
     * If the rows that the piece lands on are filled, the rows get cleared.
     */
    private void placePiece() {
        for (Point tile : fallingPiece.getTiles()) {
            this.tiles[(int) tile.getX()][(int) tile.getY()].setOccupied(true);
            this.tiles[(int) tile.getX()][(int) tile.getY()].setColor(fallingPiece.getColor());
            if (tile.y <= 1) {
                gameOver();
                return;
            }
        }

        TreeSet<Integer> rowsFilled = getFilledRows();
        if (rowsFilled.size() > 0) {
            Renderer.playRowClearAnimation(rowsFilled);
            level.rowCleared(rowsFilled.size());
        } else {
            nextPiece();
        }
    }

    /**
     * Ends the game, stops the timers and returns the user to the menu screen after showing them their results.
     */
    private void gameOver() {
        gravityTimer.stop();
        gameLengthTimer.stop();
        Renderer.render();
        String message = "Score: " + score + "\nHighscore: " + highscore;
        if (score > highscore) {
            message = message + "\nNew highscore!";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("save/highscore.txt"));
                writer.write(String.valueOf(score));
                writer.close();
            } catch (IOException e) {
                message = message + "\nError: Unable to save new highscore";
            }
        }
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.PLAIN_MESSAGE);
        GameJFrame.getWindow().backToMenu();
    }

    /**
     * Clears the passed in rows, moves the tiles above the passed in rows down and continues the game after being paused due to the row clear animation.
     * @param rows the cleared rows
     */
    public void clearFilledRows(TreeSet<Integer> rows) {
        int scoreReceived = 0;
        switch (rows.size()) {
            case 1 -> scoreReceived = 100;
            case 2 -> scoreReceived = 300;
            case 3 -> scoreReceived = 500;
            case 4 -> scoreReceived = 800;
        }
        score += scoreReceived * level.getLevel();
        GameJFrame.getWindow().updateScore(score);

        for (int row : rows) {
            for (int i = row; i >= rows.size(); i--) {
                for (int j = 0; j < TILE_COUNT_X; j++) {
                    tiles[j][i].setOccupied(tiles[j][i - 1].isOccupied());
                    tiles[j][i].setColor(tiles[j][i - 1].getColor());
                }
            }
        }
        nextPiece();
        startGravityTimer();
    }

    /**
     * Swaps the currently falling piece with the next piece.
     * Updates the upcoming piece labels.
     */
    private void nextPiece() {
        fallingPiece = pieceOrder.getNextPiece();
        pieceOrder.removePiece();
        GameJFrame.getWindow().updateUpcomingPieceLabels();
    }

    /**
     * Returns the rows filled by the currently falling piece.
     * Must be called after the piece is placed down but before the piece is swapped with the next one.
     * @return the filled rows
     */
    private TreeSet<Integer> getFilledRows() {
        HashSet<Integer> rowsChecked = new HashSet<>();
        TreeSet<Integer> rowsFilled = new TreeSet<>();
        for (Point piece : fallingPiece.getTiles()) {
            if (rowsChecked.contains(piece.getY())) continue;
            if (isRowFilled(piece.y)) rowsFilled.add(piece.y);
            rowsChecked.add((int) piece.getY());
        }
        return rowsFilled;
    }

    /**
     * Returns whether the row is filled.
     * @param row the row
     * @return whether the row is filled
     */
    private boolean isRowFilled(int row) {
        for (int i = 0; i < TILE_COUNT_X; i++) {
            if (!tiles[i][row].isOccupied()) return false;
        }
        return true;
    }

    public static GameState getGame() {
        return game;
    }

    public Piece getFallingPiece() {
        return fallingPiece;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setGravityTimerDelay(int delay) {
        this.gravityTimer.setDelay(delay);
    }

    public PieceOrder getPieceOrder() {
        return pieceOrder;
    }

    /**
     * Speeds the gravity timer up to 20 times its normal speed.
     * Called when the user presses the soft drop button.
     */
    public void softDrop() {
        gravityTimer.restart();
        gravityTimer.setDelay(level.getGravityDelay() / 20);
    }

    /**
     * Resets the gravity timer to its normal speed.
     */
    public void resetGravityTimerDelay() {
        gravityTimer.restart();
        gravityTimer.setDelay(level.getGravityDelay());
    }

    /**
     * Makes the piece fall down immediately.
     * For each cell the piece goes through, the user gets 2 additional points.
     */
    public void hardDrop() {
        while (getFallingPiece().gravity()) {
            if (!fallingPiece.isPlaceDelay()) score += 2;
        }
        GameJFrame.getWindow().updateScore(score);
        gravityTimer.restart();
    }

    /**
     * Stops the gravity timer.
     */
    public void stopGravityTimer() {
        gravityTimer.stop();
    }

    /**
     * Starts the gravity timer.
     */
    public void startGravityTimer() {
        gravityTimer.start();
    }

    /**
     * Puts the currently falling piece into the "hold" position, replaces it with the held piece and updates the upcoming piece labels.
     */
    public void hold() {
        fallingPiece = pieceOrder.hold(fallingPiece);
        GameJFrame.getWindow().updateUpcomingPieceLabels();
        gravityTimer.restart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gameLengthTimer) {
            time++;
            GameJFrame.getWindow().updateTime(time);
        } else if (e.getSource() == gravityTimer) {
            boolean gravity = getFallingPiece().gravity();
            if (!gravity) placePiece();
            Renderer.render();
            if (!gravity) return;
            if (InputHandler.getKeyPressed(KeyEvent.VK_DOWN) || InputHandler.getKeyPressed(KeyEvent.VK_S) && !fallingPiece.isPlaceDelay()) {
                score++;
                GameJFrame.getWindow().updateScore(score);
            }
        }
    }
}