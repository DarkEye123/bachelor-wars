// Internal action code for project bachelor_wars

package jason;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class test extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	
        if (terms[0].isList())
        	System.out.println("list");
        else
        	System.out.println("nie je list");
    	return true;
    }
}
