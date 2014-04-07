package jason;

import mapping.Node;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.NumberTerm;

public class isEmpty extends DefaultInternalAction {
	private static final long serialVersionUID = 7680270024954343894L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms)
			throws Exception {
		int x = (int)((NumberTerm) terms[0]).solve();
        int y = (int)((NumberTerm) terms[1]).solve();
        
        return !Node.getNode(x, y).containUnit(); //if contain unit, it's not empty, otherwise it is
	}

}
