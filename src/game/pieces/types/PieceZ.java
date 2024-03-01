package game.pieces.types;

import game.pieces.Piece;

import java.awt.*;

public class PieceZ extends Piece {
    public PieceZ() {
        super(Color.RED, 2);
    }

    @Override
    protected Point[] getStartingTiles() {
        return new Point[]{
                new Point(3, 0),
                new Point(4, 0),
                new Point(4, 1),
                new Point(5, 1)};
    }
}
