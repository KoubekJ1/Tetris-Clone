package renderer;

import game.GameState;
import game.Tile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

/**
 * A <code>RowClearAnimation</code> instance serves as an animation manager.
 * When constructed, the filled rows will blink 2 times, then disappear.
 * Its functionality begins upon its construction.
 */
public class RowClearAnimation implements ActionListener {

    private Timer animationTimer;
    private int timerActions;
    private TreeSet<Integer> rows;

    /**
     * Constructs a new <code>RowClearAnimation</code> instance and plays the animation.
     * Pauses the gravity timer until the animation is done.
     * @param rows the filled rows
     */
    public RowClearAnimation(TreeSet<Integer> rows) {
        this.rows = rows;

        animationTimer = new Timer(100, this);
        animationTimer.setInitialDelay(0);
        timerActions = 0;
        GameState.getGame().stopGravityTimer();
        animationTimer.start();
    }

    /**
     * Invoked by the animation timer.
     * The filled rows will blink 2 times before they get removed and the game continues.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == animationTimer) {
            timerActions++;
            if (timerActions > 4) {
                GameState.getGame().clearFilledRows(rows);
                animationTimer.stop();
                return;
            }
            Tile tile;
            for (int row : rows) {
                for (int i = 0; i < GameState.TILE_COUNT_X; i++) {
                    tile = GameState.getGame().getTiles()[i][row];
                    tile.setMarked(!tile.isMarked());
                }
            }
            Renderer.render();

        }
    }
}
