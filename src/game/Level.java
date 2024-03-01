package game;

import renderer.GameJFrame;

/**
 * Used for keeping track of the current level and the game speed.
 */
public class Level {

    private int level;
    private int rowsCleared;

    /**
     * Constructs a new <code>Level</code> instance and assigns its initial values.
     * The game starts on level 1 with no cleared rows.
     */
    public Level() {
        level = 1;
        rowsCleared = 0;
    }

    /**
     * Starts the next level, speeds up the gravity timer and updates the time label.
     */
    public void nextLevel() {
        level++;
        rowsCleared = 0;
        GameState.getGame().setGravityTimerDelay(getGravityDelay());
        GameJFrame.getWindow().updateLevel(level);
    }

    /**
     * Increases the <code>rowsCleared</code> value by 1.
     * If the amount of cleared rows reaches 10, the game will move on to the next level, increasing its speed.
     */
    public void rowCleared() {
        rowsCleared++;
        if (rowsCleared >= 10) {
            nextLevel();
        }
    }

    /**
     * Increases the <code>rowsCleared</code> value by the given amount.
     * If the amount of cleared rows reaches 10, the game will move on to the next level, increasing its speed.
     *
     * @param rows the amount of cleared rows
     */
    public void rowCleared(int rows) {
        for (int i = 0; i < rows; i++) {
            rowCleared();
        }
    }

    /**
     * Returns the gravity timer delay of the current level based on the official gravity curve formula.
     * Gravity curve formula: (0.8-((Level-1)*0.007))^(Level-1)
     *
     * @return the gravity timer delay
     */
    public int getGravityDelay() {
        return (int) (1000 * Math.pow(0.8 - ((level - 1) * 0.007), level - 1));
    }

    @Override
    public String toString() {
        return String.valueOf(level);
    }

    public int getLevel() {
        return level;
    }
}
