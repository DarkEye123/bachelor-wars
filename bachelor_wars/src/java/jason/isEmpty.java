package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import mapping.Node;

/**
 * Action that can tell if given location on grid is empty (means without unit)  or not
 * Usage: isEmpty(x,y), isEmpty([x,y]) or isEmpty(objId)
 * @author darkeye
 *
 */
public class isEmpty extends DefaultInternalAction {
	private static final long serialVersionUID = 7680270024954343894L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms)
			throws Exception {
		
		return !Node.getNode(terms).containUnit(); //if contain unit, it's not empty, otherwise it is
	}

}
