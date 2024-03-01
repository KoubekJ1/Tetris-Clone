package util;

import game.GameState;
import renderer.Renderer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Handles all button events during the game.
 * The individual methods should be called when their corresponding actions occur.
 */
public abstract class InputHandler {
    private static HashMap<Integer, Boolean> keyPressed = new HashMap<>();

    /**
     * Gets whether the key is being held down.
     *
     * @param key the key to be checked
     * @return whether the key is being held down
     */
    public static boolean getKeyPressed(int key) {
        if (keyPressed.containsKey(key)) return keyPressed.get(key);
        return false;
    }

    /**
     * Sets the key as held down and carries out its corresponding functionality.
     *
     * @param key the key that was pressed
     */
    public static void pressKey(int key) {
        keyPressed.put(key, true);
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            // Move left
            GameState.getGame().getFallingPiece().move(-1);
            Renderer.render();
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            // Move right
            GameState.getGame().getFallingPiece().move(1);
            Renderer.render();
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            // Rotate piece clockwise
            GameState.getGame().getFallingPiece().rotate(1);
        } else if (key == KeyEvent.VK_Y || key == KeyEvent.VK_Z || key == KeyEvent.VK_CONTROL) {
            // Rotate piece counterclockwise
            GameState.getGame().getFallingPiece().rotate(-1);
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            // Soft drop
            GameState.getGame().softDrop();
        } else if (key == KeyEvent.VK_SPACE) {
            // Hard drop (piece falls down immediately)
            GameState.getGame().hardDrop();
        } else if (key == KeyEvent.VK_C || key == KeyEvent.VK_SHIFT) {
            // Hold
            if (!GameState.getGame().getFallingPiece().wasAlreadyHeld()) GameState.getGame().hold();
        }
    }

    /**
     * Sets the key as released, i.e. not held down.
     *
     * @param key the key to be released
     */
    public static void releaseKey(int key) {
        keyPressed.put(key, false);
        if (key == 83 || key == 40) {
            GameState.getGame().resetGravityTimerDelay();
        }
    }

    /**
     * Carries out the mouse button's functionality.
     *
     * @param button the mouse button that was pressed down
     */
    public static void pressMouse(int button) {
        if (button == MouseEvent.BUTTON1) {
            GameState.getGame().hardDrop();
        }
        if (button == MouseEvent.BUTTON3) {
            GameState.getGame().getFallingPiece().rotate(1);
        }
    }
}
