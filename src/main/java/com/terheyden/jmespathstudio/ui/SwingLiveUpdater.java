package com.terheyden.jmespathstudio.ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * You know those apps where some result output updates instantly when you type some input?
 * That's what this does.
 *
 * It runs on its own thread, listening to keypresses.
 * It calls your Swing update func after a certain amount of idle time.
 */
public class SwingLiveUpdater {

    private long keyDelay;              // Idle time before updating.
    private Runnable updateFunc;        // User's func to run after a delay.
    private long lastKeyType = 0L;      // Time of last keypress.
    private boolean hasUpdated = false; // Track whether we've already updated or not since last keypress.
    private final Thread updateThread;  // Our inner thread that watches and does the update.

    /**
     * Start a watcher thread that will do live Swing updates after a key delay.
     * @param keyDelay how long to wait after the last keypress before running the update func
     * @param updateFunc func to run periodically
     */
    public SwingLiveUpdater(long keyDelay, Runnable updateFunc) {

        this.keyDelay = keyDelay;
        this.updateFunc = updateFunc;

        updateThread = new Thread(updater(), "SwingLiveUpdater");
        updateThread.setDaemon(true);
        updateThread.start();
    }

    /**
     * Notify that the user has inputted something, so we can adjust the idle time.
     */
    public void keyTyped(KeyEvent e) {

        synchronized (this) {
            lastKeyType = System.currentTimeMillis();
            hasUpdated = false;
        }
    }

    /**
     * Generate the thread's update method.
     */
    private Runnable updater() {

        return new Runnable() {
            @Override
            public void run() {

                while (true) {

                    long now;
                    long timeDiff;
                    boolean keyNeverTyped;
                    boolean tooSoon;

                    synchronized (this) {

                        // Get state metrics:
                        now = System.currentTimeMillis();
                        timeDiff = now - lastKeyType;
                        keyNeverTyped = lastKeyType == 0L;
                        tooSoon = timeDiff < keyDelay;

                        // If enough time has passed since the last keypress,
                        // go ahead and run the update func.

                        if (!keyNeverTyped && !tooSoon && !hasUpdated) {

                            try {
                                SwingUtilities.invokeAndWait(updateFunc);
                            } catch (InterruptedException | InvocationTargetException e) {
                                e.printStackTrace();
                            }

                            hasUpdated = true;
                        }

                    } // end synchronized.

                    // Sleep as little as possible.

                    try {
                        if (keyNeverTyped) {
                            Thread.sleep(keyDelay);                         // No keypress yet, sleep normally.
                        } else if (tooSoon) {
                            Thread.sleep(keyDelay - timeDiff + 10);   // Wait just a little longer.
                        } else {
                            Thread.sleep(keyDelay);                        // Idle, sleep normal.
                        }
                    } catch (InterruptedException e) {
                        // Ignore.
                    }
                }
            }
        };
    }
}
