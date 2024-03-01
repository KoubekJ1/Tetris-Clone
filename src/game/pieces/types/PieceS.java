package game.pieces.types;

import game.pieces.Piece;

import java.awt.*;

public class PieceS extends Piece {
    public PieceS() {
        super(Color.GREEN, 1);
    }

    @Override
    protected Point[] getStartingTiles() {
        return new Point[]{
                new Point(3, 1),
                new Point(4, 1),
                new Point(4, 0),
                new Point(5, 0)};
    }
}
