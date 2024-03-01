package game.pieces.types;

import game.pieces.Piece;

import java.awt.*;

public class PieceL extends Piece {
    public PieceL() {
        super(Color.ORANGE, 1);
    }

    @Override
    protected Point[] getStartingTiles() {
        return new Point[]{
                new Point(3, 1),
                new Point(4, 1),
                new Point(5, 1),
                new Point(5, 0)};
    }
}
