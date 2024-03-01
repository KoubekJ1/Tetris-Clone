package game.pieces;

import game.GameState;
import renderer.Renderer;

import java.awt.*;

/**
 * Represents a falling piece in the game.
 * Subclasses of this class have pre-defined colors, starting tiles and center tile indexes.
 */
public abstract class Piece {
    private Point[] tiles;
    private int centerTile;
    private Color color;
    private int gravityCount;

    private boolean placeDelay;

    private boolean alreadyHeld;

    /**
     * Constructs a new <code>Piece</code> instance.
     *
     * @param color      the piece's color
     * @param centerTile the piece's center tile's index
     */
    public Piece(Color color, int centerTile) {
        this.tiles = getStartingTiles();
        this.centerTile = centerTile;
        this.color = color;
        this.alreadyHeld = false;
        placeDelay = false;
    }

    /**
     * Lowers this piece by 1 tile.
     * Before the piece places down (returns <code>false</code>), there's a delay, the length of which is 1 gravity interval.
     *
     * @return whether it was able to fall
     */
    public boolean gravity() {
        if (gravityCount <= 0) {
            if (!placeDelay) {
                placeDelay = true;
                return true;
            }
            return false;
        }
        gravityCount--;
        placeDelay = false;
        for (Point piece : tiles) {
            piece.y++;
        }
        return true;
    }

    /**
     * Calculates and sets the amount of times the piece has to fall before it touches the ground.
     * Called whenever the piece moves.
     */
    public void calculateGravityCount() {
        int lowestGravityCount = GameState.TILE_COUNT_Y;
        int currentGravityCount = GameState.TILE_COUNT_Y;
        for (Point tile : tiles) {
            for (int i = tile.y; i < GameState.TILE_COUNT_Y; i++) {
                if (GameState.getGame().getTiles()[tile.x][i].isOccupied()) {
                    break;
                }
                currentGravityCount = i - tile.y;
            }
            if (currentGravityCount < lowestGravityCount) lowestGravityCount = currentGravityCount;
        }
        gravityCount = lowestGravityCount;
    }

    public int getGravityCount() {
        return gravityCount;
    }

    public boolean isPlaceDelay() {
        return placeDelay;
    }

    public void setPlaceDelay(boolean placeDelay) {
        this.placeDelay = placeDelay;
    }

    /**
     * Moves the piece 1 step to the left if it is able to move.
     *
     * @return whether the piece moved
     */
    private boolean moveLeft() {
        if (!canMoveHorizontal(-1)) return false;
        for (Point piece : tiles) {
            piece.x--;
        }
        return true;
    }

    /**
     * Moves the piece 1 step to the right if it is able to move.
     *
     * @return whether the piece moved
     */
    private boolean moveRight() {
        //System.out.println("Move right!");
        if (!canMoveHorizontal(1)) return false;
        for (Point piece : tiles) {
            piece.x++;
        }
        return true;
        //System.out.println("Moved right");
    }

    /**
     * Moves the piece 1 row up if it is able to move.
     *
     * @return whether the piece moved
     */
    private boolean moveUp() {
        if (!canMoveVertical(-1)) return false;
        for (Point piece : tiles) {
            piece.y--;
        }
        return true;
    }

    /**
     * Moves the piece in a horizontal direction the given number of times.
     * If the number of tiles is positive, the piece moves to the right.
     * If the number of tiles is negative, the piece moves to the left.
     *
     * @param numberOfTiles the number of times the piece is supposed to move
     * @return whether the piece moved
     */
    public boolean move(int numberOfTiles) {
        boolean moved = false;
        for (int i = 0; i < Math.abs(numberOfTiles); i++) {
            if (numberOfTiles > 0) moved = moveRight();
            if (numberOfTiles < 0) moved = moveLeft();
        }
        if (moved) {
            calculateGravityCount();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether the piece can move in the given horizontal direction.
     * Called whenever the piece is supposed to move in the given direction.
     *
     * @param direction the direction the piece is supposed to move in (1 - Right; -1 - Left)
     * @return whether the piece can move in the given direction
     * @throws IllegalArgumentException if the direction is a number other than 1 or -1
     */
    private boolean canMoveHorizontal(int direction) throws IllegalArgumentException {
        if (direction != 1 && direction != -1) throw new IllegalArgumentException("Direction must be 1 or -1");
        for (Point piece : tiles) {
            if (piece.getX() + direction < 0 || piece.getX() + direction > GameState.TILE_COUNT_X - 1) return false;
            if (GameState.getGame().getTiles()[(int) (piece.getX() + direction)][(int) (piece.getY())].isOccupied())
                return false;
        }
        return true;
    }

    /**
     * Returns whether the piece can move in the given vertical direction.
     * Called when the piece is supposed to move in the given direction.
     *
     * @param direction the direction the piece is supposed to move in (1 - Down; -1 - Up)
     * @return whether the piece can move in the given direction
     * @throws IllegalArgumentException if the direction is a number other than 1 or -1
     */
    private boolean canMoveVertical(int direction) throws IllegalArgumentException {
        if (direction != 1 && direction != -1) throw new IllegalArgumentException("Direction must be 1 or -1");
        for (Point piece : tiles) {
            if (piece.getY() + direction < 0 || piece.getY() + direction > GameState.TILE_COUNT_Y - 1) return false;
            if (GameState.getGame().getTiles()[piece.x][piece.y + direction].isOccupied()) return false;
        }
        return true;
    }

    /**
     * Rotates the piece in the given direction.
     * If the piece can not move due to an occupied tile, nothing happens.
     * If the rotated piece is out of bounds, it is moved back inside the game boundaries.
     *
     * @param direction the direction the piece is supposed to rotate in (1 - Clockwise; -1 - Counter-clockwise)
     * @throws IllegalArgumentException if the direction is a number other than 1 or -1
     */
    public void rotate(int direction) throws IllegalArgumentException {
        if (direction != 1 && direction != -1) throw new IllegalArgumentException("Direction must be 1 or -1");
        if (centerTile == 0) return;
        Point[] rotatedTiles = new Point[4];
        int pointX;
        int pointY;
        for (int i = 0; i < tiles.length; i++) {
            pointX = tiles[centerTile].x - (tiles[i].y - tiles[centerTile].y) * direction;
            pointY = tiles[centerTile].y + (tiles[i].x - tiles[centerTile].x) * direction;
            if (pointX < 0) {
                if (!move(1)) return;
                rotate(direction);
                return;

            } else if (pointX > GameState.TILE_COUNT_X - 1) {
                if (!move(-1)) return;
                rotate(direction);
                return;

            }
            if (pointY < 0) {
                return;
            } else if (pointY > GameState.TILE_COUNT_Y - 1) {
                if (!moveUp()) return;
                rotate(direction);
                return;
            }
            if (GameState.getGame().getTiles()[pointX][pointY].isOccupied()) return;
            rotatedTiles[i] = new Point(pointX, pointY);
        }
        tiles = rotatedTiles;
        calculateGravityCount();
        Renderer.render();
    }

    public Point[] getTiles() {
        return tiles;
    }

    public void resetTiles() {
        tiles = getStartingTiles();
    }

    public boolean wasAlreadyHeld() {
        return alreadyHeld;
    }

    public void setAlreadyHeld() {
        this.alreadyHeld = true;
    }

    public Color getColor() {
        return color;
    }

    protected abstract Point[] getStartingTiles();

    public int getCenterTile() {
        return centerTile;
    }
}
