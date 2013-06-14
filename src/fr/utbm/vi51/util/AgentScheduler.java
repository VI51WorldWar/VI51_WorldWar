package fr.utbm.vi51.util;

import java.util.Collection;
import java.util.Iterator;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.AgentActivator;
import org.janusproject.kernel.schedule.ActivationStage;
import org.janusproject.kernel.util.directaccess.DirectAccessCollection;
import org.janusproject.kernel.util.directaccess.SafeIterator;

import fr.utbm.vi51.environment.Environment;

/**
 *
 * @author valentin
 *
 */
public class AgentScheduler extends AgentActivator {
    /**
     * Collection of agents that are not situated nor environment.
     */
    //private final Collection<Agent> otherAgents = new ArrayList<Agent>();

    /**
     * Collection of the situated agents.
     */
    private Environment env;

    @Override
    protected Iterator<? extends Agent> getExecutionPolicy(
            ActivationStage stage, Collection<? extends Agent> candidates) {
        // TODO Auto-generated method stub
        return new MyIterator(candidates.iterator());
    }

    @Override
    protected SafeIterator<Agent> getExecutionPolicy(ActivationStage stage,
            DirectAccessCollection<Agent> candidates) {
        // TODO Auto-generated method stub
        return new MyIterator(candidates.iterator());
    }

    /**
     * @author Theo
     *
     */
    private class MyIterator extends SafeIterator<Agent> {

        private final Iterator<? extends Agent> candidate;
        private Environment e = env;
        private Agent next;

        public MyIterator(Iterator<? extends Agent> candidate) {
            this.candidate = candidate;
            searchNext();
        }

        private void searchNext() {
            next = null;
            while (next == null && this.candidate.hasNext()) {
                Agent a = candidate.next();
                if (a != e) {
                    next = a;
                }
            }
            if (next == null) {
                next = e;
                e = null;
            }
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return next != null;
        }

        @Override
        public Agent next() {
            Agent a = next;
            searchNext();
            // TODO Auto-generated method stub
            return a;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
