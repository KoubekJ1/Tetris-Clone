package renderer;

import game.GameState;
import game.pieces.Piece;
import game.pieces.types.*;
import util.DisplayInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The panel used for displaying the upcoming pieces, as well as the currently held piece.
 */
public class GameRightJPanel extends JPanel {
    private static ImageIcon pieceImages[];

    private JLabel heldPieceLabel;
    private JPanel upcomingPiecesPanel;
    private JLabel[] upcomingPieceLabels;

    private Font font;

    /**
     * Constructs a new <code>GameRightJPanel</code> and sets up all of its components, as well as their and its look.
     */
    public GameRightJPanel() {
        this.setBackground(Color.BLACK.brighter());
        this.setLayout(null);

        font = new Font("Segoe UI", 3, DisplayInfo.getH() / 18);

        heldPieceLabel = new JLabel("Hold");
        heldPieceLabel.setHorizontalAlignment(JLabel.CENTER);
        heldPieceLabel.setHorizontalTextPosition(JLabel.CENTER);
        heldPieceLabel.setVerticalTextPosition(JLabel.TOP);
        heldPieceLabel.setForeground(Color.WHITE);
        heldPieceLabel.setBounds(0, 0, (DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH() / 4);
        heldPieceLabel.setFont(font);

        upcomingPiecesPanel = new JPanel();
        upcomingPiecesPanel.setBackground(Color.BLACK.brighter());
        upcomingPiecesPanel.setBounds(0, heldPieceLabel.getHeight(), (DisplayInfo.getW() - DisplayInfo.getH() / 2) / 2, DisplayInfo.getH() - heldPieceLabel.getHeight());
        upcomingPiecesPanel.setLayout(new GridLayout(7, 1));

        initializeUpcomingPieceLabels();
        initializeImageIcons();

        this.add(heldPieceLabel);
        this.add(upcomingPiecesPanel);
    }

    /**
     * Updates the panel, displays the new upcoming pieces and the new currently held piece.
     */
    public void updatePanel() {
        heldPieceLabel.setIcon(getCorrespondingImageIcon(GameState.getGame().getPieceOrder().getHeldPiece()));

        int index = 1;
        for (Piece piece : GameState.getGame().getPieceOrder().getPieceOrder()) {
            upcomingPieceLabels[index].setIcon(getCorrespondingImageIcon(piece));
            index++;
        }
    }

    /**
     * Initializes the upcoming piece labels used for displaying the upcoming pieces.
     * Called by the constructor.
     */
    private void initializeUpcomingPieceLabels() {
        upcomingPieceLabels = new JLabel[7];
        for (int i = 0; i < upcomingPieceLabels.length; i++) {
            upcomingPieceLabels[i] = new JLabel();
            upcomingPieceLabels[i].setHorizontalAlignment(JLabel.CENTER);
            upcomingPieceLabels[i].setBackground(Color.BLACK.brighter());
            upcomingPiecesPanel.add(upcomingPieceLabels[i]);
        }
        upcomingPieceLabels[0].setText("Upcoming pieces");
        upcomingPieceLabels[0].setHorizontalTextPosition(JLabel.CENTER);
        upcomingPieceLabels[0].setVerticalTextPosition(JLabel.TOP);
        upcomingPieceLabels[0].setForeground(Color.WHITE);
        upcomingPieceLabels[0].setFont(font);
    }

    /**
     * Initializes the image icons used for displaying the upcoming pieces.
     */
    private void initializeImageIcons() {
        pieceImages = new ImageIcon[GameState.PIECE_COUNT];
        BufferedImage bufferedImage;
        for (int i = 0; i < GameState.PIECE_COUNT; i++) {
            try {
                bufferedImage = ImageIO.read(new File("assets/textures/pieces/" + i + ".png"));
                if (bufferedImage == null) throw new IOException("Image not initialized");
                pieceImages[i] = new ImageIcon(bufferedImage.getScaledInstance(bufferedImage.getWidth() * DisplayInfo.getW() / 5760, bufferedImage.getHeight() * DisplayInfo.getH() / 3240, Image.SCALE_SMOOTH));
            } catch (IOException e) {

            }
        }
    }

    /**
     * Gets the piece's class' corresponding image icon.
     *
     * @param piece the piece to be shown
     * @return the corresponding image icon
     */
    private static ImageIcon getCorrespondingImageIcon(Piece piece) {
        if (piece == null) return null;
        Class<? extends Piece> pieceClass = piece.getClass();
        if (pieceClass.isAssignableFrom(PieceI.class)) return pieceImages[0];
        if (pieceClass.isAssignableFrom(PieceJ.class)) return pieceImages[1];
        if (pieceClass.isAssignableFrom(PieceL.class)) return pieceImages[2];
        if (pieceClass.isAssignableFrom(PieceO.class)) return pieceImages[3];
        if (pieceClass.isAssignableFrom(PieceS.class)) return pieceImages[4];
        if (pieceClass.isAssignableFrom(PieceT.class)) return pieceImages[5];
        if (pieceClass.isAssignableFrom(PieceZ.class)) return pieceImages[6];
        throw new IllegalArgumentException("Invalid piece type!");
    }
}
