package game;

import java.awt.*;

/**
 * A <code>Tile</code> instance serves as a way to keep track of the tile's parameters, i.e. its color and whether it's occupied.
 */
public class Tile {

    private boolean occupied;
    private Color color;
    private boolean marked;

    /**
     * Constructs a new <code>Tile</code> instance, setting it as unoccupied with no color.
     */
    public Tile() {
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns whether the tile is marked, meaning whether it is supposed to be rendered as white (used for animations).
     *
     * @return whether the tile is marked
     */
    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}