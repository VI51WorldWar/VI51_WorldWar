package fr.utbm.vi51.gui;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

/**
 * @author Top-K
 *
 */
public class WindowsContainer extends Agent {
    private Window wind;

    @Override
    public Status activate(Object... params) {
        wind = new Window();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        wind.repaint();
        return null;
    }

}
