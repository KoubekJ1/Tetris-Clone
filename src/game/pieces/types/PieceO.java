package game.pieces.types;

import game.pieces.Piece;

import java.awt.*;

public class PieceO extends Piece {
    public PieceO() {
        super(Color.YELLOW, 0);
    }

    @Override
    protected Point[] getStartingTiles() {
        return new Point[]{
                new Point(4, 0),
                new Point(5, 0),
                new Point(4, 1),
                new Point(5, 1)};
    }
}
