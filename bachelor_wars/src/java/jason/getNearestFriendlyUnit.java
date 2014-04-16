// Internal action code for project bachelor_wars

package jason;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getNearestFriendlyUnit extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
        ts.getAg().getLogger().info("executing internal action 'jason.getNearestFriendlyUnit'");
        if (true) { // just to show how to throw another kind of exception
            throw new JasonException("not implemented!");
        }
        
        // everything ok, so returns true
        return true;
    }
}
