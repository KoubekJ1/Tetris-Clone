package util;


import javax.swing.*;

/**
 * Used to keep track of the resolution of the user's monitor.
 */
public abstract class DisplayInfo {
    //public static int SCREEN_RES_X = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    //public static int SCREEN_RES_Y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static int w, h;

    /**
     * Saves the resolution of the given <code>JFrame</code>.
     *
     * @param frame the frame whose resolution is going to be saved
     * @return whether the resolution was set (false if the window wasn't yet initialized)
     */
    public static boolean setResolution(JFrame frame) {
        w = frame.getContentPane().getWidth();
        h = frame.getContentPane().getHeight();
        if (w < 10 || h < 10) return false;
        return true;
    }

    public static int getW() {
        return w;
    }

    public static int getH() {
        return h;
    }
}
