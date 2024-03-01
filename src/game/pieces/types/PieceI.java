package game.pieces.types;

import game.pieces.Piece;

import java.awt.*;

public class PieceI extends Piece {
    public PieceI() {
        super(Color.CYAN, 1);
    }

    @Override
    public Point[] getStartingTiles() {
        return new Point[]{
                new Point(3, 0),
                new Point(4, 0),
                new Point(5, 0),
                new Point(6, 0)};
    }
}
