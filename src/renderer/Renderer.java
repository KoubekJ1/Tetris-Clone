package renderer;

import game.GameState;
import game.Tile;
import game.pieces.Piece;
import util.DisplayInfo;

import java.awt.*;
import java.util.TreeSet;

/**
 * Used for drawing the game on the static <code>GameJFrame</code>'s <code>GameplayJPanel</code>.
 */
public abstract class Renderer {

    private static Graphics2D g2D;

    /**
     * Assigns the static <code>GameJFrame</code>'s <code>GameplayJPanel</code>'s <code>Graphics2D</code> as the <code>Graphics2D</code> instance to be used.
     */
    public static void setGraphics2D() {
        Renderer.g2D = (Graphics2D) GameJFrame.getWindow().getGamePanel().getGraphics();
        if (g2D == null) {
            throw new IllegalStateException("Graphics2D not initialized!");
        }
    }

    /**
     * Renders the game with the assigned <code>Graphics2D</code> instance.
     */
    public static void render() {
        // Render the background
        g2D.setPaint(Color.BLACK);
        g2D.fillRect(0, 0, DisplayInfo.getH() / 2, DisplayInfo.getH());

        // Render the falling piece
        renderFallingPiece();

        // Render the placed pieces
        for (int row = 0; row < GameState.TILE_COUNT_X; row++) {
            for (int column = 0; column < GameState.TILE_COUNT_Y; column++) {
                Tile tile = GameState.getGame().getTiles()[row][column];
                Color tileColor;
                if (!tile.isMarked()) {
                    tileColor = tile.getColor();
                } else {
                    tileColor = Color.WHITE;
                }
                if (!tile.isOccupied()) continue;
                renderTile(row, column, tileColor);
            }
        }

        // Draw the grid

        g2D.setPaint(Color.DARK_GRAY.darker().darker());
        float tileSize = DisplayInfo.getH() / 20.0f;
        float lineThickness = tileSize / 20.0f;

        // Draw the columns
        for (int i = 0; i < GameState.TILE_COUNT_X + 1; i++) {
            g2D.fillRect(Math.round(tileSize * i - lineThickness / 2.0f), 0, Math.round(lineThickness), DisplayInfo.getH());
        }

        // Draw the rows
        for (int i = 0; i < GameState.TILE_COUNT_Y + 1; i++) {
            g2D.fillRect(0, Math.round(tileSize * i - lineThickness / 2.0f), DisplayInfo.getH() / 2, Math.round(lineThickness));
        }

        // Draw the outline of the piece on the ground
        drawPiecePredictionOutline();
    }

    /**
     * Plays the row clear animation on the given rows.
     * The game stops, the animation plays and then the rows get removed and the game continues.
     *
     * @param rows the filled rows
     */
    public static void playRowClearAnimation(TreeSet<Integer> rows) {
        new RowClearAnimation(rows);
    }

    /**
     * Draws the currently falling piece.
     */
    private static void renderFallingPiece() {
        Piece fallingPiece = GameState.getGame().getFallingPiece();

        g2D.setPaint(fallingPiece.getColor());

        for (Point point : fallingPiece.getTiles()) {
            renderTile(point.x, point.y, fallingPiece.getColor());
        }
    }

    /**
     * Renders the given tile.
     *
     * @param row    the tile's row
     * @param column the tile's column
     * @param color  the tile's color
     */
    private static void renderTile(int row, int column, Color color) {
        g2D.setPaint(color);

        float size = DisplayInfo.getH() / 20.0f;
        int x = Math.round(size * row);
        int y = Math.round(size * column);

        g2D.fillRect(x, y, Math.round(size), Math.round(size));
    }

    /**
     * Draws an outline of where the falling piece would land if it was hard dropped.
     */
    private static void drawPiecePredictionOutline() {
        Piece piece = GameState.getGame().getFallingPiece();

        float size = DisplayInfo.getH() / 20.0f;

        g2D.setPaint(piece.getColor());

        for (Point tile : GameState.getGame().getFallingPiece().getTiles()) {
            int x = Math.round(size * tile.x);
            int y = Math.round(size * tile.y + size * piece.getGravityCount());
            g2D.drawRect(x, y, Math.round(size), Math.round(size));
        }
    }
}