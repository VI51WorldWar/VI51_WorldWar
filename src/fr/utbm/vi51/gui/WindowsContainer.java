package fr.utbm.vi51.gui;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.environment.Environment;

/**
 * @author Top-K
 *
 */
public class WindowsContainer extends Agent {
    /**
     *
     */
    private static final long serialVersionUID = 2644080298439059134L;

    private Window wind;

    @Override
    public Status activate(Object... params) {
        wind = new Window();
        sendMessage(new StringMessage("test"), Environment.getInstance().getAddress());
        return StatusFactory.ok(this);

    }

    @Override
    public Status live() {

        try {
            wind.repaint();
            Thread.sleep(30);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
