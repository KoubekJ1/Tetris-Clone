package game.pieces;

import game.GameState;
import game.pieces.types.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * A <code>PieceOrder</code> instance serves as a queue for pre-generated upcoming pieces.
 * In Tetris, the order in which you get the pieces is based on sets of pieces, which contain only 1 of each piece.
 * <code>PieceOrder</code> has 2 such sets generated at all times, so the user can always see the next 6 upcoming pieces.
 */
public class PieceOrder {

    private LinkedList<Piece> currentPieceList;
    private LinkedList<Piece> nextPieceList;

    private Piece heldPiece;

    private ArrayList<Piece> pieceTypes;

    /**
     * Constructs a new <code>PieceOrder</code> instance, generates the upcoming pieces.
     */
    public PieceOrder() {
        currentPieceList = new LinkedList<>();
        nextPieceList = new LinkedList<>();
        pieceTypes = new ArrayList<>();
        heldPiece = null;

        fillList(currentPieceList);
        fillList(nextPieceList);
    }

    /**
     * Fills the list with a set of pieces in a random order.
     * The list will contain 1 of each piece type.
     *
     * @param list the list to be filled
     */
    private void fillList(LinkedList<Piece> list) {
        generatePieces();
        Random random = new Random();
        int addedPieceIndex;
        for (int i = pieceTypes.size(); i > 0; i--) {
            addedPieceIndex = random.nextInt(i);
            list.add(pieceTypes.get(addedPieceIndex));
            pieceTypes.remove(addedPieceIndex);
        }
    }

    /**
     * Generates a new set of pieces for use when filling a list with pieces.
     * Called by the <code>fillList()</code> method, there's otherwise no need to call it.
     */
    private void generatePieces() {
        pieceTypes.add(new PieceI());
        pieceTypes.add(new PieceO());
        pieceTypes.add(new PieceT());
        pieceTypes.add(new PieceS());
        pieceTypes.add(new PieceZ());
        pieceTypes.add(new PieceJ());
        pieceTypes.add(new PieceL());
    }

    /**
     * Puts the piece into the "hold" position.
     * If there's no piece in hold, the next upcoming piece will be returned and removed from the upcoming pieces list.
     * If there already is a piece in hold, the piece in hold will be returned and the given piece will replace its spot.
     * The pieces' positions are reset.
     *
     * @param piece
     * @return the held piece/the upcoming piece
     */
    public Piece hold(Piece piece) {
        Piece rtrnPiece;
        if (heldPiece != null) {
            rtrnPiece = heldPiece;
            rtrnPiece.calculateGravityCount();
            heldPiece = piece;
            heldPiece.setAlreadyHeld();
            heldPiece.setPlaceDelay(false);
            heldPiece.resetTiles();
            return rtrnPiece;
        } else {
            heldPiece = piece;
            heldPiece.setAlreadyHeld();
            heldPiece.resetTiles();
            rtrnPiece = getNextPiece();
            rtrnPiece.setAlreadyHeld();
            rtrnPiece.calculateGravityCount();
            removePiece();
            return rtrnPiece;
        }
    }

    /**
     * Returns the next upcoming piece.
     *
     * @return the upcoming piece
     */
    public Piece getNextPiece() {
        Piece piece = currentPieceList.getFirst();
        piece.calculateGravityCount();
        return piece;
    }

    /**
     * Removes the next upcoming piece from the list.
     * If the piece was the last in the current set, the set will be replaced with the next set, and a new set of pieces will be generated.
     */
    public void removePiece() {
        currentPieceList.removeFirst();
        if (currentPieceList.size() <= 0) {
            currentPieceList = nextPieceList;
            nextPieceList = new LinkedList<>();
            fillList(nextPieceList);
        }
    }

    /**
     * Returns the next 6 upcoming pieces.
     * Used for displaying the upcoming pieces in a <code>GameRightJPanel</code> instance.
     *
     * @return the next 6 upcoming pieces
     */
    public LinkedList<Piece> getPieceOrder() {
        LinkedList<Piece> pieceOrder = new LinkedList<>();

        pieceOrder.addAll(currentPieceList);
        if (pieceOrder.size() >= 6) {
            if (pieceOrder.size() > 6) {
                pieceOrder.removeLast();
            }
            return pieceOrder;
        }

        int remainingPieceCount = GameState.PIECE_COUNT - 1 - pieceOrder.size();
        for (Piece piece : nextPieceList) {
            if (remainingPieceCount <= 0) break;
            remainingPieceCount--;
            pieceOrder.add(piece);
        }

        if (pieceOrder.size() != 6) throw new RuntimeException("Piece order size incorrect!");
        return pieceOrder;
    }

    public Piece getHeldPiece() {
        return this.heldPiece;
    }
}
