// Internal action code for project bachelor_wars

package jason;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class test extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	
    	return un.unifies(terms[0], ListTermImpl.parseList("[]"));
    }
}
