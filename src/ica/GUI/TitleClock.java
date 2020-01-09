package ica.GUI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 * Clock that runs in its own thread to be used on the GUI panels.
 *
 * @author v8077971
 */
public class TitleClock implements Runnable {

    private final JFrame myFrame;
    private final Thread timeThread;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final Date date = new Date();

    /**
     * Constructor for the title clock class, the clock runs in the title bar
     * for the observer GUI that allows a real time clock to run at the same
     * time.
     *
     * @param frame the frame which is being updated
     */
    public TitleClock(JFrame frame) {
        myFrame = frame;
        timeThread = new Thread(this);
        timeThread.start();
    }

    /**
     * The run method that runs and repeatedly runs and updates freezing to
     * allow other threads a chance to be ran instead. This should never be
     * called directly throughout the program.
     */
    @Override
    public void run() {
        while (myFrame.isVisible()) {
            myFrame.setTitle(dateFormat.format(new Date()));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }
}
