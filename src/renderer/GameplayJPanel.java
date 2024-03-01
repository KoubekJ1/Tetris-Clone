package renderer;

import game.GameState;
import util.DisplayInfo;
import util.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The panel used for displaying the gameplay.
 */
public class GameplayJPanel extends JPanel implements MouseMotionListener, MouseListener {

    /**
     * Constructs a new <code>GameplayJPanel</code> and assigns its values.
     */
    GameplayJPanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        this.setPreferredSize(new Dimension(DisplayInfo.getH() / 2, DisplayInfo.getH()));
        this.setBackground(Color.BLACK);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Invoked when the mouse is moved.
     * If the tile that the cursor is hovering over changes, the panel gets refreshed, displaying the new piece position.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int moveAmount = (int) (e.getX() / ((DisplayInfo.getH() / 2) / GameState.TILE_COUNT_X) - GameState.getGame().getFallingPiece().getTiles()[GameState.getGame().getFallingPiece().getCenterTile()].getX());
        if (moveAmount == 0) return;
        GameState.getGame().getFallingPiece().move(moveAmount);
        Renderer.render();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        InputHandler.pressMouse(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
